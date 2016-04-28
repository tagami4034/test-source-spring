package test.sampletool.test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

public class WebTestUtils {

	/**
	 * パス情報をrequestと、ServletContextに設定する
	 * @param req
	 * @param contextPath
	 * @param servletPath
	 */
	static public void setPath(MockHttpServletRequest req, String contextPath, String servletPath){
		MockServletContext sc = (MockServletContext) req.getServletContext();
		sc.setContextPath(contextPath);
		//
		req.setContextPath(contextPath);
		req.setServletPath(servletPath);
		req.setRequestURI(contextPath + servletPath);
	}
}
