package com.udofly.utils.time;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.gsy.gsyutils.R;

import java.lang.ref.WeakReference;

/**
 * 倒计时
 */
public class CountDownCaptchaUtils extends CountDownTimer {
    private WeakReference<TextView> mTextView;

    private int countDownColor = R.color.app_color, reCountDownColor = R.color.app_color;

    public CountDownCaptchaUtils(TextView textView) {
        super(60000, 1000);
        this.mTextView = new WeakReference(textView);
    }

    public CountDownCaptchaUtils(TextView textView, int countDownColor, int reCountDownColor) {
        super(60000, 1000);
        this.mTextView = new WeakReference(textView);
        this.countDownColor = countDownColor;
        this.reCountDownColor = reCountDownColor;
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
        mTextView.get().setText(millisUntilFinished / 1000 + "秒后重发");  //设置倒计时时间
//        mTextView.setBackgroundResource(R.drawable.corners_enable_button); //设置按钮为灰色，这时是不能点击的
//        mTextView.get().setTextColor(countDownColor);
        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        SpannableString     spannableString = new SpannableString(mTextView.get().getText().toString().trim());  //获取按钮上的文字
        ForegroundColorSpan span            = new ForegroundColorSpan(mTextView.get().getContext().getResources().getColor(countDownColor));
        spannableString.setSpan(span, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.get().setText(spannableString);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onFinish() {
        //用软引用 先判空 避免崩溃
        if (mTextView.get() == null) {
            cancle();
            return;
        }
        mTextView.get().setText("重新获取");
        mTextView.get().setClickable(true);//重新获得点击
//        mTextView.get().setTextColor(reCountDownColor);
        SpannableString     spannableString = new SpannableString(mTextView.get().getText().toString().trim());  //获取按钮上的文字
        ForegroundColorSpan span            = new ForegroundColorSpan(mTextView.get().getContext().getResources().getColor(reCountDownColor));
        spannableString.setSpan(span, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.get().setText(spannableString);
    }

    public void initCountDown() {
        //用软引用 先判空 避免崩溃
        if (mTextView.get() == null) {
            cancle();
            return;
        }
        mTextView.get().setText("获取验证码");
        mTextView.get().setClickable(true);//重新获得点击
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

}
