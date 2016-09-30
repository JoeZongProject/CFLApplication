package com.joe.cflapplication.ui.base;

import com.joe.cflapplication.ui.base.annotation.Layout;
import com.joe.cflapplication.ui.base.annotation.ListenSession;
import com.joe.cflapplication.ui.base.annotation.RightButton;
import com.joe.cflapplication.ui.base.annotation.Title;
import com.joe.cflapplication.ui.base.annotation.ViewModel;
import com.joecorelibrary.mvvm.base.IViewModel;

import java.lang.reflect.Constructor;

/**
 * Created by zongdongdong on 16/7/28.
 */

public class AnnotationHandler implements IAnnotationHandler {
    private Class annotationOwnerClass;

    public AnnotationHandler(Class annotationOwnerClass) {
        this.annotationOwnerClass = annotationOwnerClass;
    }

    @Override
    public String handleRightButton() {
        RightButton rightButtonAnnotation = (RightButton) annotationOwnerClass.getAnnotation(RightButton.class);
        if (rightButtonAnnotation != null) {
            return rightButtonAnnotation.value();

        }
        Layout layoutAnnotation = (Layout) annotationOwnerClass.getAnnotation(Layout.class);
        if (layoutAnnotation != null) {
            return layoutAnnotation.rightButton();
        }
        return null;
    }

    @Override
    public int handleRightButtonResId() {
        RightButton rightButtonAnnotation = (RightButton) annotationOwnerClass.getAnnotation(RightButton.class);
        if (rightButtonAnnotation != null) {
            return rightButtonAnnotation.buttonRes();

        }
        Layout layoutAnnotation = (Layout) annotationOwnerClass.getAnnotation(Layout.class);
        if (layoutAnnotation != null) {
            return layoutAnnotation.rightButtonRes();
        }
        return -1;
    }

    @Override
    public String handleTitle() {
        Title titleAnnotation = (Title) annotationOwnerClass.getAnnotation(Title.class);
        if (titleAnnotation != null) {
            return titleAnnotation.value();

        }
        Layout layoutAnnotation = (Layout) annotationOwnerClass.getAnnotation(Layout.class);
        if (layoutAnnotation != null) {
            return layoutAnnotation.title();
        }
        return null;
    }

    @Override
    public int handleLayout() {
        Layout layoutAnnotation = (Layout) annotationOwnerClass.getAnnotation(Layout.class);
        if (layoutAnnotation != null) {
            return Math.max(layoutAnnotation.value(), layoutAnnotation.id());
        }
        return -1;
    }

    @Override
    public IViewModel handleViewModel(Object object) {
        ViewModel viewModelAnnotation = (ViewModel) annotationOwnerClass.getAnnotation(ViewModel.class);
        if (viewModelAnnotation != null) {
            Class<IViewModel> viewModelClass = viewModelAnnotation.value();
            Constructor[] constructors = viewModelClass.getDeclaredConstructors();
            if (constructors != null && constructors.length > 0) {
                for (Constructor constructor : constructors) {
                    try {
                        if (constructor.getParameterTypes().length == 0) { // 找到默认的构造方法
                            return (IViewModel) constructor.newInstance();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            throw new RuntimeException(viewModelClass.getName() + " 未找到默认的（无参）构造方法");
        } else if (object instanceof IViewModel) {
            return (IViewModel) object;
        }
        return null;
    }

    @Override
    public boolean handleListenSession() {
        ListenSession sessionAnnotation = (ListenSession) annotationOwnerClass.getAnnotation(ListenSession.class);
        if (sessionAnnotation != null) {
            return sessionAnnotation.session();
        }
        return true;
    }
}
