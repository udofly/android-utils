package com.udofly.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.udofly.utils.weight.ViewUtils;


public class ParamsUtils {

    private static ParamsUtils instance;

    private ParamsUtils() {
    }

    public static ParamsUtils get() {
        if (instance == null) {
            synchronized (ParamsUtils.class) {
                if (instance == null) {
                    instance = new ParamsUtils();
                }
            }
        }
        return instance;
    }

    public void layoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
        view.setLayoutParams(params);
    }

    /**
     * 动态设置一个控件的高度
     *
     * @param view
     * @param width
     * @param height
     */
    public void layoutParamsHeight(View view, int width, int height) {
//        try {
//            Context context = view.getContext();
//            if (context == null) {
//                return;
//            }
//            int                    height_px = (int) (LxApplication.getInstance().screen_width_rate * ViewUtils.dp2px(context, height) / 100);
//            ViewGroup.LayoutParams params    = view.getLayoutParams();
//            if (params == null) {
//                params = new ViewGroup.LayoutParams(width, height_px);
//            } else {
//                params.width = width;
//                params.height = height_px;
//            }
//            view.setLayoutParams(params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 动态设置一个控件的高度
     *
     * @param view
     * @param width_dp
     * @param height_dp
     */
    public void layoutParamsAbsoluteWH(View view, int width_dp, int height_dp) {
        try {
            Context context = view.getContext();
            if (context == null) {
                return;
            }
            int                    height_px = ViewUtils.dp2px(context, height_dp);
            int                    width_px  = ViewUtils.dp2px(context, width_dp);
            ViewGroup.LayoutParams params    = view.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(width_px, height_px);
            } else {
                params.width = width_px;
                params.height = height_px;
            }
            view.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态设置一个控件的高度
     *
     * @param view
     * @param width_px
     * @param height_px
     */
    public void layoutParamsAbsoluteWHPx(View view, int width_px, int height_px) {
        try {
            Context context = view.getContext();
            if (context == null) {
                return;
            }
            ViewGroup.LayoutParams params    = view.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(width_px, height_px);
            } else {
                params.width = width_px;
                params.height = height_px;
            }
            view.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据比例 设置控件高度
     *
     * @param view
     * @param height
     */
    public void setRealHeightDp(View view, int height) {
//        float view_height = LxApplication.getInstance().screen_width_rate * height / 100;
//        setAbsoluteHeightDp(view, (int) view_height, 0);
    }


    /**
     * 动态设置一个控件的高度
     *
     * @param view
     * @param height
     */
    public void setAbsoluteHeightDp(View view, int height) {
        setAbsoluteHeightDp(view, height, 0);
    }

    /**
     * 动态设置一个控件的高度
     *
     * @param view
     * @param height
     */
    public void setAbsoluteWidthDp(View view, int height) {
        setAbsoluteHeightDp(view, height, 0);
    }

    /**
     * @param view
     * @param height
     */
    public void setAbsoluteDp(View view, int height,int width) {
        try {
            Context context = view.getContext();
            if (context == null) {
                return;
            }
            int                    height_px = ViewUtils.dp2px(context, height);
            int                    width_px = ViewUtils.dp2px(context, width);
            ViewGroup.LayoutParams params    = view.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(width_px, height_px);
            } else {
                params.height = height_px;
                params.width = width_px;
            }
            view.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param view
     * @param height
     * @param type   1:MATCH_PARENT    0: WRAP_CONTENT
     */
    public void setAbsoluteHeightDp(View view, int height, int type) {
        try {
            Context context = view.getContext();
            if (context == null) {
                return;
            }
            int                    height_px = ViewUtils.dp2px(context, height);
            ViewGroup.LayoutParams params    = view.getLayoutParams();
            if (params == null) {
                if (type == 1) {
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height_px);
                } else {
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height_px);
                }
            } else {
                if (type == 1) {
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                } else {
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                params.height = height_px;
            }
            view.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    /**
     * 方法：动态设置控件布局
     *
     * @param view      被设置的控件
     * @param topMargin 距上方距离
     */
    public void setLayoutParams(View view, int topMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = topMargin;
        view.setLayoutParams(params);
    }

    public void setLinearLayoutParams(View view, int topMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = topMargin;
        view.setLayoutParams(params);
    }

    public void setRelativeParams(View view, int topMargin) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = topMargin;
        view.setLayoutParams(params);
    }

    public void setLayoutParams(View view, int width, int height, int topMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.topMargin = topMargin;
        view.setLayoutParams(params);
    }

    public void layoutParamsLinear(Context context, View view, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewUtils.dp2px(context, width), ViewUtils.dp2px(context, height));
        if (leftMargin != 0) {
            params.leftMargin = ViewUtils.dp2px(context, leftMargin);
        }
        if (topMargin != 0) {
            params.topMargin = ViewUtils.dp2px(context, topMargin);
        }
        if (rightMargin != 0) {
            params.rightMargin = ViewUtils.dp2px(context, rightMargin);
        }
        if (bottomMargin != 0) {
            params.bottomMargin = ViewUtils.dp2px(context, bottomMargin);
        }
        view.setLayoutParams(params);
    }

    public void layoutParamsPxLinear(View view, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        if (leftMargin != 0) {
            params.leftMargin = leftMargin;
        }
        if (topMargin != 0) {
            params.topMargin = topMargin;
        }
        if (rightMargin != 0) {
            params.rightMargin = rightMargin;
        }
        if (bottomMargin != 0) {
            params.bottomMargin = bottomMargin;
        }
        view.setLayoutParams(params);
    }

    public void layoutParamsPxFrame(View view, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        if (leftMargin != 0) {
            params.leftMargin = leftMargin;
        }
        if (topMargin != 0) {
            params.topMargin = topMargin;
        }
        if (rightMargin != 0) {
            params.rightMargin = rightMargin;
        }
        if (bottomMargin != 0) {
            params.bottomMargin = bottomMargin;
        }
        view.setLayoutParams(params);
    }

    public void layoutParamsPxFrame(View view, int width, int height, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        if (leftMargin != 0) {
            params.leftMargin = leftMargin;
        }
        if (topMargin != 0) {
            params.topMargin = topMargin;
        }
        if (rightMargin != 0) {
            params.rightMargin = rightMargin;
        }
        if (bottomMargin != 0) {
            params.bottomMargin = bottomMargin;
        }
        params.gravity = gravity;
        view.setLayoutParams(params);
    }

    public void setLayoutBottomParams(View view, int width, int height, int gravity, int bottomMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.bottomMargin = bottomMargin;
        params.gravity = gravity;
        view.setLayoutParams(params);
    }

    public void layoutParamsWindowMatch(Window window) {
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = LxApplication.getInstance().height - StatusBarUtil.getStatusBarHeight(window.getContext());
//        window.setAttributes(params);
    }

    public void layoutParamsWindow(Window window, int width, int height) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = height;
        window.setAttributes(params);
    }

    public RelativeLayout.LayoutParams getRelativeParamsMatch() {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    public RelativeLayout.LayoutParams getRelativeParamsWrap() {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public LinearLayout.LayoutParams getLinearParamsMatch() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public LinearLayout.LayoutParams getLinearParamsWrap() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public LinearLayout.LayoutParams getLinearParamsMatchWrap() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public LinearLayout.LayoutParams getLinearParams(Context context, int width, int height) {
        return new LinearLayout.LayoutParams(ViewUtils.dp2px(context, width), ViewUtils.dp2px(context, height));
    }

}
