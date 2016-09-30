package com.joecorelibrary.util.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

public class HttpConnect {

    private static final String TAG = "@nhs.http";
    private static final int DEFAULT_TIME_OUT = 15000;

    private HttpData httpData;
    private OnHttpResultListener onHttpResultListener;

    private boolean isCancel;
    private Integer timeoutMillis;

    private long startTime;

    public void request(HttpData httpData, OnHttpResultListener onHttpResultListener) {
        if (httpData == null) {
            return;
        }
        this.httpData = httpData;
        this.onHttpResultListener = onHttpResultListener;
        requestThread();
    }

    public void request(OnHttpResultListener onHttpResultListener) {
        this.onHttpResultListener = onHttpResultListener;
        requestThread();
    }

    public void setTimeout(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public void cancel() {
        this.isCancel = true;
    }

    public boolean isCancelled() {
        return isCancel;
    }

    public HttpData getHttpData() {
        return httpData;
    }

    public void setHttpData(HttpData httpData) {
        this.httpData = httpData;
    }

    protected void requestThread() {
        new Thread() {

            @Override
            public void run() {
                try {
                    startTime = System.currentTimeMillis();
                    URL url = new URL(getHttpData().getUrl());
                    Log.i(TAG, "Id:" + getHttpData().getId() + "->" + getHttpData().getUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    Log.i(TAG, "Id:" + getHttpData().getId() + "->" + getHttpData().getHttpMethod());
                    if (getHttpData().getHttpMethod() == HttpMethod.POST) {
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                    } else {
                        connection.setRequestMethod("GET");
                        connection.setDoOutput(false);
                        connection.setDoInput(true);
                    }
                    connection.setConnectTimeout(timeoutMillis != null ? timeoutMillis : DEFAULT_TIME_OUT);
                    connection.setReadTimeout(timeoutMillis != null ? timeoutMillis : DEFAULT_TIME_OUT);
                    if (getHttpData().getRequestHeaders() != null) {
                        for (String key : getHttpData().getRequestHeaders().keySet()) {
                            connection.setRequestProperty(key, getHttpData().getRequestHeaders().get(key));
                        }
                    }
                    connection.setRequestProperty("Connection", "keep-alive");
                    if (getHttpData().getHttpMethod() == HttpMethod.POST && getHttpData().getRequestString() != null) {
                        connection.setRequestProperty("Content-Length", String.valueOf(getHttpData().getRequestString().getBytes().length));
                        Log.i(TAG, "Id:" + getHttpData().getId() + "->request:" + getHttpData().getRequestString());
                        OutputStream outputStream = connection.getOutputStream();
                        try {
                            outputStream.write(getHttpData().getRequestString().getBytes());
                            outputStream.flush();
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        } finally {
                            outputStream.close();
                        }
                    }
                    if (isCancelled()) {
                        setResult(HttpError.CANCEL);
                        onResult(false);
                        return;
                    }
                    int responseCode = connection.getResponseCode();
                    Log.i(TAG, "Id:" + getHttpData().getId() + "->responseCode:" + String.valueOf(responseCode));
                    getHttpData().setResponseCode(responseCode);
                    if (responseCode != 200) {
                        setResult(HttpError.HTTP_RESPONSE_ERROR);
                        onResult(false);
                        return;
                    }
                    Map<String, List<String>> map = connection.getHeaderFields();
                    for (String name : map.keySet()) {
                        String value = map.get(name).toString();
                        value = value.substring(1, value.length() - 1);
                        getHttpData().addResponseHeaders(name, value);
                    }
                    if (isCancelled()) {
                        setResult(HttpError.CANCEL);
                        onResult(false);
                        return;
                    }
                    InputStream inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) != -1) {
                            bos.write(buffer, 0, length);
                        }
                        String responseString = new String(bos.toByteArray(), "utf-8");
                        Log.i(TAG, "Id:" + getHttpData().getId() + "->response:" + responseString);
                        getHttpData().setResponseString(responseString);
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    } finally {
                        inputStream.close();
                        bos.close();
                    }
                    if (isCancelled()) {
                        setResult(HttpError.CANCEL);
                        onResult(false);
                        return;
                    }
                    onResult(true);
                } catch (UnknownHostException e) {
                    setResult(HttpError.CONNECT_FAILED);
                    onResult(false);
                } catch (SocketException e) {
                    setResult(HttpError.CONNECT_FAILED);
                    onResult(false);
                } catch (SocketTimeoutException e) {
                    setResult(HttpError.CONNECT_TIME_OUT);
                    onResult(false);
                } catch (Exception e) {
                    Log.w(TAG, e);
                    setResult(HttpError.ERROR);
                    onResult(false);
                } catch (OutOfMemoryError e) {
                    setResult(HttpError.OUT_OF_MEMORY);
                    onResult(false);
                }
            }

        }.start();
    }

    private void setResult(HttpError httpError) {
        Log.i(TAG, "Id:" + getHttpData().getId() + "->responseError:" + httpError);
        getHttpData().setHttpError(httpError);
    }

    protected void onResult(final boolean isSuccess) {
        Log.i(TAG, "Id:" + getHttpData().getId() + "->running times:" + (System.currentTimeMillis() - startTime) + "ms");
        if (onHttpResultListener != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onHttpResultListener.onHttpResult(isSuccess, getHttpData());
                }
            });
        }
    }
}
