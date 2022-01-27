package com.udofly.utils.weight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.udofly.utils.StringUtil;

import java.lang.reflect.Method;


public class ViewUtils {


    public interface OnGoodsDataListLister {
        public void onResult(JSONArray list_left, JSONArray list_right);
    }


    public static int getVirtualBarHeight(Context context) {
        int            vh            = 0;
        WindowManager  windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display        display       = windowManager.getDefaultDisplay();
        DisplayMetrics dm            = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isMIUI()) {
            if (isFullScreen(context)) {
                vh = 0;
            }
        } else {
            if (!hasDeviceNavigationBar(context)) {
                vh = 0;
            }
        }
        return vh;
    }

    /**
     * 获取是否有虚拟按键
     * 通过判断是否有物理返回键反向判断是否有虚拟按键
     *
     * @param context
     * @return
     */
    public static boolean hasDeviceNavigationBar(Context context) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display  = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point   size     = new Point();
            Point   realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            boolean result = realSize.y != size.y;
            return realSize.y != size.y;
        } else {

            boolean menu = ViewConfiguration.get(((Activity) context)).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static boolean isFullScreen(Context context) {
        // true 是手势，默认是 false
        // https://www.v2ex.com/t/470543
        return Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0;

    }

    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        // 这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }


    /**
     * dp转px：
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        if (context == null) {
            return (int) dpValue;
        }
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue sp值
     * @return context为null，返回-1，否则返回计算后的值
     */
    public static int sp2px(Context context, float spValue) {
        if (context == null) {
            return -1;
        }

        final float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scaledDensity + 0.5f);
    }

    /**
     * dp转px：
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * px转dp：
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将px值转换为sp值
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 设置editText光标位置
     *
     * @param editText
     */
    public static void setEdSelect(EditText editText) {
        try {
            editText.setSelection(editText.getText().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果为空.隐藏空间.如果显示.则显示.如果是textView 则显示内容
     */
    public static boolean setInfoView(String content, View view) {

        if (StringUtil.isNull(content)) {
            view.setVisibility(View.GONE);
            return false;
        }
        view.setVisibility(View.VISIBLE);
        try {
            ((TextView) view).setText(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * 设置图片大小尺寸
     */
    public static void setViewSize(int width, int height, View imageView) {
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.height = height;
        para.width = width;
        imageView.setLayoutParams(para);
    }


    /**
     * 通用分页加载
     *
     * @param list_temp
     * @param list_data
     * @param input_page
     * @param ll_empty_view
     */
    public static void setListData(JSONArray list_temp, JSONArray list_data, int input_page, LinearLayout ll_empty_view) {
        if (list_temp == null) {
            list_temp = new JSONArray();
        }
        if (input_page == 2) {
            list_data.clear();
        }
        if (list_temp.size() == 0) {
            input_page--;
        }

        list_data.addAll(list_temp);

        if (ll_empty_view != null) {
            ViewUtils.setEmptyView(ll_empty_view, list_data.size());
        }

    }

    /**
     * 设置空试图
     *
     * @param emptyView 空试图
     * @param size      list size
     */
    public static void setEmptyView(View emptyView, int size) {

        if (size == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }


    public static String getText(EditText editText) {

        if (editText == null) {
            return "";
        }
        return editText.getText().toString().trim();


    }

    public static String getText(TextView editText) {

        if (editText == null) {
            return "";
        }
        return editText.getText().toString().trim();


    }

    public static void setMobileClear(EditText ed_mobile, ImageView iv_clear) {
        ed_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input_mobile = s.toString().trim();
                if (StringUtil.isNotNull(input_mobile)) {
                    iv_clear.setVisibility(View.VISIBLE);
                } else {
                    iv_clear.setVisibility(View.GONE);
                }
            }
        });

    }
}
