package com.joe.cflapplication.ui.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joe.cflapplication.R;
import com.joecorelibrary.activity.CoreBaseFragmentActivity;
import com.joecorelibrary.mvvm.base.IViewModel;
import com.joecorelibrary.util.DialogHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by zongdongdong on 2016/6/4.
 * Email-joe_zong@163.com
 */
public class BaseFragmentActivity<DataBinding extends ViewDataBinding> extends CoreBaseFragmentActivity implements Handler.Callback {
    private Handler handler;
    private DataBinding dataBinding;
    private IViewModel iViewModel;
    private IAnnotationHandler annotationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        annotationHandler = new AnnotationHandler(getClass());
        int layoutId = provideLayout();
        if (layoutId > 0) {
            preInitView();
            dataBinding = DataBindingUtil.setContentView(this, layoutId);
            iViewModel = provideViewModel();
            if (iViewModel != null) {
                if (this instanceof IViewModel) {
                } else {
                    bindContextToViewModel(iViewModel.getClass());
                }
                try {
                    Method initMethod = iViewModel.getClass().getDeclaredMethod("init");
                    initMethod.invoke(iViewModel);
                } catch (NoSuchMethodException e1) {
                    // 忽略
                } catch (IllegalAccessException e) {
                    // 忽略
                } catch (InvocationTargetException e) {
                    Log.e("Exception", iViewModel.getClass().getSimpleName() + " init()发生异常", e);
                }
                bindViewModelToUI();
            }
        }
        bindBackButtonOnClickListener();
        bindTitleText();
        bindRightButton();
    }

    /**
     * 获取数据绑定对象
     *
     * @return
     */
    public DataBinding getDataBinding() {
        return dataBinding;
    }

    /**
     * 获取ViewModel对象
     *
     * @return
     */
    public IViewModel getViewModel() {
        return iViewModel;
    }

    /**
     * 获取Handler对象
     *
     * @return
     */
    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler(this);
        }
        return handler;
    }

    /**
     * 相当于 @Title(xxx)
     *
     * @return
     */
    public String provideTitle() {
        return annotationHandler.handleTitle();
    }

    /**
     * 相当于 @Layout(xxx)
     *
     * @return
     */
    public int provideLayout() {
        return annotationHandler.handleLayout();
    }

    /**
     * 相当于 @ViewModel(XXX.class)
     *
     * @return
     */
    public IViewModel provideViewModel() {
        return annotationHandler.handleViewModel(this);
    }

    /**
     * 相当于 @RightButton("xxx");
     *
     * @return
     */
    public String provideRightButton() {
        return annotationHandler.handleRightButton();
    }

    /**
     * 相当于 @RightButton(buttonRes = R.mipmap.xxx);
     *
     * @return
     */
    public int provideRightButtonResId() {
        return annotationHandler.handleRightButtonResId();
    }

    /**
     * 标题栏左边返回按钮被点击
     * 默认为返回上一页,子类可以重写此方法来改变该行为
     *
     * @param view
     */
    public void onLeftBackButtonBeClick(View view) {
        finish();
    }

    /**
     * 标题栏右边按钮被点击
     * 子类通过重写该方法来处理按钮被点击后的事件
     *
     * @param view
     */
    public void onRightButtonBeClick(View view) {
    }

    /**
     * 处理 getHandler().setMessage()之后的回调处理
     *
     * @param message
     * @return
     */
    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    //=======================================================
    //
    //  以下为私有方法
    //
    //=======================================================

    private void bindRightButton() {
        // 处理右边的文字按钮
        TextView rightButtonView = (TextView) findViewById(R.id.textButton);
        if (rightButtonView != null) {
            String rightButtonContent = provideRightButton();
            if (rightButtonContent != null && !rightButtonContent.equals("")) {
                rightButtonView.setVisibility(View.VISIBLE);
                rightButtonView.setText(rightButtonContent);
                rightButtonView.setOnClickListener(view -> {
                    onRightButtonBeClick(view);
                });
            } else {
                rightButtonView.setVisibility(View.GONE);
            }
        }
        // 处理右边的图片按钮
        ImageView rightButtonImageView = (ImageView) findViewById(R.id.imageButton);
        if (rightButtonImageView != null) {
            int rightButtonResId = provideRightButtonResId();
            if (rightButtonResId > 0) {
                rightButtonImageView.setVisibility(View.VISIBLE);
                rightButtonImageView.setImageResource(rightButtonResId);
                rightButtonImageView.setOnClickListener(view -> {
                    onRightButtonBeClick(view);
                });
            } else {
                rightButtonImageView.setVisibility(View.GONE);
            }
        }
    }

    private void bindTitleText() {
        // 处理标题
        String titleContent = provideTitle();
        if (titleContent != null) {
            TextView titleView = (TextView) findViewById(R.id.actionbarTitle);
            if (titleView != null) {
                titleView.setText(titleContent);
            }
        }
    }

    private void bindBackButtonOnClickListener() {
        // 处理返回按钮
        View backButtonView = findViewById(R.id.rl_back);
        if (backButtonView != null) {
            backButtonView.setOnClickListener(view -> onLeftBackButtonBeClick(view));
        }
    }

    private void bindContextToViewModel(Class clazz) {
        if (!clazz.equals(Object.class)) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getType().equals(Context.class)) {
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(iViewModel, this);
                        return;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    declaredField.setAccessible(false);
                }
            }
            bindContextToViewModel(clazz.getSuperclass());
        }
    }

    private void bindViewModelToUI() {
        // 绑定两者
        Field[] declaredFields = dataBinding.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getType().equals(iViewModel.getClass())) {
                declaredField.setAccessible(true);
                try {
                    declaredField.set(dataBinding, iViewModel);
                } catch (IllegalAccessException e) {
                }
                declaredField.setAccessible(false);
                break;
            }
        }
    }

    /**
     * 初始化View之前
     */
    public void preInitView() {

    }
}
