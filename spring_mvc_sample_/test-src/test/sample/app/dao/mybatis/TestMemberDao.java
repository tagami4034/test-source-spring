package test.sample.app.dao.mybatis;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.util.fileloader.DataFileLoader;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import test.sampletool.test.TestUtils;

import com.sample.app.business.model.Member;
import com.sample.app.business.model.MemberSearchKeys;
import com.sample.app.dao.MemberDao;
import com.sampletool.common.CommonUtils;


/**<pre>
 * DBのトランザクションを行わせるため、メソッドに@Transactionalをつける
 * </pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:/META-INF/spring/dao/mybatis/applicationContext-dao.xml",
	"classpath:/META-INF/test/applicationContext-test-conf.xml",
	"classpath:/META-INF/test/applicationContext-test-db.xml"
})
public class TestMemberDao{
	@Autowired
	private DataSource dataSource;
	@Autowired
	private DataFileLoader loader;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MemberDao memberDao;
	//
	private IDatabaseConnection dbunitConn;

	@Before
	public void beforeTest() throws Exception{
		//今回はPostgresですが、Oracleを使用する場合、第二引数のスキーマを指定しないとうまく動かない場合があります。
		this.dbunitConn = new DatabaseConnection(this.dataSource.getConnection(), null, false);

		//DBに値を登録する。
		TestUtils.dbCleanInsert(this.dbunitConn, getClass(), "/product_list.xls");
		//シーケンスを進める
		TestUtils.resetSequence(this.jdbcTemplate);
	}

	@After
	public void afterTest() throws Exception{
		if(this.dbunitConn != null) try{this.dbunitConn.close(); }catch(Exception e){}
	}


	///----------------
	/**このテストで使用するテスト用のメソッド
	 * idと一致するオブジェクトを返す。
	 * @param list
	 * @param id
	 * @return
	 */
	List<Member> getById(List<Member> list, final int id){
		List<Member> ret;
		ret = new TestUtils.Filter<Member>(list){@Override
			public boolean judge(Member val) {return val.getId() == id;}
		}.exec();
		return ret;
	}

	/**このテストで使用するテスト用のメソッド
	 * 2つの値について、up_date以外が一致するかをチェックする。
	 * @param m1
	 * @param m2
	 * @return
	 */
	boolean isEquals(Member m1, Member m2){
		if(m1.getAge() != m2.getAge()) return false;
		if(m1.getId() != m2.getId()) return false;
		if(!CommonUtils.isEquals(m1.getLoginId(), m2.getLoginId())) return false;
		if(!CommonUtils.isEquals(m1.getLoginPw(), m2.getLoginPw())) return false;
		if(!CommonUtils.isEquals(m1.getName(), m2.getName())) return false;
		if(!CommonUtils.isEquals(m1.getRole(), m2.getRole())) return false;
		return true;
	}


	//-----------------------------



	@Test
	public void testObtain() throws Exception{
		//取得する
		Member mem = this.memberDao.obtainMember(1);
		assertEquals(1, mem.getId());
		assertEquals(12, mem.getAge());
		assertEquals("taro", mem.getLoginId());
		assertEquals("taro", mem.getLoginPw());
		assertEquals("太郎", mem.getName());
		assertEquals("ROLE_ADMIN", mem.getRole());
		assertEquals(new DateTime(2015,02,21, 11,12, 8), mem.getUpDate());

		//他のユーザも取得してみる
		mem = this.memberDao.obtainMember(8);
		assertEquals(8, mem.getId());
		assertEquals(3, mem.getAge());
		assertEquals("nana", mem.getLoginId());
		assertEquals("matu", mem.getLoginPw());
		assertEquals("奈菜", mem.getName());
		assertEquals("ROLE_READ", mem.getRole());
		assertEquals(new DateTime(2014,12,12, 19,12, 7), mem.getUpDate());
	}


	@Test
	public void testFindByAge() throws Exception{
		MemberSearchKeys keys = new MemberSearchKeys();
		List<Member> l;

		//取得する
		keys.setAgeFrom(3);
		keys.setAgeTo(21);
		List<Member> list = this.memberDao.findMembers(keys);
		assertEquals(3, list.size());

		//抽出された値に期待するIDがあるかを調べる
		l= getById(list, 1);
		assertEquals(1, l.size());
		//
		l= getById(list, 2);
		assertEquals(1, l.size());
		//
		l= getById(list, 8);
		assertEquals(1, l.size());
	}


	@Test
	public void testObtain_NotFound() throws Exception{
		Member m = this.memberDao.obtainMember(100);
		assertNull(m);
	}

	@Test
	public void testFind_NotFound() throws Exception{
		MemberSearchKeys keys = new MemberSearchKeys();

		//取得する
		keys.setName("aaa");
		List<Member> list = this.memberDao.findMembers(keys);
		assertNotNull(list);
		assertEquals(0, list.size());
	}


	@Test
	public void testFindByName() throws Exception{
		MemberSearchKeys keys = new MemberSearchKeys();
		List<Member> l;

		//取得する
		keys.setName("未華子");
		List<Member> list = this.memberDao.findMembers(keys);
		//検索結果数はあっているか
		assertEquals(1, list.size());
		//IDがあっているか
		assertEquals(2, list.get(0).getId());

		//取得する２（存在しない名前）
		keys.setName("未華");
		list = this.memberDao.findMembers(keys);
		//検索結果数はあっているか
		assertEquals(0, list.size());

	}


	@Test
	public void testFindByNameBW() throws Exception{
		MemberSearchKeys keys = new MemberSearchKeys();
		List<Member> l;

		//取得する
		keys.setNameBW("崎");
		List<Member> list = this.memberDao.findMembers(keys);
		assertEquals(2, list.size());

		//抽出された値に期待するIDがあるかを調べる
		l= getById(list, 5);
		assertEquals(1, l.size());
		l= getById(list, 11);
		assertEquals(1, l.size());

		//%がエスケープされているか？
		keys.setNameBW("%");
		list = this.memberDao.findMembers(keys);
		assertEquals(0, list.size());
	}



	@Test
	public void testFindByOrderBy() throws Exception{
		MemberSearchKeys keys = new MemberSearchKeys();

		//取得する
		keys.setOrderBy(MemberSearchKeys.ORDER_AGE, MemberSearchKeys.ORDER_ID);
		List<Member> list = this.memberDao.findMembers(keys);
		assertEquals(7, list.size());

		//抽出された値に期待するIDがあるかを調べる
		assertEquals(8, list.get(0).getId());
		assertEquals(1, list.get(1).getId());
		assertEquals(2, list.get(2).getId());
		assertEquals(5, list.get(3).getId());
		assertEquals(3, list.get(4).getId());
		assertEquals(10, list.get(5).getId());
		assertEquals(11, list.get(6).getId());


		//取得する２（逆順）
		keys.setOrderBy(-MemberSearchKeys.ORDER_AGE, MemberSearchKeys.ORDER_ID);
		list = this.memberDao.findMembers(keys);
		assertEquals(7, list.size());

		//抽出された値に期待するIDがあるかを調べる
		assertEquals(10, list.get(0).getId());
		assertEquals(11, list.get(1).getId());
		assertEquals(3, list.get(2).getId());
		assertEquals(5, list.get(3).getId());
		assertEquals(2, list.get(4).getId());
		assertEquals(1, list.get(5).getId());
		assertEquals(8, list.get(6).getId());
	}




	@Test
	public void testFindByAging() throws Exception{
		MemberSearchKeys keys = new MemberSearchKeys();

		//取得する
		keys.setOrderBy(MemberSearchKeys.ORDER_AGE, MemberSearchKeys.ORDER_ID);
		keys.set_pagesize(2);
		keys.set_page(0);
		List<Member> list = this.memberDao.findMembers(keys);
		assertEquals(2, list.size());
		//抽出された値に期待するIDがあるかを調べる
		assertEquals(8, list.get(0).getId());
		assertEquals(1, list.get(1).getId());

		//次のページを取得する
		keys.set_pagesize(2);
		keys.set_page(1);
		list = this.memberDao.findMembers(keys);
		assertEquals(2, list.size());
		//IDの存在チェック
		assertEquals(2, list.get(0).getId());
		assertEquals(5, list.get(1).getId());

		//次のページを取得する
		keys.set_pagesize(2);
		keys.set_page(2);
		list = this.memberDao.findMembers(keys);
		assertEquals(2, list.size());
		//IDの存在チェック
		assertEquals(3, list.get(0).getId());
		assertEquals(10, list.get(1).getId());

		//次のページを取得する
		keys.set_pagesize(2);
		keys.set_page(3);
		list = this.memberDao.findMembers(keys);
		assertEquals(1, list.size());
		//IDの存在チェック
		assertEquals(11, list.get(0).getId());

		//次のページを取得する
		keys.set_pagesize(2);
		keys.set_page(4);
		list = this.memberDao.findMembers(keys);
		assertEquals(0, list.size());
	}




	@Test
	public void testUpdate() throws Exception{
		Member mem = this.memberDao.obtainMember(8);
		mem.setName("奈々子");

		//更新する
		this.memberDao.updateMember(mem);
		Member after = this.memberDao.obtainMember(8);
		//値が一致するか(念のためアドレスが同じでないか確認してから)
		assertTrue(after != mem);
		assertTrue(isEquals(mem, after));
		assertEquals(mem.getUpDate(), after.getUpDate());
		//
	}


	/**
	 * 楽観的ロックがかかることをテストする。
	 * @throws Exception
	 */
	@Test(expected=OptimisticLockingFailureException.class)
	public void testUpdate2_optimistick() throws Exception{
		Member mem = this.memberDao.obtainMember(8);
		//他の誰かが更新したことを想定（バージョンを１つ進める）
		mem.setVersion(1);

		//更新する
		this.memberDao.updateMember(mem);
	}


	@Test
	public void testInsert() throws Exception{
		Member before = this.memberDao.obtainMember(8);
		Member after = this.memberDao.obtainMember(8);
		after.setLoginId("aiai");
		after.setUpDate(null);

		//ID=8をコピーする
		this.memberDao.insertMember(after);

		//insertした結果をテストする
		assertEquals(12, after.getId());
		assertNotNull(after.getUpDate());
		//更新日が更新されていることを確認
		assertNotSame(before.getUpDate(), after.getUpDate());
		//ID以外が一致することを確認する
		after.setId(8);
		after.setLoginId(before.getLoginId());
		assertTrue(isEquals(before, after));

		//----------------------------
		//insertした値を更新できるか？
		after = this.memberDao.obtainMember(12);
		after.setAge(11);
		after.setName("あはは");
		this.memberDao.updateMember(after);
		//更新のオブジェクトと、DBに保存されたオブジェクトが一致するか確認する。
		Member m = this.memberDao.obtainMember(12);
		assertTrue(isEquals(after, m));
	}


}
