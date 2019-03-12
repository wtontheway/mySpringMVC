package lee.iSpring.common.interceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lee.iSpring.common.bean.PropertiesObject;
import lee.iSpring.common.exception.BaseException;
import lee.iSpring.common.util.LoggerUtil;
import lee.iSpring.common.util.SpringContextHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import lee.iSpring.common.constant.RequestCacheDataItem;
import lee.iSpring.common.util.JSONUtil;

/**
 * 轨迹拦截器，跟踪显示控制器的参数、返回及异常信息
 */
public class TraceInterceptor extends CustomizableTraceInterceptor {

	private static final long serialVersionUID = 1L;
	private static final Pattern PATTERN = Pattern.compile("\\$\\[\\p{Alpha}+\\]");

	@Override
	protected void writeToLog(Log log, String message, Throwable ex) {

		LoggerUtil.trace(message, RequestCacheDataItem.PATH_INFO.get());
	}

	@Override
	protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {

		PropertiesObject po = SpringContextHolder.getBean(PropertiesObject.class);

		return po.getTraceMode();// 返回 true 则开启
	}

	@Override
	protected String replacePlaceholders(String message, MethodInvocation methodInvocation, Object returnValue,
										 Throwable throwable, long invocationTime) {

		Matcher matcher = PATTERN.matcher(message);
		StringBuffer output = new StringBuffer();

		while (matcher.find()) {
			String match = matcher.group();

			if (PLACEHOLDER_METHOD_NAME.equals(match)) {
				matcher.appendReplacement(output, Matcher.quoteReplacement(methodInvocation.getMethod().getName()));
			} else if (PLACEHOLDER_TARGET_CLASS_NAME.equals(match)) {
				String className = getClassForLogging(methodInvocation.getThis()).getName();
				matcher.appendReplacement(output, Matcher.quoteReplacement(className));
			} else if (PLACEHOLDER_TARGET_CLASS_SHORT_NAME.equals(match)) {
				String shortName = ClassUtils.getShortName(getClassForLogging(methodInvocation.getThis()));
				matcher.appendReplacement(output, Matcher.quoteReplacement(shortName));
			} else if (PLACEHOLDER_ARGUMENTS.equals(match)) {
				Object arg = null;
				Object[] arguments = methodInvocation.getArguments();
				if (arguments != null && arguments.length == 1) {
					arg = arguments[0];// 只有一个参数的话，只选择第一个
				} else {
					arg = arguments;
				}
				matcher.appendReplacement(output, JSONUtil.objToJson(arg));// 改为打印json
			} else if (PLACEHOLDER_ARGUMENT_TYPES.equals(match)) {
				appendArgumentTypes(methodInvocation, matcher, output);
			} else if (PLACEHOLDER_RETURN_VALUE.equals(match)) {
				appendReturnValue(methodInvocation, matcher, output, returnValue);
			} else if (throwable != null && PLACEHOLDER_EXCEPTION.equals(match)) {
				if (throwable instanceof BaseException) {
					BaseException ex = (BaseException) throwable;

					matcher.appendReplacement(output,
							throwable.getClass().getSimpleName() + " - " + ex.getFormattedMsg());
				} else {
					matcher.appendReplacement(output, Matcher.quoteReplacement(throwable.toString()));
				}
			} else if (PLACEHOLDER_INVOCATION_TIME.equals(match)) {
				matcher.appendReplacement(output, Long.toString(invocationTime));
			} else {
				throw new IllegalArgumentException("Unknown placeholder [" + match + "]");
			}
		}
		matcher.appendTail(output);

		return output.toString();
	}

	private void appendReturnValue(MethodInvocation methodInvocation, Matcher matcher, StringBuffer output,
								   Object returnValue) {

		if (methodInvocation.getMethod().getReturnType() == void.class) {
			matcher.appendReplacement(output, "void");
		} else if (returnValue == null) {
			matcher.appendReplacement(output, "null");
		} else {
			matcher.appendReplacement(output, Matcher.quoteReplacement(returnValue.toString()));
		}
	}

	private void appendArgumentTypes(MethodInvocation methodInvocation, Matcher matcher, StringBuffer output) {

		Class<?>[] argumentTypes = methodInvocation.getMethod().getParameterTypes();
		String[] argumentTypeShortNames = new String[argumentTypes.length];

		for (int i = 0; i < argumentTypeShortNames.length; i++) {
			argumentTypeShortNames[i] = ClassUtils.getShortName(argumentTypes[i]);
		}
		matcher.appendReplacement(output,
				Matcher.quoteReplacement(StringUtils.arrayToCommaDelimitedString(argumentTypeShortNames)));
	}
}
