package com.joe.cflapplication.data.model.common;

import cn.bmob.v3.BmobObject;

/**
 * @Autor zongdongdong on 2016/10/9.
 */

public class AppVersion extends BmobObject{
    private Integer versionCode;
    private String appUrl;

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}
