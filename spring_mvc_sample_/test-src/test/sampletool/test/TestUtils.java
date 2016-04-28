package test.sampletool.test;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.XlsDataFileLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextLoaderListener;

import com.sample.app.business.model.Member;


public class TestUtils {

	private TestUtils(){}
	

	/**
	 * DBUnitを利用して、DBのテーブルをクリアしてinsertする。
	 * @param conn [in]
	 * @param pathClass [in]Excelファイルのパスのルートになるクラスパス。
	 * @param file [in]pathClassの場所を起点としてDB登録用のExcelファイルのパスを指定する。
	 * @throws Exception
	 */
	static public void dbCleanInsert(IDatabaseConnection conn, Class<?> pathClass, String file)
	 throws Exception{
		DataFileLoader loader = new XlsDataFileLoader();
		//DBに値を登録する。
		String path = TestUtils.classPackageAsResourcePath(pathClass, file);
		IDataSet dataSet = loader.load(path);
		DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
	}
	
	
	/**
	 * シーケンスを該当テーブルの最大値にする。
	 * テスト用のテーブルデータをすべて値を挿入後にこのメソッドを呼び出すこと。
	 * @throws Exception
	 */
	static public void resetSequence(JdbcTemplate jdbcTemplate) throws Exception{
		//シーケンスを進める
		jdbcTemplate.execute("select setval('seq_member_id', (select max(id) from t_member))");
	}
	
	/**<pre>
	 * 特定のオブジェクトだけ抽出するためのクラス。
	 * 【使用サンプル】<code>
	 * List<Member> ret;
	 * ret = new TestUtils.Filter&lt;Member>(list){@Override
	 * 	public boolean judge(Member val) {return val.getId() == id;}
	 * }.exec();
	 * 
	 * </code>
	 * </pre>
	 * @param <T>
	 */
	public static abstract class Filter<T>{
		List<T> list;
		public Filter(List<T> list) {
			this.list = list;
		}
		abstract public boolean judge(T val);
		public List<T> exec(){
			List<T> ret = new ArrayList<T>();
			for(T val : this.list){
				if(judge(val)) ret.add(val);
			}
			return ret;
		}
	}
	

	/**
	 * クラスパッケージからの絶対パスを返す。
	 * @param c [in]クラス
	 * @param relativePath [in]cからの相対パス
	 * @return 作成したパス（例： /com/sample/app/common/xxx.xml）
	 */
	static public String classPackageAsResourcePath(Class<?> c, String relativePath){
		String path = "/" + ClassUtils.classPackageAsResourcePath(c) + "/member_list.xls";
		return path;
	}
	
	
	/**
	 * web.xmlにcontextConfigLocationのリスナーを設定したときの動作をシミュレートする。
	 * scに、ApplicationContextが設定される。
	 * @param sc [in]ServletContextのモック
	 * @n        [out]AppliactionContextが設定されたServletContext
	 * @param files [in]初期化に使用したリスナーオブジェクト
	 * @return
	 */
	static public ContextLoaderListener contextInitialized(ServletContext sc,
			String files){
		setInitParameter(sc, "contextConfigLocation", files);
		
		//Springリスナーの初期化を起動する
		ServletContextEvent event = new ServletContextEvent(sc);
		ContextLoaderListener listener = new ContextLoaderListener();
		listener.contextInitialized(event);
		
		return listener;
	}
	
	static private void setInitParameter(ServletContext sc, String key, String value){
		if(sc instanceof org.springframework.mock.web.MockServletContext){
			((org.springframework.mock.web.MockServletContext)sc)
			.addInitParameter(key, value);
		}
	}
}
