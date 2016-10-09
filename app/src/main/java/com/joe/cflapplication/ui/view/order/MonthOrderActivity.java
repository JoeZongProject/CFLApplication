package com.joe.cflapplication.ui.view.order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.joe.cflapplication.R;
import com.joe.cflapplication.data.event.UpdateOrderEvent;
import com.joe.cflapplication.data.model.common.PickerTheme;
import com.joe.cflapplication.data.model.order.Order;
import com.joe.cflapplication.data.service.NetCallbackBase;
import com.joe.cflapplication.data.service.NetService;
import com.joe.cflapplication.databinding.ActivityMonthOrderBinding;
import com.joe.cflapplication.databinding.ItemOrderBinding;
import com.joe.cflapplication.ui.base.BaseActivity;
import com.joe.cflapplication.ui.base.annotation.Layout;
import com.joe.cflapplication.ui.view.order.item.ItemOrderViewModel;
import com.joe.cflapplication.ui.widget.datepicker.bizs.themes.DPTManager;
import com.joe.cflapplication.ui.widget.datepicker.cons.DPMode;
import com.joe.cflapplication.ui.widget.datepicker.views.DatePicker;
import com.joecorelibrary.mvvm.base.IViewModel;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.mvvm.widget.pulltorefresh.XListView;
import com.joecorelibrary.util.JSONUtils;
import com.joecorelibrary.util.StringUtils;
import com.joecorelibrary.util.UIHelper;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


@Layout(id = R.layout.activity_month_order)
public class MonthOrderActivity extends BaseActivity<ActivityMonthOrderBinding> implements IViewModel {
    public Context mContext;
    private OrderListViewAdapter adapter;
    public ObservableField<String> actionBarTitle = new ObservableField<>();
    public ObservableField<String> allFinalPrice = new ObservableField<>();
    public ObservableField<String> allCouponPrice = new ObservableField<>();

    public ObservableList<ItemOrderViewModel> itemOrderViewModels = new ObservableArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        actionBarTitle.set(StringUtils.getCurrentYear() + "年" + StringUtils.getCurrentMonth() + "月" + StringUtils.getCurrentDay() + "日");
        actionBarTitle.set(StringUtils.dateFormater(new Date(System.currentTimeMillis()), "yyyy年MM月dd日"));
        DPTManager.getInstance().initCalendar(new PickerTheme());
        adapter = new OrderListViewAdapter();
        getDataBinding().orderListView.setAdapter(adapter);
        getDataBinding().orderListView.setPullLoadEnable(false);
        queryAllOrdersWithDate(actionBarTitle.get(), true);
        getDataBinding().orderListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                queryAllOrdersWithDate(actionBarTitle.get(), false);
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    /**
     * 日期选择
     */
    public ReplyCommand onClickTitleBthCommand = new ReplyCommand(() -> {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        DatePicker picker = new DatePicker(mContext);
        picker.setDate(StringUtils.getCurrentYear(), StringUtils.getCurrentMonth());
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(date -> {
            if (!date.equals(actionBarTitle.get())) {
                actionBarTitle.set(date);
                itemOrderViewModels.clear();
                adapter.notifyDataSetChanged();
                queryAllOrdersWithDate(date, true);
            }
            dialog.dismiss();
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(picker, params);
        dialog.getWindow().setGravity(Gravity.CENTER);
    });

    /**
     * 创建订单
     */
    public ReplyCommand onClickAddOrderCommand = new ReplyCommand(() -> {
        startActivity(new Intent(this, AddOrderActivity.class));
    });

    /**
     * item点击事件
     */
    public ReplyCommand<Integer> onClickOrderItemCommand = new ReplyCommand<>(position -> {

    });

    /**
     * 查询数据
     *
     * @param time
     */
    private void queryAllOrdersWithDate(String time, boolean isShowLoading) {
        String newTime = time.replace("年", "-").replace("月", "-").replace("日", "");
        if(isShowLoading){
            UIHelper.showDefaultProgress(this);
        }
        BmobQuery<Order> query = new BmobQuery<>();
        query.order("-createdAt");
        List<BmobQuery<Order>> and = new ArrayList<>();
        //大于00：00：00
        BmobQuery<Order> q1 = new BmobQuery<>();
        String start = newTime + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (Exception e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(date));
        and.add(q1);
        //小于23：59：59
        BmobQuery<Order> q2 = new BmobQuery<>();
        String end = newTime + " 23:59:59";
        Date date1 = null;
        try {
            date1 = sdf.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date1));
        and.add(q2);
        //添加复合与查询
        query.and(and);
        query.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                UIHelper.hideProgress();
                getDataBinding().orderListView.stopRefresh();
                if (e != null) {
                    UIHelper.showShortToast(mContext, "数据加载失败" + e.getMessage());
                } else {
                    Log.i(TAG, JSONUtils.toJson(list));
                    if (list.size() > 0) {
                        itemOrderViewModels.clear();
                        for (Order order : list) {
                            itemOrderViewModels.add(new ItemOrderViewModel(order));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        UIHelper.showShortToast(mContext, "拜吹牛逼了，到现在还没有单子");
                    }
                }
            }
        });
    }

    @Subscribe
    public void OnUpdateOrderEvent(UpdateOrderEvent event) {
        runOnUiThread(() -> {
            itemOrderViewModels.add(0, new ItemOrderViewModel(event.getOrder()));
            adapter.notifyDataSetChanged();
        });

    }

    private class OrderListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return itemOrderViewModels.size();
        }

        @Override
        public Object getItem(int position) {
            return itemOrderViewModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemOrderBinding binding;
            if (convertView == null) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_order, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(binding);
            } else {
                binding = (ItemOrderBinding) convertView.getTag();
            }
            binding.setViewModel(itemOrderViewModels.get(position));
            binding.txtvCancelOrder.setOnClickListener(v -> {
                updateOrderState(position, 2);
            });

            binding.txtvCompleteOrder.setOnClickListener(v -> {
                updateOrderState(position, 1);
            });
            return binding.getRoot();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            String allF = "0";
            String allC = "0";
            for(ItemOrderViewModel itemOrderViewModel : itemOrderViewModels){
                allF = new BigDecimal(allF).add(new BigDecimal(itemOrderViewModel.order.getFinalPrice())).toString();
                allC = new BigDecimal(allC).add(new BigDecimal(itemOrderViewModel.order.getCouponPrice())).toString();
            }
            allFinalPrice.set(allF);
            allCouponPrice.set(allC);
        }
    }

    public void updateOrderState(int position, int state) {
        ItemOrderViewModel itemOrderViewModel = itemOrderViewModels.get(position);
        Order order = itemOrderViewModel.order;
        Order newOrder = new Order();
        newOrder.setValue("state", state);
        NetService.create(mContext)
                .addProgressing("正在更新...")
                .addParams(order)
                .update(order.getObjectId(), new NetCallbackBase() {
                    @Override
                    public void onResult(boolean isSuccess, Object response, String errorMsg) {
                        super.onResult(isSuccess, response, errorMsg);
                        if (isSuccess) {
                            itemOrderViewModel.updateOrderState(state);
                            adapter.notifyDataSetChanged();
                        } else {
                            UIHelper.showShortToast(mContext, "订单处理失败" + errorMsg);
                        }
                    }
                });
    }
}
