package com.joe.cflapplication.ui.view.order.item;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.joe.cflapplication.data.model.order.Order;
import com.joe.cflapplication.data.model.order.OrderFoodMenu;
import com.joecorelibrary.util.StringUtils;

import java.util.Date;

/**
 * @Autor zongdongdong on 2016/10/9.
 */

public class ItemOrderViewModel {
    public Order order;
    public ObservableField<String> orderFoodMenus = new ObservableField<>();
    public ObservableField<Integer> orderState = new ObservableField<>(0);
    public ObservableField<String> orderStateDes = new ObservableField<>();
    public ObservableField<String> finalPrice = new ObservableField<>();
    public ObservableField<String> customInfo = new ObservableField<>();
    public ObservableField<String> orderCreateAt = new ObservableField<>();


    public ItemOrderViewModel(Order order) {
        this.order = order;
        orderState.set(order.getState());
        finalPrice.set(order.getFinalPrice());
        customInfo.set(order.getUserAddress() + "#" + order.getUserMobile());
        if(TextUtils.isEmpty(order.getCreatedAt())){
            orderCreateAt.set(StringUtils.dateFormater(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss"));
        }else{
            orderCreateAt.set(order.getCreatedAt());
        }
        String allFood = "";
        for (OrderFoodMenu orderFoodMenu : order.getFoodMenuList()) {
            allFood = allFood + orderFoodMenu.getFoodName() + "    x" + orderFoodMenu.getCount() + "\n";
        }
        orderFoodMenus.set(allFood.substring(0, allFood.length() - 1));
        setOrderStateDes(order.getState());
    }

    private void setOrderStateDes(int state) {
        switch (state) {
            case 0:
                orderStateDes.set("刚下单");
                break;
            case 1:
                orderStateDes.set("订单完成");
                break;
            case 2:
                orderStateDes.set("订单取消");
                break;
        }
    }

    public void updateOrderState(int state) {
        orderState.set(state);
        setOrderStateDes(state);
        order.setState(state);
    }
}
