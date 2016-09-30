package com.joecorelibrary.util.http;

/**
 * http返回结果接口
 *
 * @author yujinghong
 */
public interface OnHttpResultListener {

    /**
     * @param isSuccess
     * @param httpData
     */
    void onHttpResult(boolean isSuccess, HttpData httpData);
}