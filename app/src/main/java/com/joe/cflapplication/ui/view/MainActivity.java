package com.joe.cflapplication.ui.view;

import android.os.Bundle;

import com.joe.cflapplication.R;
import com.joe.cflapplication.databinding.ActivityMainBinding;
import com.joe.cflapplication.ui.base.BaseActivity;
import com.joe.cflapplication.ui.base.annotation.Layout;
import com.joecorelibrary.mvvm.base.IViewModel;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.util.UIHelper;

@Layout(id = R.layout.activity_main, title = "首页")
public class MainActivity extends BaseActivity<ActivityMainBinding> implements IViewModel {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 订单
     */
    public ReplyCommand onClickOrderBtnCommand = new ReplyCommand(() -> {
        UIHelper.showShortToast(this, "订单");


    });

    /**
     * 菜单
     */
    public ReplyCommand onClickFoodMenuBtnCommand = new ReplyCommand(() -> {
        UIHelper.showShortToast(this, "菜单");
    });

    /**
     * 统计
     */
    public ReplyCommand onClickCountBtnCommand = new ReplyCommand(() -> {
        UIHelper.showShortToast(this, "统计");
    });
}
