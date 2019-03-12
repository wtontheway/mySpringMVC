package lee.iSpring.common.bean;

/**
 * 参数属性
 */
public class PropertiesObject {

	private String mode;
	private String mongoUrl;
	private String redisHost;
	private int redisPort;
	private long redisExpiration;
	private String redisPassword;


	public boolean getTraceMode() {
		return "trace".equals(mode);
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public String getMongoUrl() {
		return mongoUrl;
	}

	public void setMongoUrl(String mongoUrl) {
		this.mongoUrl = mongoUrl;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	public long getRedisExpiration() {
		return redisExpiration;
	}

	public void setRedisExpiration(long redisExpiration) {
		this.redisExpiration = redisExpiration;
	}

	public String getRedisPassword() {
		return redisPassword;
	}

	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}

}
