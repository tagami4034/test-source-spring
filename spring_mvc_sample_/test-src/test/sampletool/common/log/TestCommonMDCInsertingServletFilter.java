package test.sampletool.common.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import test.sampletool.test.WebTestUtils;
import ch.qos.logback.classic.ClassicConstants;

import com.sampletool.common.log.CommonMDCInsertingServletFilter;
import com.sampletool.common.log.RequestIdMdcPutter;
import com.sampletool.common.log.RequestMdcPutter;
import com.sampletool.common.log.SpringSecurityMdcPutter;



public class TestCommonMDCInsertingServletFilter {
	private MockFilterConfig config;
	private CommonMDCInsertingServletFilter filter;
	private MockHttpServletRequest req;
	private MockHttpServletResponse res;
	private MockFilterChain filterChain;
	private MockServletContext sc;
	//ApplicationContext.xmlから取得するオブジェクト
	@Autowired
	
	//テスト用の変数
	private int testId;
	
	@Before
	public void beforeTest() throws Exception{
		this.config =  new MockFilterConfig();
		this.filter = new CommonMDCInsertingServletFilter();
		this.req = new MockHttpServletRequest();
		this.res = new MockHttpServletResponse();
		this.filterChain = new MockFilterChain();
		this.sc = new MockServletContext();
		//
		this.req.setRemoteHost("test-host");
	}
	
	@Test
	public void testInit() throws Exception{
		CommonMDCInsertingServletFilter filter = new CommonMDCInsertingServletFilter();
		//テストvalueの設定
		String[] values = new String[]{
			"req.=" + RequestMdcPutter.class.getName(), //１つだけの設定値
			"  req.=" + RequestMdcPutter.class.getName() + "\n  "
				+ "web.=" + RequestIdMdcPutter.class.getName() + "\n" //複数の設定
		};
		
		//テスト値の設定
		for(String val : values){
			this.config.addInitParameter(CommonMDCInsertingServletFilter.FILTER_INIT_KEY, val);

			//例外が発生しないことを確認
			this.filter.init(config);
			Assert.assertTrue(true);
		}
	}

	/**
	 * 初期値の書式に問題があり、例外が発生するパターン
	 * @throws Exception
	 */
	@Test
	public void testInit2() throws Exception{
		//テストvalueの設定（書式間違い）
		String[] values = new String[]{
			"req.　" + RequestMdcPutter.class.getName(), //１つだけの設定値
			"=" + RequestMdcPutter.class.getName(), //キー名が無い場合
			"=" + RuntimeException.class.getName(), //派生クラスでない場合
			"  req.=aaa.ffff.Test\n  "
				+ "web.=" + RequestIdMdcPutter.class.getName() + "\n" //複数の設定
		};
		
		//テスト値の設定
		for(String val : values){
			this.config.addInitParameter(CommonMDCInsertingServletFilter.FILTER_INIT_KEY, val);

			//例外が発生することを確認
			try{
				this.filter.init(config);
			}catch(Exception e){
				Assert.assertEquals(ServletException.class, e.getClass());
			}
		}
	}
	
	@Test
	public void testRequestMdcPutter() throws Exception{
		config.addInitParameter(CommonMDCInsertingServletFilter.FILTER_INIT_KEY, 
			"req.=" + RequestMdcPutter.class.getName()
		);

		//初期化
		this.filter.init(config);
		//実行
		WebTestUtils.setPath(this.req, "/sample_mvc", "/test.html");
		this.req.addHeader("User-Agent", "IE");
		this.req.addHeader("X-Forwarded-For", "client1, proxy1");
		this.req.setQueryString("name=aaa&key=56&%56=%13");
		FilterChain f = new FilterChain(){
			@Override
			public void doFilter(ServletRequest req,ServletResponse res)
			throws IOException,ServletException {
				//テスト
				Assert.assertEquals("test-host", MDC.get(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY));
				Assert.assertEquals("/sample_mvc/test.html", MDC.get(ClassicConstants.REQUEST_REQUEST_URI));
				Assert.assertEquals("http://localhost:80/sample_mvc/test.html", MDC.get(ClassicConstants.REQUEST_REQUEST_URL));
				Assert.assertEquals("name=aaa&key=56&%56=%13", MDC.get(ClassicConstants.REQUEST_QUERY_STRING));
				Assert.assertEquals("IE", MDC.get(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY));
				Assert.assertEquals("client1, proxy1", MDC.get(ClassicConstants.REQUEST_X_FORWARDED_FOR));

				//リクエストのパス
				Assert.assertEquals("/test.html", MDC.get(RequestMdcPutter.REQUEST_PATH));
			}
		};
		
		//フィルタ実行と、テストの実行
		this.filter.doFilter(this.req, this.res, f);
		
		//フィルタ実行後はMDCからキーが削除されていることをテスト
		Assert.assertNull(MDC.get(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY));
		Assert.assertNull(MDC.get(ClassicConstants.REQUEST_REQUEST_URI));
		Assert.assertNull(MDC.get(ClassicConstants.REQUEST_REQUEST_URL));
		Assert.assertNull(MDC.get(ClassicConstants.REQUEST_QUERY_STRING));
		Assert.assertNull(MDC.get(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY));
		Assert.assertNull(MDC.get(ClassicConstants.REQUEST_X_FORWARDED_FOR));
		Assert.assertNull(MDC.get(RequestMdcPutter.REQUEST_PATH));
		Assert.assertNull(MDC.get(RequestMdcPutter.REQUEST_SESSION_ID));
	}
	
	

