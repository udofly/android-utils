package com.udofly.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 内部类：handler静态，弱引用封装
 */
public class UiHandler<T> extends Handler {
    WeakReference<T> weak;
    HandleCallback   callback;

    public UiHandler(T activity, HandleCallback callback) {
        weak = new WeakReference<T>(activity);
        this.callback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T object = weak.get();
        if (object != null) {
            // 更新ui
            if (callback!=null){
                callback.handle(msg);
            }


        }
    }

    public interface HandleCallback {
        public void handle(Message msg);
    }

}