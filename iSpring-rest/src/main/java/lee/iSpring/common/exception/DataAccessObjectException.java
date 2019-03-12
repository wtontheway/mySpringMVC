package lee.iSpring.common.exception;

import lee.iSpring.common.constant.State;

/*
 * DAO 异常
 */
public class DataAccessObjectException extends BaseException {

	private static final long serialVersionUID = 1L;

	public DataAccessObjectException(State state) {
		super(state);
	}

	public DataAccessObjectException(State state, Object... args) {
		super(state, args);
	}
}
