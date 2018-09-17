package com.baymin.springboot.pay.wechat.param;

import com.baymin.springboot.pay.wechat.service.IServiceRequest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpsRequest implements IServiceRequest {

	private static Logger log = LoggerFactory.getLogger(HttpsRequest.class);

	public interface ResultListener {
		public void onConnectionPoolTimeoutError();
	}

	private boolean hasInit = false;

	// 连接超时时间，默认10秒
	private int socketTimeout = 10000;
	// 传输超时时间，默认30秒
	private int connectTimeout = 30000;
	// 请求器的配置
	private RequestConfig requestConfig;
	// HTTP请求器
	private CloseableHttpClient httpClient;

	public HttpsRequest() throws Exception {
		init();
	}

	private void init() throws Exception {
		try {
			trustAllHttpsCertificates();
		} catch (Exception e) {
			e.printStackTrace();
		}
		httpClient = HttpClients.createDefault();
		// 根据默认超时限制初始化requestConfig
		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

		hasInit = true;
	}
	
	public String sendPost(String url, Object xmlObj) throws Exception {
		if (!hasInit) {
			init();
		}

		String result = null;
		HttpPost httpPost = new HttpPost(url);
		// 解决XStream对出现双下划线的bug
		XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		// 将要提交给API的数据对象转换成XML格式数据Post给API
		String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
		Util.log(postDataXML);

		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);

		// 设置请求器的配置
		httpPost.setConfig(requestConfig);

		Util.log("executing request:" + httpPost.getRequestLine());
		Util.log("request data:" + postDataXML);

		try {
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} catch (ConnectionPoolTimeoutException e) {
			log.error("http get throw ConnectionPoolTimeoutException(wait time out)");
		} catch (ConnectTimeoutException e) {
			log.error("http get throw ConnectTimeoutException");
		} catch (SocketTimeoutException e) {
			log.error("http get throw SocketTimeoutException");
		} catch (Exception e) {
			log.error("http get throw Exception");
		} finally {
			httpPost.abort();
		}

		return result;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
		resetRequestConfig();
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		resetRequestConfig();
	}

	private void resetRequestConfig() {
		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
	}

	/**
	 * 设置更高级的请求器配置
	 */
	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}
	
	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			return;
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			return;
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
	
}