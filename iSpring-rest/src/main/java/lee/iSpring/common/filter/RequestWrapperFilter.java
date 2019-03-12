package lee.iSpring.common.filter;

import lee.iSpring.common.bean.PropertiesObject;
import lee.iSpring.common.util.SpringContextHolder;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest 过滤器，以便 Stream 能够多次读取
 */
public class RequestWrapperFilter extends HttpServlet implements Filter {

	private static final long serialVersionUID = 1L;

	private PropertiesObject po = SpringContextHolder.getBean(PropertiesObject.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		ServletRequest requestWrapper = null;
		String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
		if(po.getExcludeUrls().contains(requestURI+",")){
			requestWrapper = httpServletRequest;
		}else{
			requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
		}

		chain.doFilter(requestWrapper, response);
	}
}
