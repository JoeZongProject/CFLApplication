package com.joe.cflapplication.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.joe.cflapplication.BR;
import com.joe.cflapplication.R;
import com.joe.cflapplication.databinding.DialogInputBinding;
import com.joe.cflapplication.databinding.DialogTipBinding;
import com.joecorelibrary.mvvm.command.ReplyCommand;

/**
 * @Autor zongdongdong on 2016/10/9.
 */

public class DialogTip extends Dialog {
    public DialogTip(Context context) {
        super(context);
        initView();
    }

    public DialogTip(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public DialogTip(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogTip(Context context, String title, View.OnClickListener listener) {
        super(context);
        this.listener = listener;
        initView();
        content.set(title);
    }

    public ObservableField<String> content = new ObservableField<>();
    private View.OnClickListener listener;
    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogTipBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_tip, null, false);
        binding.setVariable(BR.viewModel, this);
        setContentView(binding.getRoot());
        binding.txtvConfirm.setOnClickListener(listener);
    }

    public ReplyCommand onClickCancelBtnCommand = new ReplyCommand(() -> {
        dismiss();
    });

    public ReplyCommand onClickConfirmBtnCommand = new ReplyCommand(() -> {
        dismiss();
    });
}
