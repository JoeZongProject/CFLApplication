package com.joecorelibrary.util.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Http数据
 *
 * @author yujinghong
 */
public class HttpData {
    private static int uniqueId = 1;
    private final String id = String.valueOf(uniqueId ++);
    private HttpMethod httpMethod = HttpMethod.GET;
    private String url;
    private String requestString;
    private Object requestObj;
    private String responseString;
    private int responseCode;
    private HttpError httpError;
    private Map<String, String> requestHeaders = new HashMap<>();
    private Map<String, String> responseHeaders = new HashMap<>();

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null) {
            if (object instanceof HttpData) {
                HttpData data = (HttpData) object;
                if (getId().equals(data.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestString() {
        return requestString;
    }

    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }

    public Object getRequestObj() {
        return requestObj;
    }

    public void setRequestObj(Object requestObj) {
        this.requestObj = requestObj;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public HttpError getHttpError() {
        return httpError;
    }

    public void setHttpError(HttpError httpError) {
        this.httpError = httpError;
    }

    public void addRequestHeaders(String key, String value) {
        requestHeaders.put(key, value);
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void addResponseHeaders(String key, String value) {
        responseHeaders.put(key, value);
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }
}
