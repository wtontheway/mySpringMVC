package lee.iSpring.common.exception;

import java.util.ArrayList;
import java.util.List;

import lee.iSpring.common.constant.State;
import lee.iSpring.common.constant.StateCode;

/*
 * 自定义异常
 */
public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;
	private State state = null;
	private List<Object> args = null;

	public List<Object> getArgs() {
		return this.args;
	}

	public State getState() {
		return this.state;
	}

	public int getStatusCode() {
		if (this.state == null) {
			return StateCode.NULL_STATE_OBJECT.getCode();
		} else {
			return this.state.getCode();
		}
	}

	public String getMessage() {
		if (this.state == null) {
			return super.getMessage();
		} else {
			return state.getMsg();
		}
	}

	public String getFormattedMsg() {
		if (this.state != null && this.args != null) {
			return state.getMsg(args.toArray());
		} else {
			return getMessage();
		}
	}

	public BaseException(State state) {
		super();
		this.state = state;
	}

	public BaseException(State state, Object... args) {
		super();
		this.state = state;
		this.args = new ArrayList<Object>();
		for (Object o : args) {
			this.args.add(o);
		}
	}
}
