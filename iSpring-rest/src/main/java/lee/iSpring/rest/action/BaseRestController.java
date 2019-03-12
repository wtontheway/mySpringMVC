package lee.iSpring.rest.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import lee.iSpring.common.bean.Request;
import lee.iSpring.common.bean.RestResponse;
import lee.iSpring.common.bean.StateInfo;
import lee.iSpring.common.constant.State;
import lee.iSpring.common.exception.ParamException;
import lee.iSpring.common.util.ExceptionUtil;
import lee.iSpring.common.constant.StateCode;
import lee.iSpring.common.util.StateUtil;

/**
 * REST 控制器基类
 */
public class  BaseRestController {

	/**
	 * 处理异常
	 */
	protected <T> RestResponse<T> handleException(Class<T> c, Exception e) {

		return ExceptionUtil.handleException(c, e);
	}

	/**
	 * 返回失败
	 */
	protected <T> RestResponse<T> failure(State state, Object... args) {

		RestResponse<T> restResponse = new RestResponse<T>();
		StateInfo stateInfo = StateUtil.getState(state, args);

		//restResponse.setArgs(stateInfo.getArgsList());
//		if (stateInfo.getCode() == StateCode.UNKNOWN_ERROR.getCode()) {
//			restResponse.setDisplayLevel(RestResponse.UNKNONW_STATUS);
//		} else {
//			restResponse.setDisplayLevel(RestResponse.BIZ_ERROR);
//		}
		restResponse.setMsg(stateInfo.getMsg());
		restResponse.setStatusCode(stateInfo.getCode());

		return restResponse;
	}

	/**
	 * 返回失败
	 */
	protected <T> RestResponse<T> failure(State state) {

		Object[] args = null;
		return failure(state, args);
	}

	/**
	 * 返回失败
	 */
	protected <T> RestResponse<T> defaultFailure(Object... args) {

		return failure(StateCode.UNKNOWN_ERROR, args);
	}

	/**
	 * 返回成功
	 */
	protected <T> RestResponse<T> success(T result) {

		RestResponse<T> restResponse = RestResponse.newInstance();
		restResponse.setMsg(StateCode.SUCCESS.getMsg());
		restResponse.setResult(result);
		restResponse.setStatusCode(StateCode.SUCCESS.getCode());

		return restResponse;
	}

	/**
	 * 取得提交数据体
	 */
	protected <T> T getData(Request request, Class<T> clazz) throws Exception {

		T result = request.getData(clazz, false);

		if (result == null)
			throw new ParamException(StateCode.PARAM_MISS, "data");

		return result;
	}

}
