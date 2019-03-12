package lee.iSpring.common.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lee.iSpring.common.constant.StateCode;
import lee.iSpring.common.exception.ParamException;
import lee.iSpring.common.util.JSONUtil;
import lee.iSpring.common.util.StateUtil;

/**
 * 请求参数
 */
public class Request extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	/**
	 * 获取加密串
	 */
	public String getEncrypt() {

		return "abc5555555";
	}

	/**
	 * 获取请求参数中的对象
	 */
	public Object getObject(String name) throws ParamException {

		return getObject(name, true);
	}

	/**
	 * 获取请求参数中的对象
	 */
	public Object getObject(String name, boolean check) throws ParamException {

		Object object = get(name);

		if (object == null && check) {
			throw new ParamException(StateUtil.getState(StateCode.PARAM_MISS), name);
		}

		return object;
	}

	/**
	 * 获取请求参数中的 yyyy-MM-dd hh:mm:ss 格式字符串
	 */
	public String getDateTimeString(String name) throws ParamException {

		String result = getString(name, true);

		if (!result.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"))
			throw new ParamException(StateUtil.getState(StateCode.PARAM_FORMAT_ERROR, name, "String"));

		return result;
	}

	/**
	 * 获取请求参数中的字符串
	 */
	public String getString(String name) throws ParamException {

		return getString(name, true);
	}

	/**
	 * 获取请求参数中的字符串
	 */
	public String getString(String name, boolean check) throws ParamException {

		Object object = getObject(name, check);

		return object == null ? null : object.toString();
	}

	/**
	 * 获取请求参数中的整数值
	 */
	public Integer getInt(String name) throws ParamException {

		return getInt(name, true);
	}

	/**
	 * 获取请求参数中的整数值
	 */
	public Integer getInt(String name, boolean check) throws ParamException {

		String string = getString(name, check);

		return string == null ? null : Integer.valueOf(string);
	}

	/**
	 * 获取请求参数中的浮点数值
	 */
	public Double getDouble(String name) throws ParamException {
		return getDouble(name, true);
	}

	/**
	 * 获取请求参数中的浮点数值
	 */
	public Double getDouble(String name, boolean check) throws ParamException {

		String string = getString(name, check);

		return string == null ? null : Double.valueOf(string);
	}

	/**
	 * 获取请求参数中的长整型数值
	 */
	public Long getLong(String name) throws ParamException {

		return getLong(name, true);
	}

	/**
	 * 获取请求参数中的长整型数值
	 */
	public Long getLong(String name, boolean check) throws ParamException {

		String string = getString(name, check);

		return string == null ? null : Long.valueOf(string);
	}

	/**
	 * 获取请求参数中的布尔值
	 */
	public Boolean getBoolean(String name) throws ParamException {

		return getBoolean(name, true);
	}

	/**
	 * 获取请求参数中的布尔值
	 */
	public Boolean getBoolean(String name, boolean check) throws ParamException {

		String string = getString(name, check);

		return string == null ? null : Boolean.valueOf(string);
	}

	/**
	 * 获取请求参数中的 Map
	 */
	public <T> Map<String, T> getMap(String name, Class<T> clazz) throws ParamException {

		return getMap(name, clazz, true);
	}

	/**
	 * 获取请求参数中的 Map
	 */
	public <T> Map<String, T> getMap(String name, Class<T> clazz, boolean check) throws ParamException {

		Object o = this.getObject(name, check);

		if (o == null)
			return null;

		try {
			return JSONUtil.jsonToMap(JSONUtil.objToJson(o), String.class, clazz);
		} catch (Exception e) {
			throw new ParamException(StateUtil.getState(StateCode.PARAM_FORMAT_ERROR, name,
					"Map<String, " + clazz.getSimpleName() + ">"));
		}
	}

	/**
	 * 获取请求参数中的 指定类型对象
	 */
	public <T> T getType(String name, Class<T> clazz) throws ParamException {

		return getType(name, clazz, true);
	}

	/**
	 * 获取请求参数中的 指定类型对象
	 */
	public <T> T getType(String name, Class<T> clazz, boolean check) throws ParamException {

		Object o = this.getObject(name, check);

		if (o == null)
			return null;

		try {
			return JSONUtil.jsonToObj(JSONUtil.objToJson(o), clazz);
		} catch (Exception e) {
			throw new ParamException(StateUtil.getState(StateCode.PARAM_FORMAT_ERROR, name, clazz.getSimpleName()));
		}
	}

	/**
	 * 获取请求参数中的 List
	 */
	public <T> List<T> getList(String name, Class<T> clazz) throws ParamException {

		return getList(name, clazz, true);
	}

	/**
	 * 获取请求参数中的 List
	 */
	public <T> List<T> getList(String name, Class<T> clazz, boolean check) throws ParamException {

		Object o = this.getObject(name, check);

		if (o == null)
			return null;

		try {
			return JSONUtil.jsonToList(JSONUtil.objToJson(o), clazz);
		} catch (Exception e) {
			throw new ParamException(
					StateUtil.getState(StateCode.PARAM_FORMAT_ERROR, name, "List<" + clazz.getSimpleName() + ">"));
		}
	}


	/**
	 * 获取请求参数中的 Hash值
	 */
	public String getHash() throws ParamException {

		return getHash(true);
	}

	/**
	 * 获取请求参数中的 Hash值
	 */
	public String getHash(boolean check) throws ParamException {

		return getString("hash", check);
	}

	/**
	 * 获取请求参数中的 Data 对象
	 */
	public <T> T getData(Class<T> clazz) throws ParamException {

		return getData(clazz, true);
	}

	/**
	 * 获取请求参数中的 Data 对象
	 */
	public <T> T getData(Class<T> clazz, boolean check) throws ParamException {

		return getType("data", clazz, check);
	}
}
