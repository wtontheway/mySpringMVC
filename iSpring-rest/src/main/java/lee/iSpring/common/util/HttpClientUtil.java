package lee.iSpring.common.util;

import lee.iSpring.common.bean.LogErrorInfo;
import lee.iSpring.common.bean.LogTraceInfo;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

    private final static String ENCODING = "UTF-8";

    public static String doPost(String url,String params) {

        Header[] headers = new Header[3];
        headers[0] = new BasicHeader(HttpHeaders.ACCEPT,"application/json");
        headers[1] = new BasicHeader(HttpHeaders.ACCEPT_CHARSET, "utf-8");
        headers[2] = new BasicHeader(HttpHeaders.CONTENT_TYPE,
                "application/json; charset=UTF-8");
        return doPost(url, params, ENCODING, headers);
    }

    public static CloseableHttpClient httpClient = null;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(50);
        RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
        httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(config).build();
    }

    public static String doPost(String url, String param, String charset, Header[] headers) {

        LogTraceInfo traceInfo = new LogTraceInfo();
        LogErrorInfo errorInfo = new LogErrorInfo();
        errorInfo.setActionName(url);
        errorInfo.setMethodName("POST");
        errorInfo.setParams(param);

        if(StringUtil.isBlank(url)) {
            return null;
        } else {
            try {
                HttpPost e = new HttpPost(url);
                if(StringUtil.isNotBlank(param)) {
                    StringEntity response = new StringEntity(param, charset);
                    e.setEntity(response);
                }

                e.setHeaders(headers);
                long runtime = System.currentTimeMillis();
                CloseableHttpResponse response1 = httpClient.execute(e);
                runtime = System.currentTimeMillis() - runtime;

                int statusCode = response1.getStatusLine().getStatusCode();
                if(statusCode != 200) {
                    e.abort();
                    errorInfo.setResult(response1.toString());
                    throw new RuntimeException("HttpClient,error status code :" + statusCode);
                } else {
                    HttpEntity entity = response1.getEntity();
                    String result = null;
                    if(entity != null) {
                        result = EntityUtils.toString(entity, "utf-8");
                    }

                    EntityUtils.consume(entity);
                    response1.close();

                    traceInfo.copyFrom(errorInfo);
                    traceInfo.setRuntime(runtime);
                    LoggerUtil.trace(traceInfo);
                    return result;
                }
            } catch (Exception var9) {
                LoggerUtil.error(errorInfo, var9);
                throw new RuntimeException(var9.getMessage(), var9);
            }
        }
    }
}
