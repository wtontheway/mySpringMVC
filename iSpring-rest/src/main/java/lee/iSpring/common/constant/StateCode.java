package lee.iSpring.common.constant;


/**
 * 全局返回状态，取值范围：-1 - 9999
 */
public class StateCode {

	/**
	 * 请求成功。
	 */
	public static final State OK = new State(0, "请求成功。");

	/**
	 * 请求成功。
	 */
	public static final State SUCCESS = new State(0, "请求成功。");

	/**
	 * 失败
	 */
	public static final State FAILURE = new State(1, "操作失败");

	public static final State DATA_ERROR = new State(2,"数据异常，请重试");

	/**
	 * {0}
	 */
	public static final State UNKNOWN_ERROR = new State(-1, "{0}");

	public static final State DATASOURCE_ERROR = new State(1000,"初始化数据源异常:{0}");

	public static final State DATASOURCE_NULL_ERROR = new State(1001,"初始化数据源异常:{0}");
	/**
	 * 缺失参数：{0}。
	 */
	public static final State PARAM_MISS = new State(1002, "请填写：{0}。");

	/**
	 * 参数格式错误，参数：{0}，类型：{1}。
	 */
	public static final State PARAM_FORMAT_ERROR = new State(1003, "参数格式错误，参数：{0}，类型：{1}。");

	/**
	 * 传入的参数有误：{0}。
	 */
	public static final State PARAM_ERROR = new State(1004, "传入的参数有误：{0}。");

	/**
	 * 对象为空(null)。
	 */
	public static final State NULL_STATE_OBJECT = new State(1005, "对象为空(null)。");

	/**
	 * 1005为方法 {0} 执行错误。，
	 */
	public static final State METHOD_ERROR = new State(1006, "方法 {0} 执行错误。");

	public static final State DATABASE_ERROR = new State(1007, "服务器内部错误");

	/**
	 * 2001 为调用外部服务失败！{0},
	 */
	public static final State EXT_SERVICE_ERROR = new State(2001, "调用外部服务失败！{0}");

	public static final State USER_NO_PERMISSIONS= new State(3005, "用户无权限！");

	public static final State USER_PERMISSIONS_FAIL= new State(3006, "用户无{0}权限！");

}
