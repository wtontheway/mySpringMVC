package lee.iSpring.common.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lee.iSpring.common.bean.RestResponse;
import lee.iSpring.common.constant.RequestCacheDataItem;
import lee.iSpring.common.util.ExceptionUtil;
import lee.iSpring.common.util.JSONUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 默认异常处理
 */
public class DefaultExceptionHandler implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
										 Exception ex) {

		ModelAndView mv = new ModelAndView();

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache, must-revalidate");

		RestResponse<String> result = ExceptionUtil.handleException(String.class, ex);

		//定义为响应默认异常，throws出来的都进来这里写mongo，拦截器不处理
		RequestCacheDataItem.RESPONSE_DEFAULT_EXCEPTION.set(true);
		//写入收集日志
		//StatisticsUtil.statisticsDefaultExceptionDataToMongo(request, response, result);

		try {
			response.getWriter().write(JSONUtil.objToJson(result));
		} catch (IOException ignore) {
		}

		return mv;
	}
}
