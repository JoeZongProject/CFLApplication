package com.joecorelibrary.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;

public class DialogHelper {

	/**
	 * 展示一个提示框（一个按钮）
	 * 按钮名称为确定
	 * 按钮点击监听为关闭当前提示框
	 * @param context 上下文
	 * @param title   提示框标题，不填为默认的“提示”
	 * @param msg     提示信息
	 */
	public static void showAlert(Context context, String title, String msg) {
		showAlert(context, title, msg, "确定");
	}

	/**
	 * 展示一个提示框（一个按钮）
	 * 此处按钮的作用仅仅是关闭提示框
	 *
	 * @param context 上下文
	 * @param title   提示框标题，不填为默认的“提示”
	 * @param msg     提示信息
	 * @param buttonText 按钮文字
	 */
	public static void showAlert(Context context, String title, String msg, String buttonText) {
		showAlert(context, title, msg, buttonText, null);
	}

	/**
	 * 展示一个提示框（一个按钮）
	 * 按钮名称固定为“确定”
	 * 可自由监听按钮点击
	 * @param context 上下文
	 * @param title   提示框标题，不填为默认的“提示”
	 * @param msg     提示信息
	 * @param listener 点击按钮后的监听
	 */
	public static Dialog showAlert(Context context, String title, String msg, DialogInterface.OnClickListener listener) {
		return showAlert(context, title, msg, "确定", listener);
	}

	/**
	 * 展示一个提示框
	 * 可自由设置按钮名称
	 * 可自由监听按钮点击
	 * @param context 上下文
	 * @param title   提示框标题，不填为默认的“提示”
	 * @param msg     提示信息
	 * @param buttonText 按钮文字
	 * @param listener 点击按钮后的监听
	 */
	public static Dialog showAlert(Context context, String title, String msg, String buttonText, DialogInterface.OnClickListener listener) {
		return  showDialog(context, title, msg, buttonText, listener, null, null, null, null, true);
	}

	public static Dialog showAlert(Context context, String title, String msg, String buttonText, DialogInterface.OnClickListener listener, boolean cancelable) {
		return  showDialog(context, title, msg, buttonText, listener, null, null, null, null, cancelable);
	}

	/**
	 * 展示一个包含两个按钮的对话框
	 * @param context 上下文
	 * @param title   提示框标题，不填为默认的“提示”
	 * @param msg     提示信息
	 * @param buttonText1 第一个按钮文字
	 * @param listener1
	 * @param buttonText2 第二个按钮文字
	 * @param listener2
	 * @param cancelable 点击外部区域和BACK键是否可以取消
	 * @return
	 */
	public static Dialog showDialog(Context context,
									 String title,
									 String msg,
									 String buttonText1, DialogInterface.OnClickListener listener1,
									 String buttonText2, DialogInterface.OnClickListener listener2, boolean cancelable) {
		return showDialog(context, title, msg, buttonText1, listener1, buttonText2, listener2, null, null, cancelable);
	}

	/**
	 * 弹出一个Prompt
	 * @param view
	 * @param title
	 * @param okListener
	 * @param cancelListener
	 */
	public static Dialog showPrompt(View view,
								  	   String title,
									   DialogInterface.OnClickListener okListener,
									   DialogInterface.OnClickListener cancelListener) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
					.setTitle(title)
					.setView(view)
					.setPositiveButton("确定", okListener)
					.setNegativeButton("取消", cancelListener);
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
			return dialog;
		} catch (Exception e) {
//			LogUtil.e("=======Show Dialog Error=======", e);
		}
		return null;
	}

	/**
	 * 展示一个包含三个按钮的对话框
	 * @param context
	 * @param title
	 * @param msg
	 * @param buttonText1
	 * @param listener1
	 * @param buttonText2
	 * @param listener2
	 * @param buttonText3
	 * @param listener3
	 * @param cancelable 点击外部区域和BACK键是否可以取消
	 * @return
	 */
	public static Dialog showDialog(Context context,
									 String title,
									 String msg,
									 String buttonText1, DialogInterface.OnClickListener listener1,
									 String buttonText2, DialogInterface.OnClickListener listener2,
									 String buttonText3, DialogInterface.OnClickListener listener3, boolean cancelable) {
		try {
			if (title == null || title.equals("")) {
				title = "提示";
			}
			AlertDialog.Builder builder;
			if (Build.VERSION.SDK_INT < 21) {
				builder = new AlertDialog.Builder(context);
			} else {
				builder = new AlertDialog.Builder(context,android.R.style.Theme_Material_Light_Dialog_Alert);
			}
			builder.setTitle(title).setMessage(msg);
			if (buttonText1 != null) {
				builder.setPositiveButton(buttonText1, listener1);
			}
			if (buttonText2 != null) {
				builder.setNeutralButton(buttonText2, listener2);
			}
			if (buttonText3 != null) {
				builder.setNegativeButton(buttonText3, listener3);
			}
			AlertDialog dialog = builder.create();
			dialog.setCancelable(cancelable);
			dialog.setCanceledOnTouchOutside(cancelable);
			dialog.show();
			return dialog;
		} catch (Throwable e) {
//			LogUtil.e("=======Show Dialog Error=======", e);
		}
		return null;
	}
	/**
	 * 展示一个包含三个按钮的对话框
	 * @param context
	 * @param title
	 * @param items
	 * @param listener
	 * @param cancelable 点击外部区域和BACK键是否可以取消
	 * @return
	 */
	public static Dialog showList(Context context,
								  String title,
								  String[] items,
								  DialogInterface.OnClickListener listener,
								  boolean cancelable) {
		try {
			if (title == null || title.equals("")) {
				title = "提示";
			}
			AlertDialog.Builder builder;
			if (Build.VERSION.SDK_INT < 21) {
				builder = new AlertDialog.Builder(context);
			} else {
				builder = new AlertDialog.Builder(context,android.R.style.Theme_Material_Light_Dialog_Alert);
			}
			builder.setTitle(title).setItems(items,listener);
			AlertDialog dialog = builder.create();
			dialog.setCancelable(cancelable);
			dialog.show();
			return dialog;
		} catch (Throwable e) {
//			LogUtil.e("=======Show Dialog Error=======", e);
		}
		return null;
	}

}