package com.joe.cflapplication.data.service;


import com.joe.cflapplication.MyApplication;
import com.joecorelibrary.util.UIHelper;

public abstract class NetCallbackBase<T> {
    public void onResult(boolean isSuccess, T response, String errorMsg) {
        if (!isSuccess) {
            UIHelper.showShortToast(MyApplication.getInstance().getApplicationContext(), errorMsg);
        }
    }
}
