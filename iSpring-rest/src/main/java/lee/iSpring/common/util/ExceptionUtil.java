package lee.iSpring.common.util;

import lee.iSpring.common.bean.RestResponse;
import lee.iSpring.common.exception.BaseException;
import lee.iSpring.common.exception.BusinessException;
import lee.iSpring.common.exception.ParamException;

/**
 * 异常工具类
 */
public class ExceptionUtil {

	/**
	 * 处理异常
	 */
	public static <T> RestResponse<T> handleException(Class<T> c, Exception e) {

		RestResponse<T> restResponse = new RestResponse<T>();

		restResponse.setMsg(e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());

		if (e instanceof BusinessException) {
			restResponse.setArgs(((BusinessException) e).getArgs());
			restResponse.setDisplayLevel(RestResponse.BIZ_ERROR);
			restResponse.setStatusCode(((BusinessException) e).getStatusCode());
		} else {
			if (e instanceof ParamException) {
				restResponse.setArgs(((ParamException) e).getArgs());
				restResponse.setDisplayLevel(RestResponse.PARAM_ERROR);
				restResponse.setStatusCode(((ParamException) e).getStatusCode());
			} else {
				restResponse.setDisplayLevel(RestResponse.SYSTEM_ERROR);
				if (e instanceof BaseException) {
					restResponse.setArgs(((BaseException) e).getArgs());
					restResponse.setStatusCode(((BaseException) e).getStatusCode());
				} else {
					restResponse.setStatusCode(RestResponse.UNKNONW_STATUS);
				}
			}
			if (!(e instanceof BaseException))
				LoggerUtil.error(restResponse.getMsg(), e);
		}

		return restResponse;
	}
}
