package lee.iSpring.common.util;

import lee.iSpring.common.bean.LogErrorInfo;
import lee.iSpring.common.bean.LogTraceInfo;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import lee.iSpring.common.constant.StateCode;
import lee.iSpring.common.exception.ExtServiceException;

import java.util.Map;

/**
 * 外部 REST 服务工具
 */
public class RestServiceUtil {

	private final static String ENCODING = "UTF-8";

	/**
	 * POST 数据
	 */
	public static String post(String url, String jsonData) throws Exception {

		return post(url, jsonData, "", "");

	}

	/**
	 * POST 数据
	 */
	public static String post(String url, String jsonData, String user, String password) throws Exception {

		LogErrorInfo errorInfo = new LogErrorInfo();

		errorInfo.setActionName(url);
		errorInfo.setMethodName("POST");
		errorInfo.setParams("{\"data\":" + jsonData + ",\"user\":\"" + user + "\",\"password\":\"" + password + "\"}");
		errorInfo.setSource("RestServiceUtil");

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost method = new HttpPost(url);
		//默认添加网关key

		try {
			if (!user.isEmpty())
				method.setHeader("UserId", user);
			if (!password.isEmpty())
				method.setHeader("PWD", password);

			StringEntity entity = new StringEntity(jsonData, ENCODING);

			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			method.setEntity(entity);

			long runtime = System.currentTimeMillis();

			CloseableHttpResponse response = httpClient.execute(method);
			runtime = System.currentTimeMillis() - runtime;
			try {
				// 返回结果
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String result = EntityUtils.toString(response.getEntity(), ENCODING);
					LogTraceInfo traceInfo = new LogTraceInfo();

					traceInfo.copyFrom(errorInfo);
					traceInfo.setRuntime(runtime);
					traceInfo.setResult(result);
					LoggerUtil.trace(traceInfo);

					return result;
				} else {
					String result = response.getStatusLine().toString();
					ExtServiceException ex = new ExtServiceException(StateCode.EXT_SERVICE_ERROR, result);

					errorInfo.setResult(result);
					LoggerUtil.error(errorInfo, ex);

					throw ex;
				}
			} finally {
				response.close();
			}
		} catch (Exception ex) {
			if (ex instanceof ExtServiceException) {
				throw ex;
			} else {
				String result = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
				ExtServiceException e = new ExtServiceException(StateCode.EXT_SERVICE_ERROR, result);

				e.setStackTrace(ex.getStackTrace());
				errorInfo.setResult(result);
				LoggerUtil.error(errorInfo, e);

				throw e;
			}
		}
	}

	/**
	 * GET 数据
	 */
	public static String get(String url, String jsonData) throws Exception {

		return get(url, jsonData, "", "");
	}

	/**
	 * GET 数据
	 */
	public static String get(String url, String jsonData, String user, String password) throws Exception {

		LogErrorInfo errorInfo = new LogErrorInfo();

		errorInfo.setActionName(url);
		errorInfo.setMethodName("POST");
		errorInfo.setParams("{\"data\":" + jsonData + ",\"user\":\"" + user + "\",\"password\":\"" + password + "\"}");
		errorInfo.setSource("RestServiceUtil");

		try {

			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet method = new HttpGet(url);

			if (!user.isEmpty())
				method.setHeader("UserId", user);
			if (!password.isEmpty())
				method.setHeader("PWD", password);

			long runtime = System.currentTimeMillis();

			CloseableHttpResponse response = httpClient.execute(method);
			runtime = System.currentTimeMillis() - runtime;
			try {
				// 返回结果
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String result = EntityUtils.toString(response.getEntity(), ENCODING);
					LogTraceInfo traceInfo = new LogTraceInfo();

					traceInfo.copyFrom(errorInfo);
					traceInfo.setResult(result);
					traceInfo.setRuntime(runtime);
					LoggerUtil.trace(traceInfo);

					return result;
				} else {
					String result = response.getStatusLine().toString();
					ExtServiceException ex = new ExtServiceException(StateCode.EXT_SERVICE_ERROR, result);

					errorInfo.setResult(result);
					LoggerUtil.error(errorInfo, ex);

					throw ex;
				}
			} finally {
				response.close();
			}
		} catch (Exception ex) {
			if (ex instanceof ExtServiceException) {
				throw ex;
			} else {
				String result = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
				ExtServiceException e = new ExtServiceException(StateCode.EXT_SERVICE_ERROR, result);

				e.setStackTrace(ex.getStackTrace());
				errorInfo.setResult(result);
				LoggerUtil.error(errorInfo, e);

				throw e;
			}
		}
	}

	/**
	 * GET 数据
	 */
	public static String get(String url, Map<String, Object> headers) throws Exception {

		LogErrorInfo errorInfo = new LogErrorInfo();

		errorInfo.setActionName(url);
		errorInfo.setMethodName("POST");
		errorInfo.setParams("{\"url\":" + url + "\"}");
		errorInfo.setSource("RestServiceUtil");

		try {

			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet method = new HttpGet(url);

			for(String key : headers.keySet()){
				method.setHeader(key, headers.get(key).toString());
			}

			long runtime = System.currentTimeMillis();

			CloseableHttpResponse response = httpClient.execute(method);
			runtime = System.currentTimeMillis() - runtime;
			try {
				// 返回结果
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String result = EntityUtils.toString(response.getEntity(), ENCODING);
					LogTraceInfo traceInfo = new LogTraceInfo();

					traceInfo.copyFrom(errorInfo);
					traceInfo.setResult(result);
					traceInfo.setRuntime(runtime);
					LoggerUtil.trace(traceInfo);

					return result;
				} else {
					String result = response.getStatusLine().toString();
					ExtServiceException ex = new ExtServiceException(StateCode.EXT_SERVICE_ERROR, result);

					errorInfo.setResult(result);
					LoggerUtil.error(errorInfo, ex);

					throw ex;
				}
			} finally {
				response.close();
			}
		} catch (Exception ex) {
			if (ex instanceof ExtServiceException) {
				throw ex;
			} else {
				String result = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
				ExtServiceException e = new ExtServiceException(StateCode.EXT_SERVICE_ERROR, result);

				e.setStackTrace(ex.getStackTrace());
				errorInfo.setResult(result);
				LoggerUtil.error(errorInfo, e);

				throw e;
			}
		}
	}

}
