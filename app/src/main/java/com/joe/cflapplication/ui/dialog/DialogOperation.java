package com.joe.cflapplication.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.Window;

import com.joe.cflapplication.BR;
import com.joe.cflapplication.R;
import com.joe.cflapplication.databinding.DialogOperationBinding;
import com.joecorelibrary.mvvm.command.ReplyCommand;

/**
 * @Autor zongdongdong on 2016/9/30.
 */

public class DialogOperation extends Dialog{
    public DialogOperation(Context context) {
        super(context);
        initView();
    }

    public DialogOperation(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public DialogOperation(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogOperation(Context context, OnOperationListener listener){
        super(context);
        initView();
        this.listener = listener;
    }

    private OnOperationListener listener;
    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogOperationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_operation, null, false);
        binding.setVariable(BR.viewModel, this);
        setContentView(binding.getRoot());
    }

    public ReplyCommand onClickUpdateBtnCommand = new ReplyCommand(() -> {
        dismiss();
        if(listener != null){
            listener.update();
        }
    });

    public ReplyCommand onClickDeleteBtnCommand = new ReplyCommand(() -> {
        dismiss();
        if(listener != null){
            listener.delete();
        }
    });

    public interface OnOperationListener{
        void update();
        void delete();
    }

}
