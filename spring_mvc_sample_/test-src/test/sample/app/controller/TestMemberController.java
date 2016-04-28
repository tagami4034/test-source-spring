package test.sample.app.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.util.fileloader.DataFileLoader;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;

import test.sample.app.dao.mybatis.TestMemberDao;
import test.sampletool.test.TestUtils;


import com.sample.app.business.model.Member;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
//spring security 4以上なら以下の機能が使え、CSRFトークンもテストで指定できるらしい
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;



/**<pre>MemberControllerの単体テスト。
 * Controllerの単体テストには、MockMvcRequestBuilders.*、MockMvcResultMatchers.*などのstaticメソッドのimportが
 * 必要です！
 * 
 * 【注意！！】
 * わざわざeclipseの「サーバでデバッグ」で起動してデバッグテストをするのはやめましょう！
 * tomcatを起動してデバッグすると、起動までに時間がかかり、しかも手入力で目的の画面まで進まないといけません。
 * またステータスコードの確認やView名の確認など、ブラウザ上では確認しずらい値について確認できず、期待する動きと違ってしまうかもしれません。
 * 最初はassertなどは記述せず、単体テストでControllerのデバッグをする方が効率的です。
 * できればControllerの単体テストも作っておいた方が良いと個人的には思います。
 * 
 * 【ビジネスロジックやDaoがまだ完成していない場合】
 * たいていの場合、ビジネスロジックとDaoとControllerのそれぞれを作る人が違うのが通常だと思います。
 * そうするとControllerはビジネスとDaoができていないと何も動かないので、すべて完成するまで手を出せなくなってしまいます。
 * そんなときにも単体テストが役に立ちます。
 * ビジネスロジックのモックを作り、何か適当な値を返すようにしておきます。また、このとき、パッケージは実際のビジネスロジックとは違うパッケージ名、
 * 例えば　"com.sample.mock.bussiness.MockMemberServiceImpl"　などとしておきます。
 * そして、applicationContext.xml上で、component-scan base-package="com.sample.mock.business"などと
 * 指定すれば単体テストでControllerをデバッグできます。
 * また、完成したときに記述を変更すれば、実際のビジネスロジックと入れ替えられます。
 * 入れ替えた後から結合テストが始まることになります。
 * </pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/web/applicationContext-webmvc.xml",
		"classpath:/META-INF/spring/web/spring-security.xml",
		"classpath:/META-INF/spring/service/applicationContext-service.xml",
		"classpath:/META-INF/spring/dao/mybatis/applicationContext-dao.xml",
		"classpath:/META-INF/test/applicationContext-test-conf.xml",
		"classpath:/META-INF/test/applicationContext-test-db.xml"
})
public class TestMemberController {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	//SpringSecurityの動作を模擬するため
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	//CSRFトークンを取得するため
	@Autowired
	private HttpSessionCsrfTokenRepository csrfRepository;
	@Autowired
	private WebApplicationContext wac;
	
	///
	private MockMvc mockMvc;
	///現在のセッション情報
	private MockHttpSession mockSess;

	
	@Before
	public void setup() throws Exception {
		initDb();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
		//SpringSecurityのフィルタを追加します。
		.addFilters(this.springSecurityFilterChain)
        .build();
    }
    
	
	/**
	 * Memberテーブルを初期化します。
	 * @throws Exception
	 */
	protected void initDb() throws Exception{
		//今回はPostgresですが、Oracleを使用する場合、第二引数のスキーマを指定しないとうまく動かない場合があります。
		IDatabaseConnection dbunitConn 
			= new DatabaseConnection(this.dataSource.getConnection(), null, false);
		
		try{
			//DBに値を登録する。
			TestUtils.dbCleanInsert(dbunitConn, TestMemberDao.class, "/member_list.xls");
			//シーケンスを進める
			TestUtils.resetSequence(this.jdbcTemplate);
		}finally{
			if(dbunitConn != null) try{dbunitConn.close(); }catch(Exception e){}
		}
	}
	
	/**
	 * ログインをする。
	 * CSRFトークンのチェックも行っている。
	 * ログイン後のセッション情報をthis.mockSessに保管する。
	 * このメソッドは共通化しても良いと思う。
	 * @throws Exception
	 */
	protected void login(String id, String pw) throws Exception{
		//ログイン画面
		MvcResult result = this.mockMvc.perform(get("/login.html")).andReturn();
		
		/*セッションにCSRFトークンを設定
		ちなみにSpring4以降は以下のように自分でCSRFトークンをセッションに設定しなくても、
		 .perform(post("/").with(csrf()))
		のように記述すれば動作するようです。
		ここの以下のコードは、Spring3.2の場合のCSRFトークン発行方法です。
		*/
		this.mockSess = (MockHttpSession) result.getRequest().getSession();
		CsrfToken token = this.csrfRepository.generateToken(result.getRequest());
		this.csrfRepository.saveToken(token, result.getRequest(), result.getResponse());
		
		//ログイン認証を実施
		result = this.mockMvc.perform(post("/j_spring_security_check")
			.param("j_username", id)
			.param("j_password", pw)
			.param(token.getParameterName(), token.getToken())
			.session(this.mockSess) //ここで引数にしたセッションは中身がクリアされることに注意
		)
		.andDo(print())
		.andExpect(status().isFound()) //リダイレクトされることを確認する;
		.andReturn();
		
		//セッションをログイン後の状態にする。
		this.mockSess = (MockHttpSession) result.getRequest().getSession();
	}
	
	
	/**<pre>
	 * 正常な会員情報編集画面へのアクセス。
	 * </pre>
	 * @throws Exception
	 */
	@Test
	public void testInputNormal() throws Exception{
		login("taro", "taro");
		MvcResult result = this.mockMvc.perform(get("/cust/member/edit/input.html?member.id=1")
			.session(this.mockSess)
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		//結果の取得
		ModelAndView mv = result.getModelAndView();
		Map<String, Object> model = mv.getModel();
		Member m = (Member) PropertyUtils.getProperty(model, "form.member");
		//結果の確認
		assertEquals("cust/member-Edit-Input", mv.getViewName());
		assertEquals(1, m.getId());
	}
    
    
	/**
	 * 会員情報編集でtaroでログインしたのに、taro以外のidを指定して画面アクセスした場合、
	 * アクセスエラーになる。
	 * @throws Exception
	 */
	@Test
	public void testInputInvalid_id() throws Exception{
		login("taro", "taro");
		MvcResult result = this.mockMvc.perform(get("/cust/member/edit/input.html?member.id=2")
			.session(this.mockSess)
		)
			.andDo(print())
			.andExpect(status().isForbidden())
			.andReturn();
		
		//結果の取得
		ModelAndView mv = result.getModelAndView();
		
		//結果の確認
		assertEquals("/error/app-error", mv.getViewName());
		assertEquals(AccessDeniedException.class, result.getResolvedException().getClass());
		assertEquals("不正アクセス", result.getResolvedException().getMessage());
	}
    
	
	@Test
	public void testConfirm() throws Exception{
		login("taro", "taro");
		MvcResult result = this.mockMvc.perform(post("/cust/member/edit/confirm.html")
			.session(this.mockSess)
			.param("member.id", "1")
			.param("member.age", "27")
			.param("member.name", "四朗")
			.param("member.upDate", "2015/02/21 11:12:08.000")
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		
		//結果の取得
		ModelAndView mv = result.getModelAndView();
		Map<String, Object> model = mv.getModel();
		Member m = (Member) PropertyUtils.getProperty(model, "form.member");
		
		//結果の確認
		assertEquals("cust/member-Edit-Confirm", mv.getViewName());
		assertEquals(1, m.getId());
		assertEquals(27, m.getAge());
		assertEquals("四朗", m.getName());
		assertEquals(new DateTime("2015-02-21T11:12:08.000"), m.getUpDate());
	}
	
	

	@Test
	public void testConfirmInvalid_values() throws Exception{
		login("taro", "taro");
		MvcResult result = this.mockMvc.perform(post("/cust/member/edit/confirm.html")
			.session(this.mockSess)
			.param("member.id", "1")
			.param("member.age", "27a") //エラー
			.param("member.name", "四aaa朗") //エラー
			.param("member.upDate", "2015/02/21 11:12:08.000")
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().errorCount(2))
			.andExpect(model().attributeHasFieldErrors("form", "member.age"))
			.andExpect(model().attributeHasFieldErrors("form", "member.name"))
			.andReturn();
		
		//結果の取得
		ModelAndView mv = result.getModelAndView();
		Map<String, Object> model = mv.getModel();
		
		//結果の確認
		//妥当性チェックエラーの場合、入力画面に戻る
		assertEquals("cust/member-Edit-Input", mv.getViewName());
	}
}
