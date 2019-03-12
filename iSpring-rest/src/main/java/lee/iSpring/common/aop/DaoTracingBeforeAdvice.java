package lee.iSpring.common.aop;

import java.lang.reflect.Method;
import java.util.Stack;

import org.springframework.aop.MethodBeforeAdvice;

import lee.iSpring.common.util.SpringContextHolder;
import lee.iSpring.common.bean.PropertiesObject;
import lee.iSpring.common.bean.SysTrace;
import lee.iSpring.common.constant.RequestCacheDataItem;

/**
 * DAO 层方法前切面
 */
public class DaoTracingBeforeAdvice implements MethodBeforeAdvice {

	public void before(Method method, Object[] args, Object target) throws Throwable {

		if (SpringContextHolder.getBean(PropertiesObject.class).getTraceMode()) {
			String guid = RequestCacheDataItem.CURRENT_REFNO.get();
			Stack<SysTrace> methodStack = RequestCacheDataItem.CURRENT_THREAD_VAR.get();
			SysTrace trace = new SysTrace(guid, method.getDeclaringClass().getName() + "." + method.getName());

			methodStack.push(trace); // 入栈
		}
	}
}
