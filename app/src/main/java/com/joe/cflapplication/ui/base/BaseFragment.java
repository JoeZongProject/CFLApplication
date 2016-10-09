package com.joe.cflapplication.ui.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joe.cflapplication.R;
import com.joecorelibrary.activity.CoreBaseFragment;
import com.joecorelibrary.mvvm.base.IViewModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zongdongdong on 2016/6/4.
 */
public abstract class BaseFragment<DataBinding extends ViewDataBinding> extends CoreBaseFragment implements Handler.Callback {
    public String TAG = getClass().getSimpleName();
    private BaseFragmentActivity mActivity;
    public BaseFragmentActivity getParentActivity(){
        return mActivity;
    }

    private Handler handler;
    private DataBinding dataBinding;
    private IViewModel iViewModel;
    private IAnnotationHandler annotationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        annotationHandler = new AnnotationHandler(getClass());
        mActivity = (BaseFragmentActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = provideLayout();
        if (layoutId > 0) {
            dataBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), layoutId, container, false);
            iViewModel = provideViewModel();
            if (iViewModel != null) {
                if(this instanceof IViewModel){
                }else{
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
            bindTitleText(dataBinding.getRoot());
        }
        return dataBinding.getRoot();
    }

    /**
     * 获取数据绑定对象
     * @return
     */
    public DataBinding getDataBinding() {
        return dataBinding;
    }

    /**
     * 获取ViewModel对象
     * @return
     */
    public IViewModel getViewModel() {
        return iViewModel;
    }

    /**
     * 获取Handler对象
     * @return
     */
    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler(this);
        }
        return handler;
    }

    private void bindTitleText(View view) {
        // 处理标题
        String titleContent = provideTitle();
        if (titleContent != null) {
            TextView titleView = (TextView) view.findViewById(R.id.actionbarTitle);
            if (titleView != null) {
                titleView.setText(titleContent);
            }
        }
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
     * @return
     */
    public int provideLayout() {
        return annotationHandler.handleLayout();
    }

    /**
     * 相当于 @ViewModel(XXX.class)
     * @return
     */
    public IViewModel provideViewModel() {
        return annotationHandler.handleViewModel(this);
    }

    /**
     * 处理 getHandler().setMessage()之后的回调处理
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

    private void bindContextToViewModel(Class clazz) {
        if (!clazz.equals(Object.class)) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getType().equals(Context.class)) {
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(iViewModel, getParentActivity());
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
                try {declaredField.set(dataBinding, iViewModel);} catch (IllegalAccessException e) {}
                declaredField.setAccessible(false);
                break;
            }
        }
    }
}
