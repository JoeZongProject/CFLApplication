package com.joecorelibrary.util.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.joecorelibrary.util.http.HttpData;
import com.joecorelibrary.util.http.HttpError;
import com.joecorelibrary.util.http.HttpMethod;
import com.joecorelibrary.util.http.OnHttpResultListener;
import com.joecorelibrary.util.okhttp.OkHttpUtils;
import com.joecorelibrary.util.okhttp.builder.GetBuilder;
import com.joecorelibrary.util.okhttp.builder.PostFormBuilder;
import com.joecorelibrary.util.okhttp.builder.PostStringBuilder;
import com.joecorelibrary.util.okhttp.callback.StringCallback;
import com.joecorelibrary.util.okhttp.request.RequestCall;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import okhttp3.Call;

/**
 * @Autor zongdongdong on 2016/9/20.
 */

public class OkHttpConnect {
    public static final String TAG = "@vcredit.http";
    private HttpData httpData;
    private OnHttpResultListener onHttpResultListener;


    public HttpData getHttpData() {
        return httpData;
    }

    public void setHttpData(HttpData httpData) {
        this.httpData = httpData;
    }

    public void request(OnHttpResultListener onHttpResultListener) {
        this.onHttpResultListener = onHttpResultListener;
        requestThread();
    }

    protected void requestThread() {
        RequestCall call = null;
        Log.i(TAG, "Id:" + httpData.getId() + "->" + httpData.getUrl());
        Log.i(TAG, "Id:" + httpData.getId() + "->" + httpData.getHttpMethod());
        Log.i(TAG, "Id:" + httpData.getId() + "->request:" + String.valueOf(httpData.getRequestObj()));
        if (httpData.getHttpMethod() == HttpMethod.POST) {
            PostFormBuilder postFormBuilder = OkHttpUtils.getInstance().post();
            postFormBuilder.url(httpData.getUrl());
            if (getHttpData().getRequestHeaders() != null) {
                for (String key : getHttpData().getRequestHeaders().keySet()) {
                    postFormBuilder.addHeader(key, getHttpData().getRequestHeaders().get(key));
                }
            }
            postFormBuilder.params(((Map) httpData.getRequestObj()));
            call = postFormBuilder.build();
        } else if (httpData.getHttpMethod() == HttpMethod.GET) {
            GetBuilder getBuilder = OkHttpUtils.getInstance().get();
            getBuilder.url(httpData.getUrl());
            if (getHttpData().getRequestHeaders() != null) {
                for (String key : getHttpData().getRequestHeaders().keySet()) {
                    getBuilder.addHeader(key, getHttpData().getRequestHeaders().get(key));
                }
            }
            getBuilder.params(((Map) httpData.getRequestObj()));
            call = getBuilder.build();
        }
        if (call != null) {
            call.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    setResultError(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    getHttpData().setResponseString(response);
                    onResult(true);
                    Log.i(TAG, "Id:" + httpData.getId() + "->response:" + response);
                }
            });
        }
    }

    private void setResultError(Exception e){
        if(e instanceof UnknownHostException){
            setResult(HttpError.CONNECT_FAILED);
        }else if(e instanceof SocketException){
            setResult(HttpError.CONNECT_FAILED);
        }else if(e instanceof SocketTimeoutException){
            setResult(HttpError.CONNECT_TIME_OUT);
        }else if(e instanceof Exception){
            setResult(HttpError.ERROR);
        }
        onResult(false);
    }

    private void setResult(HttpError httpError) {
//        Log.i(TAG, "Id:" + getHttpData().getId() + "->responseError:" + httpError);
        getHttpData().setHttpError(httpError);
    }

    protected void onResult(final boolean isSuccess) {
//        Log.i(TAG, "Id:" + getHttpData().getId() + "->running times:" + (System.currentTimeMillis() - startTime) + "ms");
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
