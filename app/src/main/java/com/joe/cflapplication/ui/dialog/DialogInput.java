package com.joe.cflapplication.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Window;

import com.joe.cflapplication.BR;
import com.joe.cflapplication.R;
import com.joe.cflapplication.databinding.DialogInputBinding;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.util.UIHelper;

/**
 * @Autor zongdongdong on 2016/10/9.
 */

public class DialogInput extends Dialog {
    public DialogInput(Context context) {
        super(context);
        initView();
    }

    public DialogInput(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public DialogInput(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogInput(Context context, String inputHint, OnInputListener listener) {
        super(context);
        initView();
        this.inputHint.set(inputHint);
        this.listener = listener;
    }

    public ObservableField<String> inputHint = new ObservableField<>();
    public ObservableField<String> inputContent = new ObservableField<>();
    public ObservableField<Integer> inputType = new ObservableField<>(8194);//8194代码数字+小数点
    public ObservableField<String> dialogTitle = new ObservableField<>("自己输入");
    private OnInputListener listener;

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogInputBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_input, null, false);
        binding.setVariable(BR.viewModel, this);
        setContentView(binding.getRoot());
    }

    public ReplyCommand onClickCancelBtnCommand = new ReplyCommand(() -> {
        dismiss();
    });

    public ReplyCommand onClickConfirmBtnCommand = new ReplyCommand(() -> {
        if(listener!=null){
            listener.onInput(inputContent.get());
        }
        dismiss();
    });

    public void setInputType(int type){
        this.inputType.set(type);
    }

    public void setDialogTitle(String title){
        this.dialogTitle.set(title);
    }

    public void setInputContent(String content){
        this.inputContent.set(content);
    }

    public interface OnInputListener {
        void onInput(String content);
    }
}
