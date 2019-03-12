package lee.iSpring.common.aop;

import java.lang.reflect.Method;
import java.util.Stack;

import lee.iSpring.common.bean.LogTraceInfo;
import lee.iSpring.common.bean.PropertiesObject;
import lee.iSpring.common.bean.SysTrace;
import lee.iSpring.common.constant.RequestCacheDataItem;
import lee.iSpring.common.util.JSONUtil;
import lee.iSpring.common.util.LoggerUtil;
import lee.iSpring.common.util.SpringContextHolder;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.aop.AfterReturningAdvice;

/**
 * 业务层方法后切面
 */
public class BizTracingAfterAdvice implements AfterReturningAdvice {

	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {

		if (SpringContextHolder.getBean(PropertiesObject.class).getTraceMode()) {
			Stack<SysTrace> methodStack = RequestCacheDataItem.CURRENT_THREAD_VAR.get();
			SysTrace trace = methodStack.pop(); // 出栈
			//Marker marker = MarkerManager.getMarker(trace.getGuid());
			String guid = RequestCacheDataItem.CURRENT_REFNO.get();
			Marker marker = new MarkerManager.Log4jMarker(guid);
			LogTraceInfo info = new LogTraceInfo();

			info.setActionName(RequestCacheDataItem.REQUEST_URL.get());
			info.setMethodName(trace.getMethodName());
			info.setParams(args.length > 0 ? JSONUtil.objToJson(args) : "void");
			info.setResult(returnValue == null ? "void" : JSONUtil.objToJson(returnValue));
			info.setRuntime(trace.getRunTime());
			info.setSource(getClass().getSimpleName());

			LoggerUtil.trace(marker, info);
		}
	}
}
