package com.joecorelibrary.util.http;


/**
 * http错误类型
 *
 * @author yujinghong
 */
public enum HttpError {
    /**
     * 取消
     */
    CANCEL,
    /**
     * 无法连接
     */
    CONNECT_FAILED,
    /**
     * 连接超时
     */
    CONNECT_TIME_OUT,
    /**
     * HTTP错误Code
     */
    HTTP_RESPONSE_ERROR,
    /**
     * 出错
     */
    ERROR,
    /**
     * 内存溢出
     */
    OUT_OF_MEMORY
}
