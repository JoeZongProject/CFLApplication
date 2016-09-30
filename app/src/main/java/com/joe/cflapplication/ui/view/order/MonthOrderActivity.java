package com.joe.cflapplication.ui.view.order;

import android.databinding.ObservableField;
import android.os.Bundle;
import com.joe.cflapplication.R;
import com.joe.cflapplication.databinding.ActivityMonthOrderBinding;
import com.joe.cflapplication.ui.base.BaseActivity;
import com.joe.cflapplication.ui.base.annotation.Layout;
import com.joecorelibrary.mvvm.base.IViewModel;
import com.joecorelibrary.mvvm.command.ReplyCommand;

@Layout(id = R.layout.activity_month_order)
public class MonthOrderActivity extends BaseActivity<ActivityMonthOrderBinding> implements IViewModel{
    public ObservableField<String> title = new ObservableField<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ReplyCommand onClickTitleBthCommand = new ReplyCommand(() -> {

    });
}
