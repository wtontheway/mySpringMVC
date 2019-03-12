package lee.iSpring.common.exception;

import lee.iSpring.common.constant.State;

/*
 * 参数异常
 */
public class ParamException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ParamException(State state) {
		super(state);
	}

	public ParamException(State state, Object... args) {
		super(state, args);
	}
}
