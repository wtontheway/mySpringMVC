package lee.iSpring.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具
 */
public class StringUtil {

	/**
	 * 判断数字是否存在逗号分隔的字符串里面
	 */
	public static boolean in(int num, String numStr) {

		return contains(numStr, num, ",");
	}

	/**
	 * 判断字符串是否存在
	 */
	public static boolean in(String str, String... targetArray) {

		for (String target : targetArray)
			if (target.equals(str))
				return true;

		return false;
	}

	/**
	 *判断字符串为空
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().equals("");
	}

	/**
	 *判断字符串不为空
	 */
	public static boolean isNotBlank(String str) {
		return str != null && !str.trim().equals("");
	}

	/**
	 * 字符串转换成十六进制字符串
	 */
	public static String str2HexStr(String str) {

		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;

		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}

		return sb.toString();
	}

	/**
	 * 十六进制转换字符串
	 */
	public static String hexStr2Str(String hexStr) {

		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}

		return new String(bytes);
	}

	/**
	 * bytes转换成十六进制字符串
	 */
	public static String byte2HexStr(byte[] b) {

		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
			// if (n<b.length-1) hs=hs+":";
		}

		return hs.toUpperCase();
	}

	private static byte uniteBytes(String src0, String src1) {

		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);

		return ret;
	}

	/**
	 * bytes转换成十六进制字符串
	 */
	public static byte[] hexStr2Bytes(String src) {

		int m = 0, n = 0;
		int l = src.length() / 2;
		byte[] ret = new byte[l];

		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
		}

		return ret;
	}

	/**
	 * String的字符串转换成unicode的String
	 */
	public static String str2Unicode(String strText) throws Exception {

		char c;
		String strRet = "";
		int intAsc;
		String strHex;

		for (int i = 0; i < strText.length(); i++) {
			c = strText.charAt(i);
			intAsc = (int) c;
			strHex = Integer.toHexString(intAsc);
			if (intAsc > 128) {
				strRet += "//u" + strHex;
			} else {
				// 低位在前面补00
				strRet += "//u00" + strHex;
			}
		}

		return strRet;
	}

	/**
	 * unicode的String转换成String的字符串
	 */
	public static String unicode2Str(String hex) {

		int t = hex.length() / 6;
		StringBuilder str = new StringBuilder();

		for (int i = 0; i < t; i++) {
			String s = hex.substring(i * 6, (i + 1) * 6);
			// 高位需要补上00再转
			String s1 = s.substring(2, 4) + "00";
			// 低位直接转
			String s2 = s.substring(4);
			// 将16进制的string转为int
			int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
			// 将int转换为字符
			char[] chars = Character.toChars(n);
			str.append(new String(chars));
		}

		return str.toString();
	}

	/**
	 * 拼接args为字符串
	 */
	public static String append(Object... args) {

		StringBuilder sb = new StringBuilder();

		for (Object arg : args) {
			sb.append(arg);
		}

		return sb.toString();
	}

	/**
	 * 数字转字符
	 */
	public static String intToString(Integer arge) {

		String str = "";

		if (arge != null && arge.intValue() > 0) {
			str = arge.toString();
		}

		return str;
	}

	/**
	 * 类似于oracle中的nvl函数，如果value不为null则返回value, 为null则返回defaultValue
	 */
	public static String nvl(String value, String defaultValue) {

		return value == null ? defaultValue : value;
	}

	public static String evl(String value, String defaultValue) {
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * 返回 sourceStr.contains(pad + targetStr + pad)
	 */
	public static boolean contains(String sourceStr, Object targetObj, String pad) {

		return padIfNeed(sourceStr, pad).contains(padIfNeed(targetObj.toString(), pad));
	}

	/**
	 * 返回 sourceStr.contains(pad + targetStr + pad)
	 */
	public static <T extends Number> boolean contains(String sourceStr, T targetStr, String pad) {

		return padIfNeed(sourceStr, pad).contains(append(pad, targetStr, pad));
	}

	/**
	 * 如果str不是以pad开始，则在开始处加上pad，如果不是以pad结束，则在结尾加上pad
	 */
	private static String padIfNeed(String str, String pad) {

		StringBuilder sb = new StringBuilder(str.length() + pad.length() * 2);

		if (!str.startsWith(pad)) {
			sb.append(pad);
		}
		sb.append(str);
		if (!str.endsWith(pad)) {
			sb.append(pad);
		}

		return sb.toString();
	}

	/**
	 * 将字符串的首字母改成大写
	 */
	public static String firstCharUp(String str) {

		if (StringUtils.isBlank(str)) {
			return str;
		}

		StringBuilder sb = new StringBuilder();
		char firstChar = str.charAt(0);

		if (firstChar >= 'a' && firstChar <= 'z') {
			firstChar = (char) (firstChar - ('a' - 'A'));
		}
		sb.append(firstChar);
		for (int i = 1, length = str.length(); i < length; i++) {
			sb.append(str.charAt(i));
		}

		return sb.toString();
	}

	/**
	 * 将字符串的首字母改成小写
	 */
	public static String firstCharDown(String str) {

		if (StringUtils.isBlank(str)) {
			return str;
		}

		StringBuilder sb = new StringBuilder();
		char firstChar = str.charAt(0);

		if (firstChar >= 'A' && firstChar <= 'Z') {
			firstChar = (char) (firstChar + ('a' - 'A'));
		}
		sb.append(firstChar);
		for (int i = 1, length = str.length(); i < length; i++) {
			sb.append(str.charAt(i));
		}

		return sb.toString();
	}

	/**
	 * 将builder内的from字符串替换为to字符串
	 */
	public static void replaceAll(StringBuilder builder, String from, String to) {

		int index = builder.indexOf(from);
		while (index != -1) {
			builder.replace(index, index + from.length(), to);
			index += to.length(); // index移到替换的字符串之后
			index = builder.indexOf(from, index);
		}
	}

}
