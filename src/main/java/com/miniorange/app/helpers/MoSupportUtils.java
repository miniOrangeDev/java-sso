package com.miniorange.app.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ProxySelector;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

public class MoSupportUtils {

	private static Log log = LogFactory.getLog(MoSupportUtils.class);

	public static final String CONTENT_TYPE_JSON = "application/json";
	
	public static String sendPostRequest(String url, String data, String contentType, HashMap<String, String> headers) {
		try {
			log.debug("MoHttpUtils sendPostRequest Sending POST request to " + url + " with payload " + data);
			CloseableHttpClient httpClient = getHttpClient();
			HttpPost postRequest = new HttpPost(url);

			if (headers != null) {
				Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
				if (!headers.isEmpty()) {
					while (iterator.hasNext()) {
						Map.Entry pairs = (Map.Entry) iterator.next();
						postRequest.setHeader(pairs.getKey().toString(), pairs.getValue().toString());
					}
				}
			}
			StringEntity input = new StringEntity(data);
			input.setContentType(contentType);
			postRequest.setEntity(input);

			return executePostRequest(httpClient, postRequest);
            
		} catch (Exception e) {
			return "Failed";
		}
	}

	public static String executePostRequest(CloseableHttpClient httpClient, HttpPost postRequest){
		try{
			HttpResponse response = httpClient.execute(postRequest);
			
			log.debug("Response for HTTP Request: " + response.toString() + " and Status Code: " + response
					.getStatusLine().getStatusCode());

			if (response.getEntity() != null) {				
				log.debug("Response Entity found. Reading Response payload.");
				
				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

				String output, status = "";
				
				while ((output = br.readLine()) != null) {
					status += output;
				}
				
				log.debug("Response payload: " + status);
				httpClient.close();
				return status;
			} else {
					log.debug("Response Entity NOT found. Returning EMPTY string.");
					httpClient.close();
					return StringUtils.EMPTY;
				}
		}catch (Exception e) {
			return "Failed";
		}
	}
	
	private static CloseableHttpClient getHttpClient() throws KeyStoreException, NoSuchAlgorithmException,
			KeyManagementException {
		HttpClientBuilder builder = HttpClientBuilder.create();
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		}).build();
		
		SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier
				.INSTANCE);
		builder.setSSLSocketFactory(sslConnectionFactory);

		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslConnectionFactory)
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.build();

		HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);

		builder.setConnectionManager(ccm);

		SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
		CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner).setConnectionManager(ccm)
				.build();
		return httpclient;
	}
	
	public static String sendGetRequestWithHeaders(String url, HashMap headers) {

		try {
			HttpClient httpClient = getHttpClient();
			HttpGet getRequest = new HttpGet(url);
			log.debug("Sending HTTP Request to URL " + url);
			if (headers != null) {
				Iterator iterator = headers.entrySet().iterator();
				if (!headers.isEmpty()) {
					while (iterator.hasNext()) {
						Map.Entry pairs = (Map.Entry) iterator.next();
						getRequest.setHeader(pairs.getKey().toString(), pairs.getValue().toString());
					}
				}
			}	
			HttpResponse response = httpClient.execute(getRequest);
			log.debug("Response for HTTP Request to URL " + url + " response: " + response.toString()
					+ " \n status code: " + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() >500) {
				log.debug("Response status code out of range 200-300");
				return "Failed";
			} 

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output, responseString = "";
			while ((output = br.readLine()) != null) {
				responseString += output;
			}

			log.debug("Response string HTTP Request to URL " + url + "\n response string: \n " + responseString);
			httpClient.getConnectionManager().shutdown();

			return responseString;

		} catch (Exception e) {
            return "Failed";
		}
	}
	
}

