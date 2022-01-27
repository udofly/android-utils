package com.udofly.utils.weight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * describe: 监听软件盘
 * author: Gao Chunfa
 * time: 2018/2/1-上午9:21
 * Company: 猎象网络科技
 * other:
 */
public class SoftKeyBoardListener {
    private View rootView;//activity的根视图
    int rootViewVisibleHeight;//纪录根视图的显示高度
    private WeakReference<OnSoftKeyBoardChangeListener> reference;

    private static SoftKeyBoardListener instance;

    private SoftKeyBoardListener() {

    }

    public static SoftKeyBoardListener get() {
        if (instance == null) {
            synchronized (SoftKeyBoardListener.class) {
                if (instance == null) {
                    instance = new SoftKeyBoardListener();
                }
            }
        }
        return instance;
    }

    ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            //获取当前根视图在屏幕上显示的大小
            Rect r = new Rect();
            if (rootView == null) {
                return;
            }
            rootView.getWindowVisibleDisplayFrame(r);

            int visibleHeight = r.height();
            System.out.println("" + visibleHeight);
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight;
                return;
            }

            //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
            if (rootViewVisibleHeight == visibleHeight) {
                return;
            }

            //根视图显示高度变小超过200，可以看作软键盘显示了
            if (rootViewVisibleHeight - visibleHeight > 200) {
                if (reference != null && reference.get() != null) {
                    reference.get().keyBoardShow(rootViewVisibleHeight - visibleHeight);
                }
                rootViewVisibleHeight = visibleHeight;
                return;
            }

            //根视图显示高度变大超过200，可以看作软键盘隐藏了
            if (visibleHeight - rootViewVisibleHeight > 200) {
                if (reference != null && reference.get() != null) {
                    reference.get().keyBoardHide(visibleHeight - rootViewVisibleHeight);
                }
                rootViewVisibleHeight = visibleHeight;
                return;
            }

        }
    };

    public void init(View activityRootView) {
        //获取activity的根视图
        rootView = activityRootView;

        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.reference = new WeakReference<>(onSoftKeyBoardChangeListener);
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }

    public void setListener(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        init(activity.getWindow().getDecorView());
        setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }

    public void setListener(View activityRootView, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        init(activityRootView);
        setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }

    public void showKeyboard(Activity context, EditText editText) {
        //弹出对话框后直接弹出键盘
        editText.requestFocus();
        InputMethodManager manager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null)
            manager.showSoftInput(context.getCurrentFocus(), InputMethodManager.SHOW_FORCED);

    }

    public void hindKeyboard(Activity context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (context.getWindow().peekDecorView().getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(context.getWindow().peekDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 如果当前显示软键盘.则隐藏掉
     *
     * @param context
     */
    public void isSoftShowing(Activity context) {
        //获取当前屏幕内容的高度
        int screenHeight = context.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        if (screenHeight - rect.bottom != 0) {
            hindKeyboard(context);
        }
    }

    /**
     * 资源释放
     */
    public void release() {
        if (reference != null) {
            reference.clear();
            reference = null;
        }
        if (rootView != null) {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            rootView = null;
        }
    }
}