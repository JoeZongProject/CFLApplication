package com.joecorelibrary.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 访问网络的工具包
 * 
 * @author huangyin
 */
public class NetUtil {

	public static final int HTTP_OK = 200;
	public static final int TIME_OUT_DEFAULT = 15 * 1000;
	public static final String ERROR_TAG = "Error:";

	/**
	 * get访问返回字符串
	 */
	public String httpGet(String url) {
		return httpGet(url, TIME_OUT_DEFAULT);
	}

	/**
	 * get访问返回字符串
	 */
	public static String httpGet(String url, int timeout) {
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(timeout);
			int code = conn.getResponseCode();
			if (code == HTTP_OK) {
				return getResponseString(conn.getInputStream());
			} else {
//				LogUtil.e("Net Error:", code + "");
				return ERROR_TAG + code;
			}
		} catch (Exception e) {
//			LogUtil.handleException(e);
			return ERROR_TAG + e.getMessage();
		}
	}

	/**
	 * post访问返回字符串
	 */
	public static String httpPost(String url, Map<String, Object> myParams) {
		return httpPost(url, myParams, TIME_OUT_DEFAULT, null);
	}

	/**
	 * post访问返回字符串
	 */
	public static String httpPost(String url, Map<String, Object> myParams, int timeout) {
		return httpPost(url, myParams, timeout, null);
	}

	/**
	 * post访问返回字符串
	 */
	public static String httpPost(String url, Map<String, Object> myParams, String cookie) {
		return httpPost(url, myParams, TIME_OUT_DEFAULT, cookie);
	}

	/**
	 * post访问返回字符串
	 */
	public static String httpPost(String url, Map<String, Object> myParams, int timeout, String cookie) {
		try {
			byte[] data = getRequestData(myParams, "UTF-8").toString().getBytes();//获得请求体
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			if (cookie != null) {
				conn.setRequestProperty("Cookie", cookie);
			}
			conn.setDoOutput(true);
			conn.getOutputStream().write(data);
			conn.setConnectTimeout(timeout);
			int code = conn.getResponseCode();
			if (code == HTTP_OK) {
				return getResponseString(conn.getInputStream());
			} else {
//				LogUtil.e("Net Error:", code + "");
				return ERROR_TAG + code;
			}
		} catch (Exception e) {
//			LogUtil.handleException(e);
			return ERROR_TAG + e.getMessage();
		}
	}

	/**
	 * json post访问返回字符串
	 */
	public static String jsonPost(String url, String json) throws Exception {
		return jsonPost(url, json, TIME_OUT_DEFAULT, null);
	}

	/**
	 * json post访问返回字符串
	 */
	public static String jsonPost(String url, String json, Map<String, String> heads) throws Exception {
		return jsonPost(url, json, TIME_OUT_DEFAULT, heads);
	}

	/**
	 * json post访问返回字符串
	 */
	public static String jsonPost(String url, String json, int timeout, Map<String, String> heads) throws Exception {
		byte[] data = json.getBytes("utf-8"); // 获得请求体
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setRequestProperty("content-type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		if (heads != null) {
			for (String key : heads.keySet()) {
				conn.setRequestProperty(key, heads.get(key));
			}
		}
		conn.setDoOutput(true);
		conn.setConnectTimeout(timeout);
		conn.setReadTimeout(timeout);
		conn.getOutputStream().write(data);
		int code = conn.getResponseCode();
		if (code == HTTP_OK) {
			return getResponseString(conn.getInputStream());
		} else {
//			LogUtil.e("Net Error:", code + "");
			return ERROR_TAG + code;
		}
	}

	private static String getResponseString(InputStream inputStream) throws IOException {
		BufferedReader re = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		StringBuffer rst = new StringBuffer(100);
		while ((line = re.readLine()) != null) {
			rst.append(line);
		}
		return rst.toString();
	}

	/*
	 * Function : 封装请求体信息 Param : params请求体内容，encode编码格式
	 */
	public static StringBuffer getRequestData(Map<String, Object> params, String encode) throws Exception {
		StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), encode)).append("&");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
		}
		return stringBuffer;
	}
}