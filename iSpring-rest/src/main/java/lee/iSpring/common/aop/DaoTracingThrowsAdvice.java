package lee.iSpring.common.aop;

import java.lang.reflect.Method;

import lee.iSpring.common.bean.LogErrorInfo;
import lee.iSpring.common.exception.BusinessException;
import lee.iSpring.common.exception.DataAccessObjectException;
import lee.iSpring.common.util.LoggerUtil;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.aop.ThrowsAdvice;

import lee.iSpring.common.constant.RequestCacheDataItem;
import lee.iSpring.common.constant.StateCode;
import lee.iSpring.common.util.JSONUtil;

/**
 * DAO 层异常切面
 */
public class DaoTracingThrowsAdvice implements ThrowsAdvice {

	public void afterThrowing(Method method, Object[] args, Object target, Throwable subclass)
			throws DataAccessObjectException,BusinessException {

		LogErrorInfo errorInfo = new LogErrorInfo();

		errorInfo.setActionName(RequestCacheDataItem.REQUEST_URL.get());
		errorInfo.setMethodName(method.getDeclaringClass().getName() + "." + method.getName());
		errorInfo.setParams(args.length > 0 ? JSONUtil.objToJson(args) : "void");
		errorInfo.setSource(getClass().getSimpleName());
		String guid = RequestCacheDataItem.CURRENT_REFNO.get();
		Marker marker = new MarkerManager.Log4jMarker(guid);


		if (subclass instanceof DataAccessObjectException) {
//			throw (DataAccessObjectException) subclass;
			BusinessException ex = (BusinessException) subclass;
			LoggerUtil.error(marker, errorInfo, ex);
			throw new BusinessException(StateCode.DATA_ERROR);

		} else if (subclass instanceof BusinessException) {
			BusinessException ex = (BusinessException) subclass;
			errorInfo.setResult(ex.getClass().getSimpleName() + " - " + ex.getMessage());
			LoggerUtil.error(marker, errorInfo, ex);
			throw new BusinessException(StateCode.DATA_ERROR);
		}else {
			DataAccessObjectException exception = new DataAccessObjectException(StateCode.DATABASE_ERROR);

			exception.setStackTrace(subclass.getStackTrace());
			errorInfo.setResult(subclass.getClass().getSimpleName() + " - " + subclass.getMessage());
			LoggerUtil.error(marker, errorInfo, subclass);
//			throw exception;
			throw new BusinessException(StateCode.DATA_ERROR);
		}
	}
}
