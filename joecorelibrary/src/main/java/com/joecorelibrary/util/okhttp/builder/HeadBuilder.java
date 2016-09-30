package com.joecorelibrary.util.okhttp.builder;


import com.joecorelibrary.util.okhttp.OkHttpUtils;
import com.joecorelibrary.util.okhttp.request.OtherRequest;
import com.joecorelibrary.util.okhttp.request.RequestCall;

public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
