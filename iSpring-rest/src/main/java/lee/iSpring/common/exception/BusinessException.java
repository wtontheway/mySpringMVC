package lee.iSpring.common.exception;

import lee.iSpring.common.constant.State;

/*
 * 业务异常
 */
public class BusinessException extends BaseException {

	private static final long serialVersionUID = 1L;

	public BusinessException(State state) {
		super(state);
	}

	public BusinessException(State state, Object... args) {
		super(state, args);
	}
}
