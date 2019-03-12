package lee.iSpring.common.bean;

import java.util.ArrayList;
import java.util.List;

import lee.iSpring.common.constant.State;

/**
 * 返回状态信息类
 */
public class StateInfo extends State {

	private Object[] args;
	private String method;

	public StateInfo(int code, String msg, String method, Object... args) {

		super(code, msg);
		this.method = method;
		this.args = args;
	}

	public StateInfo(State state, String method, Object... args) {

		super(state.getCode(), state.getMsg());
		this.method = method;
		this.args = args;
	}

	public Object[] getArgs() {

		return this.args;
	}

	public List<Object> getArgsList() {

		if (args.length == 0) {
			return null;
		} else {
			ArrayList<Object> result = new ArrayList<Object>();
			for (Object o : args) {
				result.add(o);
			}

			return result;
		}
	}

	public String getMethod() {

		return this.method;
	}
}
