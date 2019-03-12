package lee.iSpring.common.exception;

import lee.iSpring.common.constant.State;

/**
 * 外部服务异常
 */
public class ExtServiceException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ExtServiceException(State state) {
		super(state);
	}

	public ExtServiceException(State state, Object... args) {
		super(state, args);
	}
}
