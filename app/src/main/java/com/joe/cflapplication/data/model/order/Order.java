package com.joe.cflapplication.data.model.order;

import com.joe.cflapplication.data.model.foodmenu.FoodMenu;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;

/**
 * @Autor zongdongdong on 2016/10/8.
 */

public class Order extends BmobObject {
    private Integer state = 0;//0刚下单，1订单完成,2订单取消
    private List<OrderFoodMenu> foodMenuList;
    private String totalPrice;//总价
    private String couponPrice;//优惠的价格
    private String finalPrice;//最终价格
    private String userMobile;
    private String userAddress;

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public List<OrderFoodMenu> getFoodMenuList() {
        return foodMenuList;
    }

    public void setFoodMenuList(List<OrderFoodMenu> foodMenuList) {
        this.foodMenuList = foodMenuList;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Integer getState() {
        return state;
    }
}
