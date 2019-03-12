package lee.iSpring.common.util;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 以静态变量保存 Spring ApplicationContext
 */
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	@Override
	public void destroy() throws Exception {
		SpringContextHolder.clear();
	}

	/**
	 * 清除 ApplicationContext
	 */
	public static void clear() {

		applicationContext = null;
	}

	/**
	 * 实现 ApplicationContextAware 接口，注入 Context 到静态变量中
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {

		SpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 取得 ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {

		assertContextInjected();

		return applicationContext;
	}

	/**
	 * 从 AapplicationContext 取得Bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {

		assertContextInjected();

		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从 ApplicationContext 取得 Bean
	 */
	public static <T> T getBean(Class<T> requiredType) {

		assertContextInjected();

		return (T) applicationContext.getBean(requiredType);
	}

	/**
	 * 从 ApplicationContext 取得 Bean
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {

		assertContextInjected();

		return applicationContext.getBean(name, requiredType);
	}

	/**
	 * 检查 ApplicationContext
	 */
	private static void assertContextInjected() {

		if (applicationContext == null) {
			throw new IllegalStateException("ApplicationContext 未注入！请修改 Spring 配置。");
		}
	}
}
