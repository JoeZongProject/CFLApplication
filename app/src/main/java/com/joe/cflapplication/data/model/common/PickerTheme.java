package com.joe.cflapplication.data.model.common;

import android.graphics.Color;

import com.joe.cflapplication.ui.widget.datepicker.bizs.themes.DPBaseTheme;
import com.joe.cflapplication.ui.widget.datepicker.bizs.themes.DPTheme;


/**
 * @Autor zongdongdong on 2016/10/8.
 */

public class PickerTheme extends DPBaseTheme {
    public PickerTheme() {
        super();
    }

    /**
     * 月视图背景色
     * <p>
     * Color of MonthView's background
     *
     * @return 16进制颜色值 hex color
     */
    @Override
    public int colorBG() {
        return Color.parseColor("#ffffff");
    }

    /**
     * 背景圆颜色
     * <p>
     * Color of MonthView's selected circle
     *
     * @return 16进制颜色值 hex color
     */
    @Override
    public int colorBGCircle() {
        return Color.parseColor("#8010A1B4");
    }

    /**
     * 标题栏背景色
     * <p>
     * Color of TitleBar's background
     *
     * @return 16进制颜色值 hex color
     */
    @Override
    public int colorTitleBG() {
        return Color.parseColor("#10A1B4");
    }

    /**
     * 标题栏文本颜色
     * <p>
     * Color of TitleBar text
     *
     * @return 16进制颜色值 hex color
     */
    @Override
    public int colorTitle() {
        return Color.parseColor("#ffffff");
    }

    /**
     * 今天的背景色
     * <p>
     * Color of Today's background
     *
     * @return 16进制颜色值 hex color
     */
    @Override
    public int colorToday() {
        return Color.parseColor("#10A1B4");
    }

    /**
     * 公历文本颜色
     * <p>
     * Color of Gregorian text
     *
     * @return 16进制颜色值 hex color
     */
    @Override
    public int colorG() {
        return Color.parseColor("#333333");
    }



    /**
     * 节日文本颜色
     * <p>
     * Color of Festival text
     *
     * @return 16进制颜色值 hex color
     */
    @Override
    public int colorF() {
        return Color.parseColor("#cfcfcf");
    }

    /**
     * 周末文本颜色
     * <p>
     * Color of Weekend text
     *
     * @return 16进制颜色值 hex color
     */
    @Override
    public int colorWeekend() {
        return Color.parseColor("#333333");
    }

    /**
     * 假期文本颜色
     * <p>
     * Color of Holiday text
     *
     * @return 16进制颜色值 hex color
     */
    @Override
    public int colorHoliday() {
        return Color.parseColor("#333333");
    }

    @Override
    public int colorL() {
        return Color.parseColor("#cfcfcf");
    }
}
