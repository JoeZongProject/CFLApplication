package com.joecorelibrary.util;

import android.app.Activity;

import java.util.Stack;

/**
 * @Autor zongdongdong on 2016/9/22.
 */

public class ActivityManager {
    public static ActivityManager instance;
    public Stack<Activity> activityStack;

    public static ActivityManager getInstance(){
        if(instance == null){
            instance = new ActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity){
        if(activityStack==null){
            activityStack=new Stack<>();
        }
        activityStack.add(activity);
    }

    public void closeAllActivity(){
        if(activityStack != null){
            for(Activity activity : activityStack){
                if(activity != null){
                    activity.finish();
                }
            }
        }
    }

    public void closeActivity(Class cls){
        if(activityStack != null){
            for(Activity activity : activityStack){
                if(activity != null && activity.getClass().equals(cls)){
                    activity.finish();
                    break;
                }
            }
            activityStack.remove(cls);
        }
    }

    /**
     * 获得当前栈顶Activity
     * @return
     */
    public Activity currentActivity(){
        if(activityStack == null)
            return null;
        Activity activity=activityStack.lastElement();
        return activity;
    }

    /**
     * 退出栈中所有Activity直到指定类
     */
    public void popAllActivityUntilOne(Class cls){
        if(activityStack != null){
            for(Activity activity : activityStack){
                if(activity == null || activity.getClass().equals(cls)){
                    break;
                }else{
                    activity.finish();
                }
            }
            activityStack.remove(cls);
        }
    }

    /**
     * 退出指定Activity
     * @param activity
     */
    public void closeActivity(Activity activity){
        if(activity!=null){
            activity.finish();
            activityStack.remove(activity);
            activity=null;
        }
    }
}
