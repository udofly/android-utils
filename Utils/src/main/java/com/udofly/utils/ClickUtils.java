package com.udofly.utils;

/**
 * Describe: 点击间隔
 * Created by udofly on 2021/12/24.
 */
public class ClickUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int  MIN_CLICK_DELAY_TIME = 600;
    private static       long lastClickTime        = 0;
    private static       long lastTime             = 0;

    /**
     * 600毫秒内不允许重复操作
     *
     * @return
     */
    public static boolean isFastClick() {
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            lastClickTime = curClickTime;
            return false;
        }
        return true;
    }

    /**
     * 间隔多长时间以后才能继续操作
     *
     * @param time
     * @return
     */
    public static boolean isFastClick(int time) {
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastTime) >= time) {
            lastTime = curClickTime;
            return false;
        }
        return true;
    }
}