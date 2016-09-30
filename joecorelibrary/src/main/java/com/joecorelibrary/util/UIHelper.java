package com.joecorelibrary.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * @author zongdongdong on 2016/7/20.
 */
public class UIHelper {
    public static Toast mToast;

    public static void showShortToast(Context context, String msg){
        if(mToast == null){
            mToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    public static void showLongToast(Context context, String msg){
        if(mToast == null){
            mToast = Toast.makeText(context,msg,Toast.LENGTH_LONG);
        }
        mToast.setText(msg);
        mToast.show();
    }


    private static ProgressDialog mProgressDialog;
    public static void showProgress(Context context, String msg){
        if(mProgressDialog != null){
            mProgressDialog.cancel();
        }
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }
    public static void showDefaultProgress(Context context){
        if(mProgressDialog != null){
            mProgressDialog.cancel();
        }
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在加载数据...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    public static void hideProgress(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}
