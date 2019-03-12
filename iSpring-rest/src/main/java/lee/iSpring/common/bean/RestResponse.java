package lee.iSpring.common.bean;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lee.iSpring.common.util.JSONUtil;

public class RestResponse<T> {

	public static final int UNKNONW_STATUS = -1;
	public static final int NORMAL = 0;
	public static final int SUCCESS = 0;
	public static final int SYSTEM_ERROR = 1;
	public static final int PARAM_ERROR = 2;
	public static final int BIZ_ERROR = 3;

	private List<String> args = new ArrayList<String>();
	private int displayLevel = 0;
	private String msg = "";
	private T result = null;
	private int statusCode = 0;

	/*
	 * 返回一个新的实例
	 */
	public static <T> RestResponse<T> newInstance() {

		return new RestResponse<T>();
	}

//	public List<String> getArgs() {
//
//		return this.args;
//	}

	public void setArgs(List<Object> value) {

		this.args.clear();
		if (value != null) {
			for (Object o : value) {
				this.args.add(o.toString());
			}
		}
	}

//	public int getDisplayLevel() {
//
//		return this.displayLevel;
//	}

	public void setDisplayLevel(int value) {

		this.displayLevel = value;
	}



//	public String getFormattedMsg() {
//
//		if (this.args != null) {
//			return MessageFormat.format(this.msg, this.args.toArray());
//		} else {
//			return this.msg;
//		}
//	}

	public String getMsg() {

		if (this.args != null) {
			return MessageFormat.format(this.msg, this.args.toArray());
		} else {
			return this.msg;
		}
	}

	public void setMsg(String msg) {

		this.msg = msg;
	}

	public int getStatusCode() {

		return statusCode;
	}

	public void setStatusCode(int statusCode) {

		this.statusCode = statusCode;
	}

	public T getResult() {

		return result;
	}

	public void setResult(T result) {

		this.result = result;
	}

	public void setStatusCodeAndMsg(StateInfo stateInfo) {

		setStatusCode(stateInfo.getCode());
		setMsg(stateInfo.getMsg());
		setArgs(stateInfo.getArgsList());
	}

	@Override
	public String toString() {
		return JSONUtil.objToJson(this);
	}
}
