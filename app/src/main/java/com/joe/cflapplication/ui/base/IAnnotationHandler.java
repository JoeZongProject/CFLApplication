package com.joe.cflapplication.ui.base;


import com.joecorelibrary.mvvm.base.IViewModel;

/**
 * Created by zongdongdong on 16/7/28.
 */

public interface IAnnotationHandler {

    String handleRightButton();

    int handleRightButtonResId();

    String handleTitle();

    int handleLayout();

    IViewModel handleViewModel(Object object);

    boolean handleListenSession();
}
