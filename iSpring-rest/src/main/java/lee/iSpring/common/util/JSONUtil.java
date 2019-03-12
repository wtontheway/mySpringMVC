package lee.iSpring.common.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * JSON 工具
 */
public class JSONUtil {

	public static String objToJson(Object obj) {
		return JSON.toJSONString(obj);
	}

	/**
	 * JSON 转 JAVABEAN, 如果首字母是大写，需要在字段上加上{@literal @JsonProperty注解}
	 */
	public static <T> T jsonToObj(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

	/**
	 * JSON 转泛型对象
	 */
	public static <T> T jsonToGenericObj(String json, TypeReference<T> type) {
		return JSON.parseObject(json, type);
	}

	/**
	 * JSON 字符串转 List 对象
	 */
	public static <T> List<T> jsonToList(String json, Class<T> clazz) {
		return JSON.parseArray(json, clazz);
	}

	/**
	 * JSON 字符串转 Map 对象
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> jsonToMap(String json, Class<K> keyClass, Class<V> elementClass) {
		return (Map<K, V>) JSON.parse(json);
	}

	/**
	 * Map 对象转 JSON 字符串
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String json) {
		return (Map<String, Object>) JSON.parse(json);
	}
}
