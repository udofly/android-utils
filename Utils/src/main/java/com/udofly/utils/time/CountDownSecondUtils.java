package com.udofly.utils.time;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * 倒计时时分秒
 */

/**
 * countDownSecondUtils = new CountDownSecondUtils(tv_empty, start_rest_time, false);
 * countDownSecondUtils.setCountdownListener(new CountDownSecondUtils.OnCountdownListener() {
 *     @Override
 *     public void finish() {
 *                  setNextLevel(0);
 *                          }
 *        });
 * countDownSecondUtils.start();
 */

public class CountDownSecondUtils extends CountDownTimer {
    private WeakReference<TextView> mTextView;
    private boolean                 isShowHour   = true;
    private boolean                 isShowMinute = true;

    public CountDownSecondUtils(TextView textView, long daojishiSecond) {
        super(daojishiSecond * 1000, 1000);
        this.mTextView = new WeakReference(textView);
        initTv(daojishiSecond);
    }

    private void initTv(long daojishiSecond) {
        mTextView.get().setText(DateUtil.updateBydaoJiShi(daojishiSecond, isShowHour, isShowMinute));  //设置倒计时时间
    }

    public CountDownSecondUtils(TextView textView, long daojishiSecond, boolean isShowHour) {
        super(daojishiSecond * 1000, 1000);
        this.mTextView = new WeakReference(textView);
        this.isShowHour = isShowHour;
        initTv(daojishiSecond);
    }

    public CountDownSecondUtils(TextView textView, long daojishiSecond, boolean isShowHour, long countDownInterval) {
        super(daojishiSecond * 1000, countDownInterval);
        this.mTextView = new WeakReference(textView);
        this.isShowHour = isShowHour;
        initTv(daojishiSecond);
    }
   public CountDownSecondUtils(TextView textView, long daojishiSecond, boolean isShowHour, boolean isShowMinute) {
        super(daojishiSecond * 1000, 1000);
        this.mTextView = new WeakReference(textView);
        this.isShowHour = isShowHour;
       this.isShowMinute = isShowMinute;
       initTv(daojishiSecond);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onTick(long millisUntilFinished) {
        //用弱引用 先判空 避免崩溃
        if (mTextView.get() == null) {
            cancle();
            return;
        }
        mTextView.get().setClickable(false); //设置不可点击
        mTextView.get().setText(DateUtil.updateBydaoJiShi(millisUntilFinished / 1000, isShowHour,isShowMinute));  //设置倒计时时间
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onFinish() {
        //用软引用 先判空 避免崩溃
        if (mTextView.get() == null) {
            cancle();
            return;
        }
        if (listener != null) {
            listener.finish();
        }
    }

    public void setClickAble(boolean is) {
        //用软引用 先判空 避免崩溃
        if (mTextView.get() == null) {
            cancle();
            return;
        }
        mTextView.get().setClickable(is);
    }

    public void cancle() {
        if (this != null) {
            this.cancel();
        }
    }


    public void setShowHours(boolean isShowHour) {
        this.isShowHour = isShowHour;
    }


    public interface OnCountdownListener {

        void finish();
    }

    private OnCountdownListener listener;

    public void setCountdownListener(OnCountdownListener listener) {
        this.listener = listener;
    }


}
