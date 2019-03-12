package lee.iSpring.common.aop;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import lee.iSpring.common.bean.LogErrorInfo;
import lee.iSpring.common.bean.LogTraceInfo;
import lee.iSpring.common.bean.StateInfo;
import lee.iSpring.common.exception.BaseException;
import lee.iSpring.common.exception.BusinessException;
import lee.iSpring.common.exception.ParamException;
import lee.iSpring.common.util.LoggerUtil;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.aop.ThrowsAdvice;

import lee.iSpring.common.constant.StateCode;
import lee.iSpring.common.constant.RequestCacheDataItem;
import lee.iSpring.common.util.JSONUtil;
import lee.iSpring.common.util.StateUtil;

/**
 * 业务层异常切面
 */
public class BizTracingThrowsAdvice implements ThrowsAdvice {

	public void afterThrowing(Method method, Object[] args, Object target, Throwable subclass) throws Exception {

		String guid = RequestCacheDataItem.CURRENT_REFNO.get();
		Marker marker = new MarkerManager.Log4jMarker(guid);
		LogErrorInfo errorInfo = new LogErrorInfo();

		errorInfo.setActionName(RequestCacheDataItem.REQUEST_URL.get());
		errorInfo.setMethodName(method.getDeclaringClass().getName() + "." + method.getName());
		errorInfo.setParams(args.length > 0 ? JSONUtil.objToJson(args) : "void");
		errorInfo.setSource(getClass().getSimpleName());

		if (subclass instanceof BusinessException) {
			BusinessException ex = (BusinessException) subclass;
			errorInfo.setResult("BusinessException - " + ex.getFormattedMsg());
			LoggerUtil.error(marker, errorInfo, ex);
			throw ex;
		} else {

			if (subclass instanceof ParamException) {
				BaseException ex = (BaseException) subclass;

				errorInfo.setResult(ex.getClass().getSimpleName() + " - " + ex.getFormattedMsg());
				LoggerUtil.error(marker, errorInfo, ex);
				throw ex;
			}else if (subclass instanceof BaseException) {
				BaseException ex = (BaseException) subclass;

				errorInfo.setResult(ex.getClass().getSimpleName() + " - " + ex.getFormattedMsg());
				LoggerUtil.error(marker, errorInfo, ex);
				throw new BusinessException(StateCode.FAILURE);
//				throw ex;
			} else if (subclass instanceof UndeclaredThrowableException) {
				UndeclaredThrowableException exception = (UndeclaredThrowableException) subclass;
				Throwable undelcaredThrowable = exception.getUndeclaredThrowable();

				errorInfo.setResult(exception.getClass().getSimpleName() + " - " + exception.getMessage());
				LoggerUtil.error(marker, errorInfo, subclass);

//				if (undelcaredThrowable instanceof DataAccessObjectException) {
//					throw (DataAccessObjectException) undelcaredThrowable;
//				}
				throw new BusinessException(StateCode.DATA_ERROR);
			} else {
				// 先根据方法名称查找错误信息
				StateInfo stateInfo = StateUtil.getStateByMethodName(method.getName());

				errorInfo.setResult(subclass.getClass().getSimpleName() + " - " + subclass.getMessage());
				LoggerUtil.error(marker, errorInfo, subclass);
				throw new BusinessException(StateCode.FAILURE);
//				throw new UnexpectedException(new State(stateInfo.getCode(), stateInfo.getMsg()));
			}
		}
	}
}
