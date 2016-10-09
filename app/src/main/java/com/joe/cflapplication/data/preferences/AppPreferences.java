package com.joe.cflapplication.data.preferences;

import android.text.TextUtils;

import com.joe.cflapplication.MyApplication;
import com.joecorelibrary.base.BasePreferences;

/**
 * sp本地数据存储
 *
 * @Autor zongdongdong on 2016/9/18.
 */

public class AppPreferences extends BasePreferences {


    public final String FOOD_MENU_INFO = "cfl.food_menu_info";

    private static AppPreferences instance;

    public static AppPreferences getInstance() {
        if (instance == null){
            synchronized (AppPreferences.class){
                instance = new AppPreferences();
            }
        }
        return instance;
    }

    private AppPreferences() {
        request(MyApplication.getInstance().getApplicationContext(), "CFL_File");
    }

    public void setFoodMenuInfo(String data){
        setString(FOOD_MENU_INFO, data);
    }

    public String getFoodMeunInfo(){
        return getString(FOOD_MENU_INFO, "[]");
    }
}