	@Test
	public void testRequestIdMdcPutter() throws Exception{
		RequestIdMdcPutter putter = new RequestIdMdcPutter();
		
		//フィルタ実行と、テストの実行
		putter.put("req.", this.req);
		assertEquals("1", MDC.get("req." + RequestIdMdcPutter.REQ_ID));

		putter.put("req.", this.req);
		assertEquals("2", MDC.get("req." + RequestIdMdcPutter.REQ_ID));

		//最大値までインクリメントされたとき
		putter.setCount(RequestIdMdcPutter.MAX_COUNT);
		putter.put("req.", this.req);
		assertEquals("1", MDC.get("req." + RequestIdMdcPutter.REQ_ID));
		
		//フィルタ実行後はMDCからキーが削除されていることをテスト
		putter.remove("req.");
		assertNull(MDC.get("req." + RequestIdMdcPutter.REQ_ID));
	}
	
	
	@Test
	public void testSpringSecurityMdcPutter() throws Exception{
		SpringSecurityMdcPutter sputter = new SpringSecurityMdcPutter();
		
		//セッションにSpringSecurityの認証手形を設定する。
		WebTestUtils.setPath(this.req, "/test", "/j_spring_security_check");
		MockHttpSession sess = (MockHttpSession)req.getSession();
		SecurityContextImpl sctx = new SecurityContextImpl();
		sess.setAttribute(
			HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sctx);
		
		//認証OKになっていない場合
		Authentication auth = new UsernamePasswordAuthenticationToken("taro", "ptaro");
		sctx.setAuthentication(auth);
		sputter.put("sec.", this.req);
		//テスト
		assertEquals("taro", MDC.get("sec." + SpringSecurityMdcPutter.SEC_USERNAME));
		assertEquals("", MDC.get("sec." + SpringSecurityMdcPutter.SEC_IS_AUTHED));
		assertEquals("[]", MDC.get("sec." + SpringSecurityMdcPutter.SEC_ROLES));
		
		//removeのテスト
		sputter.remove("sec.");
		assertNull(MDC.get("sec." + SpringSecurityMdcPutter.SEC_USERNAME));
		assertNull(MDC.get("sec." + SpringSecurityMdcPutter.SEC_IS_AUTHED));
		assertNull(MDC.get("sec." + SpringSecurityMdcPutter.SEC_ROLES));
		
		
		//認証OKになっている場合
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_READ"));
		roles.add(new SimpleGrantedAuthority("ROLE_UPDATE"));
		auth = new UsernamePasswordAuthenticationToken("taro", "ptaro", roles);
		sctx.setAuthentication(auth);
		sputter.put("sec.", this.req);
		//テスト
		assertEquals("taro", MDC.get("sec." + SpringSecurityMdcPutter.SEC_USERNAME));
		assertEquals("authed", MDC.get("sec." + SpringSecurityMdcPutter.SEC_IS_AUTHED));
		assertEquals("[ROLE_READ, ROLE_UPDATE]", MDC.get("sec." + SpringSecurityMdcPutter.SEC_ROLES));

		//データのクリア（フィルタの終了処理）
		sputter.remove("sec.");
		
		//何も設定されていない場合
		sess.setAttribute(
			HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, null);
		sputter.put("sec.", this.req);
		assertNull(MDC.get("sec." + SpringSecurityMdcPutter.SEC_USERNAME));
		assertNull(MDC.get("sec." + SpringSecurityMdcPutter.SEC_IS_AUTHED));
		assertNull(MDC.get("sec." + SpringSecurityMdcPutter.SEC_ROLES));
	}
	
	
}
