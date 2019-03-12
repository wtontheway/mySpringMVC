package lee.iSpring.common.util;

import lee.iSpring.common.bean.LogErrorInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import lee.iSpring.common.bean.LogTraceInfo;

/**
 * 日志工具
 */
public class LoggerUtil {

	private static final Logger errorLogger = LogManager.getLogger("iSpring-error");
	private static final Logger tracer = LogManager.getLogger("iSpring-tracer");
	private static final Logger debugger = LogManager.getLogger("iSpring-debugger");

	public static void trace(LogTraceInfo info) {

		tracer.trace(JSONUtil.objToJson(info));
	}

	public static void trace(Marker marker, LogTraceInfo info) {

		tracer.trace(marker, JSONUtil.objToJson(info));
	}

	public static void trace(String msg, Object... params) {

		tracer.trace(msg, params);
	}

	public static void error(LogErrorInfo info, Throwable ex) {

		errorLogger.error(JSONUtil.objToJson(info), ex);
	}

	public static void error(Marker marker, LogErrorInfo info, Throwable ex) {

		errorLogger.error(JSONUtil.objToJson(info), ex);
	}

	public static void error(String msg, Throwable ex) {

		errorLogger.error(msg, ex);
	}

	public static void debug(String msg, Object... params) {
		debugger.debug(msg, params);
	}
}
