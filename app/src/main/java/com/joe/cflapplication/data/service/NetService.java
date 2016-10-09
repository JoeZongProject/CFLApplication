package com.joe.cflapplication.data.service;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;

import com.joe.cflapplication.data.model.foodmenu.FoodMenu;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @Autor zongdongdong on 2016/9/30.
 */

public class NetService<T extends BmobObject> {
    private ProgressDialog mProgressDialog;
    private Context context;
    private String progressContent;
    private boolean cancelable = true;

    private Object params;
    private Object responseInfoType = Object.class;
    private NetCallbackBase callback;
    private T request;

    public static NetService create(Context context) {
        return new NetService(context);
    }

    private NetService(Context context) {
        this.context = context;
    }

    public NetService addParams(T obj) {
        this.request = obj;
        return this;
    }

    /**
     * 如果你需要一个默认的进度条
     *
     * @return
     */
    public NetService addDefaultProgressing() {
        this.progressContent = "正在加载,请稍等...";
        return this;
    }

    /**
     * 如果你需要一个自定义文字的进度条
     *
     * @return
     */
    public NetService addProgressing(String progressContent) {
        this.progressContent = progressContent;
        return this;
    }

    /**
     * 如果你需要一个自定义文字以及自定义是否可以中途取消的进度条
     *
     * @return
     */
    public NetService addProgressing(String progressContent, boolean cancelable) {
        this.progressContent = progressContent;
        this.cancelable = cancelable;
        return this;
    }

    public void save(NetCallbackBase listener) {
        showProgress();
        request.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                dismissProgress();
                listener.onResult(e == null, s, e == null ? "" : e.getMessage());
            }
        });
    }

    public void delete(NetCallbackBase listener){
        showProgress();
        request.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dismissProgress();
                listener.onResult(e == null, "", e == null ? "" : e.getMessage());
            }
        });
    }

    public void update(NetCallbackBase listener){
        showProgress();
        request.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dismissProgress();
                listener.onResult(e == null, "", e == null ? "" : e.getMessage());
            }
        });
    }

    public void update(String objId, NetCallbackBase listener){
        showProgress();
        request.update(objId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dismissProgress();
                listener.onResult(e == null, "", e == null ? "" : e.getMessage());
            }
        });
    }

    public void queryAll(BmobQuery<T> query, NetCallbackBase listener){
        showProgress();
        query.order("-createdAt");
        query.findObjects(new FindListener<T>() {
            @Override
            public void done(List<T> list, BmobException e) {
                dismissProgress();
                listener.onResult(e == null, list, e == null ? "" : e.getMessage());
            }
        });
    }


    private void showProgress() {
        if (progressContent != null) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
            } else {
                mProgressDialog.cancel();
            }
            mProgressDialog.setMessage(progressContent);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.show();
        }
    }

    private void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }


}
