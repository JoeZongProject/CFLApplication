package com.joe.cflapplication.ui.view.order;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.text.TextUtils;

import com.joe.cflapplication.R;
import com.joe.cflapplication.data.model.order.DayOrder;
import com.joe.cflapplication.data.model.order.MonthOrder;
import com.joe.cflapplication.data.service.NetCallbackBase;
import com.joe.cflapplication.data.service.NetService;
import com.joe.cflapplication.databinding.ActivityMonthOrderBinding;
import com.joe.cflapplication.ui.base.BaseActivity;
import com.joe.cflapplication.ui.base.annotation.Layout;
import com.joecorelibrary.mvvm.base.IViewModel;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.util.StringUtils;
import com.joecorelibrary.util.UIHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;

@Layout(id = R.layout.activity_month_order)
public class MonthOrderActivity extends BaseActivity<ActivityMonthOrderBinding> implements IViewModel {
    public Context mContext;
    public String currentMonthTitle;
    public ObservableField<String> actionBarTitle = new ObservableField<>();
    public List<MonthOrder> monthOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        currentMonthTitle = StringUtils.dateFormater(new Date(System.currentTimeMillis()), "yyyy年MM月");
        actionBarTitle.set(currentMonthTitle);
    }

    public ReplyCommand onClickTitleBthCommand = new ReplyCommand(() -> {
        UIHelper.showShortToast(this, "我是测试的");
    });

    public void loadAllMonthOrder() {
        NetService.create(this)
                .addDefaultProgressing()
                .queryAll(new BmobQuery<MonthOrder>(), new NetCallbackBase<List<MonthOrder>>() {
                    @Override
                    public void onResult(boolean isSuccess, List<MonthOrder> response, String errorMsg) {
                        if (isSuccess) {
                            if (response != null && response.size() > 0) {
                                monthOrders = response;
                                for (MonthOrder monthOrder : response) {
                                    if (monthOrder.getDayName().equals(currentMonthTitle)) {
                                        actionBarTitle.set(currentMonthTitle);
                                        break;
                                    }
                                }
                            }
                            if (TextUtils.isEmpty(actionBarTitle.get())) {
                                addCurrentMonth();
                            }
                        } else {
                            UIHelper.showShortToast(mContext, "数据加载失败啦");
                        }
                    }
                });
    }

    public void addCurrentMonth() {
        MonthOrder monthOrder = new MonthOrder();
        monthOrder.setDayName(currentMonthTitle);
        NetService.create(this)
                .addDefaultProgressing()
                .addParams(monthOrder)
                .save(new NetCallbackBase<String>() {
                    @Override
                    public void onResult(boolean isSuccess, String response, String errorMsg) {
//                        super.onResult(isSuccess, response, errorMsg);
                        if(isSuccess){
                            actionBarTitle.set(currentMonthTitle);
                            loadCurrentDayOrderWithMonth(currentMonthTitle);
                        }else{

                        }
                    }
                });
    }

    private void loadCurrentDayOrderWithMonth(String month){
        BmobQuery query = new BmobQuery<DayOrder>();
        query.addWhereEqualTo("monthName", month);
        NetService.create(this)
                .addDefaultProgressing()
                .queryAll(query, new NetCallbackBase<List<DayOrder>>() {
                    @Override
                    public void onResult(boolean isSuccess, List<DayOrder> response, String errorMsg) {
                        if (isSuccess) {
                            if (response != null && response.size() > 0) {
                                if(month.equals(StringUtils.dateFormater(new Date(System.currentTimeMillis()), "yyyy年MM月"))){
                                    String dayName = response.get(0).getDayName();
                                    if(!dayName.equals(StringUtils.getCurrentDay())){
                                        DayOrder dayOrder = new DayOrder();
                                        dayOrder.setDayName(StringUtils.getCurrentDay());
                                        dayOrder.setMonthName(month);
                                        addCurrentDay(dayOrder);
                                    }
                                }
                            }
                            if (TextUtils.isEmpty(actionBarTitle.get())) {
                                addCurrentMonth();
                            }
                        } else {
                            UIHelper.showShortToast(mContext, "数据加载失败啦");
                        }
                    }
                });
    }

    private void addCurrentDay(DayOrder dayOrder){

    }
}
