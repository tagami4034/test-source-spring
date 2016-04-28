package test.sampletool.common.web;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.TypeMismatchException;

import com.sampletool.common.model.BaseSearchKeys;
import com.sampletool.common.web.WebPaging;

public class TestWebPaging {
	static public class TestSearchKeys extends BaseSearchKeys{
		public int id;
		public Integer num;
		public String str;
	}
	
	protected TestSearchKeys searchKeys;
	
	@Before
	public void setUp() throws Exception{
		this.searchKeys = createSerachKeys();
	}
	
	protected TestSearchKeys createSerachKeys(){
		TestSearchKeys s = new TestSearchKeys();
		s.set_page(2); //検索キーは0始まり、WebPagingは1始まりなことに注意
		s.set_pagesize(5);
		return s;
	}
	
	@Test
	public void testEntire() throws Exception{
		WebPaging paging = new WebPaging(this.searchKeys, 100);
		this.searchKeys.id = 12;
		this.searchKeys.str = "泉";
		
		//レスポンスで検索キーをhiddenに設定したときに正しいjson文字列が出力されるか？
		String json = paging.getSearchKeys();
		assertTrue(json.contains("id"));
		assertTrue(json.contains("str"));
		
		//リクエストを受け取ったときにjson文字列から検索キーオブジェクトを再現できるか？
		TestSearchKeys reqSearchKeys = createSerachKeys();
		WebPaging reqPaging = new WebPaging(reqSearchKeys, 100);
		reqPaging.setSearchKeys(json);
		assertEquals(12, reqSearchKeys.id);
		assertEquals("泉", reqSearchKeys.str);
		assertNull(reqSearchKeys.num);
	}
	
	/**
	 * ページ番号を指定してジャンプする機能のテスト
	 * @throws Exception
	 */
	@Test
	public void testSpecifyPageNum() throws Exception{
		WebPaging paging = new WebPaging(this.searchKeys, 100);
		this.searchKeys.str = "太郎%";
		//ページ番号を指定できるオブジェクトを取得する
		paging = paging.getPageSpecify();
		//ページ番号は0になっている
		assertEquals(0, paging.getPage());
		//レスポンスでの処理
		String json = paging.getSearchKeys();
		
		//リクエストでの処理(setSearchKeysの前にページ番号が設定されるパターン)
		TestSearchKeys reqSearchKeys = createSerachKeys();
		WebPaging reqPaging = new WebPaging(reqSearchKeys, 100);
		//ページ番号のパラメタの設定
		reqPaging.setSpecifiedPageNum(10);
		reqPaging.setSearchKeys(json);
		//検証
		assertEquals(0, reqSearchKeys.id);
		assertEquals("太郎%", reqSearchKeys.str);
		assertNull(reqSearchKeys.num);
		assertEquals(10-1, reqSearchKeys.get_page()); //reqSearchKeysのpageは0始まり
		assertEquals(5, reqSearchKeys.get_pagesize());
		
		//リクエストでの処理(setSearchKeysの後にページ番号が設定されるパターン)
		reqSearchKeys = new TestSearchKeys();
		reqPaging = new WebPaging(reqSearchKeys, 100);
		reqPaging.setSearchKeys(json);
		//ページ番号のパラメタの設定
		reqPaging.setSpecifiedPageNum(10);
		//検証
		assertEquals(0, reqSearchKeys.id);
		assertEquals("太郎%", reqSearchKeys.str);
		assertNull(reqSearchKeys.num);
		assertEquals(10-1, reqSearchKeys.get_page());
		assertEquals(5, reqSearchKeys.get_pagesize());
	}
	
	
	/**
	 * ページ番号でリンクを作る処理のテスト
	 * @throws Exception
	 */
	@Test
	public void testPageList() throws Exception{
		WebPaging paging = new WebPaging(this.searchKeys, 100);
		paging.setPageListSize(5);
		this.searchKeys.str = "太郎%";
		this.searchKeys.num = 10;
		//レスポンスでの処理
		List<WebPaging> list = paging.getPageList();
		
		//ページ番号のリンクを作った数(現在ページ（３）を中心に前後5個が作られる。)
		assertEquals(8, list.size());
		for(int i=0; i<8; ++i){
			WebPaging p = list.get(i);
			assertEquals(i+1, p.getPage());
		}
	}
	
	
	/**
	 * パラメタが改ざんされた場合の動き
	 * @throws Exception
	 */
	@Test
	public void testMulformedParams() throws Exception{
		WebPaging paging = new WebPaging(this.searchKeys, 100);
		this.searchKeys.str = "太郎%";
		//ページ番号が最大値を超えている場合のテスト
		//レスポンスでの処理
		String json = paging.getSearchKeys();
		//テストのため先頭のページ情報を書き変える
		json = json.replaceFirst("([0-9]+\\:){3}", "12:101:5:");
		
		TestSearchKeys reqSearchKeys = createSerachKeys();
		WebPaging reqPaging = new WebPaging(reqSearchKeys, 100);
		try{
			//リクエストでの処理
			reqPaging.setSearchKeys(json);
			
			assertFalse(true);
		}catch(TypeMismatchException e){
			assertEquals("ページ番号の設定が不正: page=101", e.getCause().getMessage());
		}
		

		//ページング情報の書式が不正の場合のテスト
		//レスポンスでの処理
		json = json.replaceFirst("([0-9]+\\:){3}", "12:");
		
		reqSearchKeys = createSerachKeys();
		reqPaging = new WebPaging(reqSearchKeys, 100);
		try{
			//リクエストでの処理
			reqPaging.setSearchKeys(json);
			
			assertFalse(true);
		}catch(TypeMismatchException e){
			assertEquals("ページング情報の書式が不正", e.getCause().getMessage());
		}
	}
	
	
	@Test
	public void testPreNextLast() throws Exception{
		WebPaging paging = new WebPaging(this.searchKeys, 100);
		this.searchKeys.str = "太郎%";
		//レスポンスでの処理
		String json = paging.getSearchKeys();
		
		//検索結果：100件、ページサイズ：5、現在ページ：3
		WebPaging p = paging.getFirst();
		assertEquals(1, p.getPage());
		assertTrue(p.isFirst());
		assertFalse(p.isLast());
		//
		p = paging.getLast();
		assertEquals(20, p.getPage());
		assertFalse(p.isFirst());
		assertTrue(p.isLast());
		//
		p = paging.getNext();
		assertEquals(4, p.getPage());
		assertFalse(p.isFirst());
		assertFalse(p.isLast());
		//
		p = paging.getPrev();
		assertEquals(2, p.getPage());
		assertFalse(p.isFirst());
		assertFalse(p.isLast());
	}
	
	
	@Test
	public void test_setPageSize() throws Exception{
		WebPaging paging = new WebPaging(this.searchKeys, 100);
		this.searchKeys.str = "太郎%";
		
		//
		assertEquals(5, paging.getPageSize());
		paging.setPageSize(7);
		assertEquals(7, paging.getPageSize());
		//検索キーも書き変わっていることを確認する
		assertEquals(7, this.searchKeys.get_pagesize());
	}
	
	
	@Test
	public void test_toString() throws Exception{
		WebPaging paging = new WebPaging(this.searchKeys, 100);
		this.searchKeys.str = "太郎%";
		
		//
		String str = paging.toString();
		assertTrue(str.startsWith("WebPaging[searchKeysClassName=testSearchKeys,"));
		assertTrue(str.contains(", page=3"));
		assertTrue(str.contains(", pageSize=5"));
		
	}
}
