package lee.iSpring.rest.interceptor;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lee.iSpring.common.bean.*;
import lee.iSpring.common.util.*;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lee.iSpring.common.annotation.Check;
import lee.iSpring.common.annotation.CheckRole;
import lee.iSpring.common.constant.RequestCacheDataItem;
import lee.iSpring.common.constant.State;
import lee.iSpring.common.constant.StateCode;
import lee.iSpring.common.exception.ParamException;
import lee.iSpring.common.filter.BodyReaderHttpServletRequestWrapper;
import lee.iSpring.common.constant.RequestCacheData;


/**
 * 全局访问拦截器
 */
public class ControllerInterceptor extends HandlerInterceptorAdapter {


	private PropertiesObject po = SpringContextHolder.getBean(PropertiesObject.class);


	@Override
	public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse response, Object handler)
			throws Exception {

		BodyReaderHttpServletRequestWrapper requestWrapper = servletRequest instanceof BodyReaderHttpServletRequestWrapper
				? (BodyReaderHttpServletRequestWrapper) servletRequest
				: servletRequest instanceof DefaultMultipartHttpServletRequest
				? null
				: new BodyReaderHttpServletRequestWrapper(servletRequest);
		String body = requestWrapper != null ? requestWrapper.getBodyString() : "";

		RequestCacheData.getInstance().setCache();
		RequestCacheDataItem.PATH_INFO.set(servletRequest.getPathInfo());


		Request request = null;

		if (body.equals("")) {
			// 兼容 GET 方法
			request = new Request();
			if(servletRequest instanceof DefaultMultipartHttpServletRequest){
				request.put("token", servletRequest.getParameter("token"));
				request.put("client", servletRequest.getParameter("client"));
			}
		} else {
			request = JSONUtil.jsonToObj(body, Request.class);
		}

		// 判断请求参数是否有外部跟踪号，没有则生成一个
		String guid = request.getString("RefNo", false);
		if (guid == null || "".equals(guid))
			guid = UUID.randomUUID().toString();

		Stack<SysTrace> methodStack = new Stack<SysTrace>();
		RequestCacheDataItem.CURRENT_THREAD_VAR.set(methodStack);

		if (po.getTraceMode()) {
			// 在拦截器里面生成一个调用的栈
			SysTrace trace = new SysTrace(guid, servletRequest.getRequestURI());
			methodStack.push(trace);
		}

		RequestCacheDataItem.CURRENT_REFNO.set(guid);
		RequestCacheDataItem.CLIENT_IP.set(servletRequest.getRemoteAddr());
		RequestCacheDataItem.REQUEST_URL.set(servletRequest.getRequestURI());

		return check(servletRequest, request, response, handler);

	}

	/**
	 * 进行各种验证
	 */
	private boolean check(HttpServletRequest servletRequest, Request request, HttpServletResponse response, Object handler) throws Exception {
		//先检验是否是登录接口
		Check check = getCheck(handler);
		if (check != null && check.passCheck()) {
			return true;
		}

		try {
			if (!checkRole(response, handler, request)) {
				return false;
			}
		} catch (ParamException e) {
			flushRestResponse(response, StateCode.UNKNOWN_ERROR, "验证权限出错, 请检查参数是否正确");
			return false;
		}
		return true;
	}

	/**
	 * 检查权限
	 *
	 * @param request
	 * @param response
	 * @param handler
	 * @throws IOException
	 * @throws ParamException
	 * @throws Exception
	 */
	private boolean checkRole(HttpServletResponse response, Object handler, Request request)
			throws IOException, ParamException {
		CheckRole checkRole = getCheckRole(handler);
		if (checkRole == null) {
			return true;//如果不指定权限标识则认为跳过权限判断, 不需要验证权限
		}
		String role = checkRole.role();
		if (StringUtil.isBlank(role)) {
			flushRestResponse(response, StateCode.USER_NO_PERMISSIONS);
			return false;
		}
		String describe = checkRole.describe();
		if (true) {// 没有权限，不能执行
			flushRestResponse(response, StateCode.USER_PERMISSIONS_FAIL, describe);
			return false;
		}

		return true;
	}

	private CheckRole getCheckRole(Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		if(!method.isAnnotationPresent(CheckRole.class)) {
			return null;
		}
		return method.getAnnotation(CheckRole.class);
	}

	private Check getCheck(Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		if(!method.isAnnotationPresent(Check.class)) {
			return null;
		}
		return method.getAnnotation(Check.class);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		try {
			if (po.getTraceMode()) {

				BodyReaderHttpServletRequestWrapper requestWrapper = request instanceof BodyReaderHttpServletRequestWrapper
						? (BodyReaderHttpServletRequestWrapper) request
						: request instanceof DefaultMultipartHttpServletRequest
						? null
						: new BodyReaderHttpServletRequestWrapper(request);
				Stack<SysTrace> methodStack = RequestCacheDataItem.CURRENT_THREAD_VAR.get();
				SysTrace trace = methodStack.pop();
				Marker marker = MarkerManager.getMarker(trace.getGuid());
				LogTraceInfo info = new LogTraceInfo();

				info.setActionName(RequestCacheDataItem.REQUEST_URL.get());
				info.setMethodName("");
				info.setParams(requestWrapper != null ? requestWrapper.getBodyString():"");
				info.setResult("");
				info.setRuntime(trace.getRunTime());

				LoggerUtil.trace(marker, info);
			}
		} finally {
			// 方法完毕以后清除线程变量
			RequestCacheData.getInstance().removeCache();
		}
	}

	private void flushRestResponse(HttpServletResponse response, State state, Object... args) throws IOException {

		RestResponse<String> restResponse = new RestResponse<String>();

		restResponse.setStatusCodeAndMsg(StateUtil.getState(state, args));
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		OutputStream out = response.getOutputStream();
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "utf-8"));

		// 返回 JSON 格式提示
		pw.println(JSONUtil.objToJson(restResponse));
		pw.flush();
		pw.close();
	}
}