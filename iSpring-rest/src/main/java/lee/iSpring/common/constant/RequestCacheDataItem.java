package lee.iSpring.common.constant;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import lee.iSpring.common.bean.SysTrace;

/**
 * 请求数据, 每个请求各自保存一份、互相独立
 */
public class RequestCacheDataItem<T> {

	/**
	 * key生成器
	 */
	private static AtomicInteger keyGenerator = new AtomicInteger(Short.MIN_VALUE);

	private RequestCacheDataItem() {
		this.key = (short) keyGenerator.getAndIncrement();
	}

	private static <T> RequestCacheDataItem<T> newInstance() {
		return new RequestCacheDataItem<T>();
	}

	/**
	 * key, 必须唯一
	 */
	private short key;

	public short getKey() {
		return key;
	}

	/**
	 * 获取值
	 */
	public T get() {
		T value = RequestCacheData.getInstance().get(this);
		return value;
	}

	/**
	 * 删除值
	 */
	public void remove() {
		RequestCacheData.getInstance().remove(this);
	}

	/**
	 * 设置值
	 */
	public void set(T value) {
		RequestCacheData.getInstance().put(this, value);
	}

	// 本次请求信息
	public static final RequestCacheDataItem<String> PATH_INFO = newInstance();
	public static final RequestCacheDataItem<Stack<SysTrace>> CURRENT_THREAD_VAR = newInstance();
	public static final RequestCacheDataItem<String> CURRENT_REFNO = newInstance();
	public static final RequestCacheDataItem<String> CLIENT_IP = newInstance();
	public static final RequestCacheDataItem<String> REQUEST_URL = newInstance();
	public static final RequestCacheDataItem<Boolean> RESPONSE_DEFAULT_EXCEPTION = newInstance();
	public static final RequestCacheDataItem<String> DB_CONN_ID = newInstance();
	public static final RequestCacheDataItem<String> MAIN_ID = newInstance();
}
