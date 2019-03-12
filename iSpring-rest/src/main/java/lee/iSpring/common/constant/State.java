package lee.iSpring.common.constant;

import java.text.MessageFormat;

/**
 * 返回状态信息
 */
public class State {

	private int code;
	private String msg; // 参数格式：{0}{1}{2}

	public State(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}

	public String getMsg(Object... args) {
		return MessageFormat.format(msg, args);
	}
}
