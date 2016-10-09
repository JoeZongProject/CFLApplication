package com.joe.cflapplication;

import android.app.Application;

import com.google.gson.reflect.TypeToken;
import com.joe.cflapplication.data.model.foodmenu.FoodMenu;
import com.joe.cflapplication.data.preferences.AppPreferences;
import com.joecorelibrary.util.JSONUtils;

import java.util.List;

/**
 * @Autor zongdongdong on 2016/9/29.
 */

public class MyApplication extends Application {
    public static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public List<FoodMenu> foodMenuList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public List<FoodMenu> getFoodMenuList() {
        if (foodMenuList == null || foodMenuList.size() == 0) {
            foodMenuList = JSONUtils.fromJson(AppPreferences.getInstance().getFoodMeunInfo(), new TypeToken<List<FoodMenu>>(){});
        }
        return foodMenuList;
    }

    public void setFoodMenuList(List<FoodMenu> list){
        if(list == null || list.size() == 0){
            AppPreferences.getInstance().setFoodMenuInfo("");
            return;
        }
        AppPreferences.getInstance().setFoodMenuInfo(JSONUtils.toJson(list));
    }
}
