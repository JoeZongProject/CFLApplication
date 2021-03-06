package com.joecorelibrary.mvvm.widget.banner.transformer;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 创建时间:15/6/19 上午8:41
 * 描述:
 */
public class StackPageTransformer extends BGAPageTransformer {

    @Override
    public void handleInvisiblePage(View view, float position) {
    }

    @Override
    public void handleLeftPage(View view, float position) {
    }

    @Override
    public void handleRightPage(View view, float position) {
        ViewHelper.setTranslationX(view, -view.getWidth() * position);
    }

}