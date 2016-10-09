package com.joe.cflapplication.data.model.order;

/**
 * @Autor zongdongdong on 2016/10/9.
 */

public class OrderFoodMenu {
    private String foodName;
    private String foodPrice;
    private String count = "0";

    public OrderFoodMenu(String foodName, String foodPrice, String count) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.count = count;
    }

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
