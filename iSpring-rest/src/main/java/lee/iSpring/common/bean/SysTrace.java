package lee.iSpring.common.bean;

/**
 * 跟踪数据
 */
public class SysTrace {

	private String guid;
	private String methodName;
	private long startTime;

	public SysTrace(String guid, String methodName) {

		this.guid = guid;
		this.methodName = methodName;
		startTime = System.currentTimeMillis();
	}

	public String getGuid() {
		return guid;
	}

	/**
	 * 获取运行时间，精确到毫秒
	 */
	public long getRunTime() {
		return System.currentTimeMillis() - startTime;
	}

	public String getMethodName() {
		return methodName;
	}

	public long getStartTime() {
		return startTime;
	}
}
