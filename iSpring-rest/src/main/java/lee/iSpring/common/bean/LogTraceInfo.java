package lee.iSpring.common.bean;


/**
 * trace级别日志内容
 */
public class LogTraceInfo extends LogErrorInfo {

	private long runtime;

	public long getRuntime() {
		return runtime;
	}

	public void setRuntime(long runtime) {
		this.runtime = runtime;
	}

	public void copyFrom(LogTraceInfo source) {
		super.copyFrom(source);
		setRuntime(source.getRuntime());
	}
}
