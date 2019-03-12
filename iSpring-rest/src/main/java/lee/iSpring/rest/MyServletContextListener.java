package lee.iSpring.rest;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

public class MyServletContextListener extends ContextLoaderListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		super.contextDestroyed(event);
	}
}
