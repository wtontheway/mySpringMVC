package lee.iSpring.common.bean;

/**
 * 错误日志内容
 */
public class LogErrorInfo {

	private String methodName;
	private String actionName;
	private String params;
	private String result;
	private String source;


	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void copyFrom(LogErrorInfo source) {
		setActionName(source.getActionName());
		setMethodName(source.getMethodName());
		setParams(source.getParams());
		setResult(source.getResult());
		setSource(source.getSource());
	}
}
