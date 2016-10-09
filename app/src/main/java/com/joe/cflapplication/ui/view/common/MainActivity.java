package com.joe.cflapplication.ui.view.common;

import android.content.Intent;
import android.os.Bundle;

import com.joe.cflapplication.MyApplication;
import com.joe.cflapplication.R;
import com.joe.cflapplication.databinding.ActivityMainBinding;
import com.joe.cflapplication.ui.base.BaseActivity;
import com.joe.cflapplication.ui.base.annotation.Layout;
import com.joe.cflapplication.ui.view.about.AboutActivity;
import com.joe.cflapplication.ui.view.foodmenu.FoodMenuActivity;
import com.joe.cflapplication.ui.view.order.MonthOrderActivity;
import com.joecorelibrary.mvvm.base.IViewModel;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.util.UIHelper;

import cn.bmob.v3.Bmob;

@Layout(id = R.layout.activity_main, title = "首页")
public class MainActivity extends BaseActivity<ActivityMainBinding> implements IViewModel {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "4160b1c71ea4ad728df4cb8ebc69c3cb");
        MyApplication.getInstance().setFoodMenuList(null);
    }

    /**
     * 订单
     */
    public ReplyCommand onClickOrderBtnCommand = new ReplyCommand(() -> {
        startActivity(new Intent(this, MonthOrderActivity.class));
    });

    /**
     * 菜单
     */
    public ReplyCommand onClickFoodMenuBtnCommand = new ReplyCommand(() -> {
        startActivity(new Intent(this, FoodMenuActivity.class));
    });

    /**
     * 统计
     */
    public ReplyCommand onClickCountBtnCommand = new ReplyCommand(() -> {
        UIHelper.showShortToast(this, "别急，还在开发呢...");
    });

    /**
     * 关于
     */
    public ReplyCommand onClickAboutBtnCommand = new ReplyCommand(() -> {
        startActivity(new Intent(this, AboutActivity.class));
    });


    @Override
    public void onBackPressed() {
        exit();
    }

    private long exitTime = 0;

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            UIHelper.showShortToast(this, "再按一次才能退出，脑残");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
