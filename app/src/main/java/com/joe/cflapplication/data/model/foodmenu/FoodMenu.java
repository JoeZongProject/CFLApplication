package com.joe.cflapplication.data.model.foodmenu;

import cn.bmob.v3.BmobObject;

/**
 * @Autor zongdongdong on 2016/9/30.
 */

public class FoodMenu extends BmobObject {
    private String foodName;
    private String foodPrice;
    private String count = "0";

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
