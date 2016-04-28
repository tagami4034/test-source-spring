package test.sampletool.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.sampletool.common.SecurityUtils;
import com.sampletool.common.log.CommonMDCInsertingServletFilter;

public class TestSecurityUtils {
	private MockHttpServletRequest req;

	@Before
	public void beforeTest() throws Exception{
		this.req = new MockHttpServletRequest();
		//
		this.req.setRemoteHost("test-host");
	}
	
	/**
	 * getLoginId(auth)のテスト
	 * @throws Exception
	 */
	@Test
	public void test_getLoginId() throws Exception{
		Authentication auth = new UsernamePasswordAuthenticationToken("taro", "ptaro");
		String id = SecurityUtils.getLoginId(auth);
		assertEquals("taro", id);
		
		//値が無い場合
		auth = new UsernamePasswordAuthenticationToken(null, "ptaro");
		id = SecurityUtils.getLoginId(auth);
		assertNull(id);
		
		//UserDetailsの場合
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_READ"));
		roles.add(new SimpleGrantedAuthority("ROLE_UPDATE"));
		UserDetails detail = new User("taro", "taro", roles);
		auth = new UsernamePasswordAuthenticationToken(detail, "ptaro");
		id = SecurityUtils.getLoginId(auth);
		assertEquals("taro", id);
		
		//nullの場合
		id = SecurityUtils.getLoginId(null);
		assertNull(id);
	}
	
	
	/**
	 * getLoginId()のテスト
	 * @throws Exception
	 */
	@Test
	public void test_getLoginId2() throws Exception{
		//ホルダーに存在する場合
		Authentication auth = new UsernamePasswordAuthenticationToken("taro", "ptaro");
		SecurityContextHolder.getContext().setAuthentication(auth);
		String id = SecurityUtils.getLoginId();
		assertEquals("taro", id);
		
		//ホルダーに存在しない場合
		SecurityContextHolder.getContext().setAuthentication(null);
		id = SecurityUtils.getLoginId();
		assertNull(id);
	}
	
	
	@Test
	public void test_getSecurityContextFromSession() throws Exception{
		this.req.getSession();
		MockHttpSession sess = (MockHttpSession)req.getSession();
		SecurityContextImpl sctx = new SecurityContextImpl();
		sess.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sctx);
		SecurityContext sctx2 = SecurityUtils.getSecurityContextFromSession(this.req);
		//
		assertEquals(sctx, sctx2);
		
		//SecurityContextが設定されていない場合
		this.req.clearAttributes();
		this.req.getSession().setAttribute(
			HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, null);
		sctx2 = SecurityUtils.getSecurityContextFromSession(this.req);
		assertNull(sctx2);
		
		//Session自体が設定されていない場合
		this.req.setSession(null);
		sctx2 = SecurityUtils.getSecurityContextFromSession(this.req);
		assertNull(sctx2);
		
	}
	
	
	@Test
	public void test1() throws Exception{
		//abstractにしたのでclassがカバレッジを通らない。カバレッジを正しくするためにこれも通しておく
		SecurityUtils s = new SecurityUtils(){};
	}
}
