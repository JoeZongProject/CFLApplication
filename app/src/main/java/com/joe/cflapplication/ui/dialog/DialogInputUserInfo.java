package com.joe.cflapplication.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Window;

import com.joe.cflapplication.BR;
import com.joe.cflapplication.R;
import com.joe.cflapplication.data.model.foodmenu.FoodMenu;
import com.joe.cflapplication.databinding.DialogFoodMenuBinding;
import com.joe.cflapplication.databinding.DialogInputUserinfoBinding;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.util.StringUtils;
import com.joecorelibrary.util.UIHelper;

/**
 * @Autor zongdongdong on 2016/10/9.
 * 填写用户信息（下单的时候）
 */

public class DialogInputUserInfo extends Dialog {
    public DialogInputUserInfo(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogInputUserInfo(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public DialogInputUserInfo(Context context) {
        super(context);
        initView();
    }

    public DialogInputUserInfo(Context context, OnInputInfoListener listener){
        super(context);
        initView();
        this.listener = listener;
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogInputUserinfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_input_userinfo, null, false);
        binding.setVariable(BR.viewModel, this);
        setContentView(binding.getRoot());
    }

    public OnInputInfoListener listener;
    public ObservableField<String> userAddress = new ObservableField<>("");
    public ObservableField<String> userMobile = new ObservableField<>("");

    public ReplyCommand onClickCancelBtnCommand = new ReplyCommand(() -> {
        dismiss();
    });

    public ReplyCommand onClickConfirmBtnCommand = new ReplyCommand(() -> {
        String userA = userAddress.get();
        String userM = userMobile.get();
        if(TextUtils.isEmpty(userA) || TextUtils.isEmpty(userM)){
            UIHelper.showShortToast(getContext(), "输入相关信息,我不想在提示这种弱智问题了");
            return;
        }
        if(!StringUtils.isMobilePhone(userM)){
            UIHelper.showShortToast(getContext(), "请看看手机号有没有毛病");
            return;
        }
        if(listener != null){
            listener.onInput(userA, userM);
        }
        dismiss();
    });

    public interface OnInputInfoListener {
        void onInput(String... content);
    }
}
