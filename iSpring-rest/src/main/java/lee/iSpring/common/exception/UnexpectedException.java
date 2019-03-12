package lee.iSpring.common.exception;

import lee.iSpring.common.constant.State;

public class UnexpectedException extends BaseException {

	private static final long serialVersionUID = 1L;

	public UnexpectedException(State state) {
		super(state);
	}

	public UnexpectedException(State state, Object... args) {
		super(state, args);
	}
}
