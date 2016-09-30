package com.joe.cflapplication.data.model.order;

import cn.bmob.v3.BmobObject;

/**
 * @Autor zongdongdong on 2016/9/30.
 */

public class MonthOrder extends BmobObject {
    private String dayName;//2016年9月

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}
