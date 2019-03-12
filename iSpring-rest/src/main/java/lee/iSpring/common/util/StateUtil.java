package lee.iSpring.common.util;

import java.text.MessageFormat;

import lee.iSpring.common.bean.StateInfo;
import lee.iSpring.common.constant.State;
import lee.iSpring.common.constant.StateCode;

/**
 * 返回状态工具
 */
public class StateUtil {

	public static StateInfo getState(State state, Object... args) {

		if(state == null) {
			return new StateInfo(StateCode.NULL_STATE_OBJECT.getCode(),
					StateCode.NULL_STATE_OBJECT.getMsg(), "");
		} else {
			return new StateInfo(state.getCode(), state.getMsg(), "", args);
		}
	}

	public static StateInfo getStateByMethodName(String method){
		return new StateInfo(StateCode.METHOD_ERROR.getCode(),
				MessageFormat.format(StateCode.METHOD_ERROR.getMsg(), method), "");
	}

}
