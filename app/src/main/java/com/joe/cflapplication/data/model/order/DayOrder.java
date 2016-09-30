package com.joe.cflapplication.data.model.order;

import cn.bmob.v3.BmobObject;

/**
 * @Autor zongdongdong on 2016/9/30.
 */

public class DayOrder extends BmobObject {
    private String monthName;//2016年9月
    private String dayName;//30号

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }
}
