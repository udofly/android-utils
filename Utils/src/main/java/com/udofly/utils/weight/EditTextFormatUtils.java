package com.udofly.utils.weight;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.udofly.utils.JsonUtils;
import com.udofly.utils.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @describe：规定输入框格式
 * @author： Gao Chunfa.
 * @time： 2017/5/6-16:13.
 */

public class EditTextFormatUtils {


    // 限制输入框不能输入汉字
    public static void StringWatcher(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (c >= 0x4e00 && c <= 0X9fff) {
                            s.delete(i,i+1);
                        }
                    }
                }
            }
        });
    }


    /**
     * 输入框输入金额的时候使用。注意。、布局中需要  android:inputType="numberDecimal"
     *
     * @param editText
     */
    public static void checkPriceNumber(EditText editText) {
        InputFilter filter = new CashierInputFilter();
        editText.setFilters(new InputFilter[]{filter});
    }

    public static void checkPriceNumber(EditText editText1, EditText editText2) {
        InputFilter filter = new CashierInputFilter();
        editText1.setFilters(new InputFilter[]{filter});
        editText2.setFilters(new InputFilter[]{filter});
    }

    public static void checkPriceNumber(EditText editText1, EditText editText2, EditText editText3) {
        InputFilter filter = new CashierInputFilter();
        editText1.setFilters(new InputFilter[]{filter});
        editText2.setFilters(new InputFilter[]{filter});
        editText3.setFilters(new InputFilter[]{filter});
    }

    public static void checkPriceNumber(EditText editText1, EditText editText2, EditText editText3, EditText editText4) {
        InputFilter filter = new CashierInputFilter();
        editText1.setFilters(new InputFilter[]{filter});
        editText2.setFilters(new InputFilter[]{filter});
        editText3.setFilters(new InputFilter[]{filter});
        editText4.setFilters(new InputFilter[]{filter});
    }


    //金额
    public static class CashierInputFilter implements InputFilter {
        Pattern mPattern;

        //输入的最大金额
        private static final int MAX_VALUE      = Integer.MAX_VALUE;
        //小数点后的位数
        private static final int POINTER_LENGTH = 2;

        private static final String POINTER = ".";

        private static final String ZERO = "0";

        public CashierInputFilter() {
            mPattern = Pattern.compile("([0-9]|\\.)*");
        }

        /**
         * @param source 新输入的字符串
         * @param start  新输入的字符串起始下标，一般为0
         * @param end    新输入的字符串终点下标，一般为source长度-1
         * @param dest   输入之前文本框内容
         * @param dstart 原内容起始坐标，一般为0
         * @param dend   原内容终点坐标，一般为dest长度-1
         * @return 输入内容
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String sourceText = source.toString();
            String destText   = dest.toString();

            //验证删除等按键
            if (TextUtils.isEmpty(sourceText)) {
                return "";
            }

            Matcher matcher = mPattern.matcher(source);
            //已经输入小数点的情况下，只能输入数字
            if (destText.contains(POINTER)) {
                if (!matcher.matches()) {
                    return "";
                } else {
                    if (POINTER.equals(source.toString())) {  //只能输入一个小数点
                        return "";
                    }
                }

                //验证小数点精度，保证小数点后只能输入两位
                int index  = destText.indexOf(POINTER);
                int length = dend - index;

                if (length > POINTER_LENGTH) {
                    return dest.subSequence(dstart, dend);
                }
            } else {
                /**
                 * 没有输入小数点的情况下，只能输入小数点和数字
                 * 1. 首位不能输入小数点
                 * 2. 如果首位输入0，则接下来只能输入小数点了
                 */
                if (!matcher.matches()) {
                    return "";
                } else {
                    if ((POINTER.equals(source.toString())) && TextUtils.isEmpty(destText)) {  //首位不能输入小数点
                        return "";
                    } else if (!POINTER.equals(source.toString()) && ZERO.equals(destText)) { //如果首位输入0，接下来只能输入小数点
                        return "";
                    }
                }
            }

            //验证输入金额的大小
            double sumText = JsonUtils.getDouble(destText + sourceText);
            if (sumText > MAX_VALUE) {
                return dest.subSequence(dstart, dend);
            }

            return dest.subSequence(dstart, dend) + sourceText;
        }
    }
    /**
     * 方法：包含Emoji表情字符串长度过滤器，限制输入的长度
     */
    public static class LengthFilter implements InputFilter {
        private final int mMax;

        public LengthFilter(int max) {
            mMax = max;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            String text = dest.toString() + source.toString();
//            Logs.e("text = " + text);
            int keep = filterLen(text);
//            Logs.e("keep = " + keep);
            if (keep < 0 || keep > mMax) {
                return "";
            } else {
                return source;
            }
        }
    }



    /**
     * 方法：获取包含Emoji表情字符串的字符个数
     *
     * @param source
     * @return
     */
    public static int filterLen(String source) {
        if (StringUtil.isNotNull(source)) {
//            return source.codePointCount(0, source.length());
            return filterEmoji(source).length();
        }
        return 0;
    }

    public static String filterEmoji(String source) {
//        Logs.e("before source.len --> " + source.length());
//        Logs.e("before1 unincode --> " + string2Unicode(source));
        if (source != null) {
            Pattern emoji1 = Pattern
                    .compile("([\ud83c\udc00-\ud83c\udfff]\u200d(?:[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udc00-\ud83e\udfff]))" +
//							"|([\ud83c\udc00-\ud83c\udfff]\u200d[\ud83d\udc00-\ud83d\udfff])" +
//							"|([\ud83c\udc00-\ud83c\udfff]\u200d[\ud83e\udc00-\ud83e\udfff])" +
                                    "|([\ud83c\udffb-\ud83c\udfff]\u200d[\u2600-\uffff][\u2600-\uffff])" +
                                    "|\u200d[\u2600-\uffff][\u2600-\uffff]" +
                                    "|\ud83c\udffb\ufe0f" +
                                    "|\ud83c\udffc\ufe0f" +
                                    "|\ud83c\udffd\ufe0f" +
                                    "|\ud83c\udffe\ufe0f" +
                                    "|\ud83c\udfff\ufe0f" +
                                    "|\u200d(?:[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udc00-\ud83e\udfff])" +
//                                    "|\ud83e\uddb1" +
                                    "|[\u2640-\u2642]" +
                                    "",
                            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher1 = emoji1.matcher(source);
            if (emojiMatcher1.find()) {
                String s = string2Unicode(source);
//                Logs.e("before unincode --> " + s);
                // 空
                source = emojiMatcher1.replaceAll("");
                String s1 = string2Unicode(source);
//                Logs.e("after1 unincode --> " + s1);
            }
//            Logs.e("after1 source.len --> " + source.length());
//            国旗
            Pattern emoji = Pattern
                    .compile(
                            "\ud83c\udff4\udb40\udc67\udb40\udc62\udb40\udc65\udb40\udc6e\udb40\udc67\udb40\udc7f" +
                                    "|\ud83c\udff4\udb40\udc67\udb40\udc62\udb40\udc73\udb40\udc63\udb40\udc74\udb40\udc7f" +
                                    "|\ud83c\udff4\udb40\udc67\udb40\udc62\udb40\udc77\udb40\udc6c\udb40\udc73\udb40\udc7f" +
                                    "|\ud83c\udff3\ufe0f\u200d\ud83c\udf08" +
                                    "|\ud83d\udd90\ud83c\udffd\ufe0f" +
                                    "|\ud83d\udd90\ud83c\udffb\ufe0f" +
                                    "|\ud83d\udd90\ud83c\udffc\ufe0f" +
                                    "|\ud83d\udd90\ud83c\udffe\ufe0f" +
                                    "|\ud83d\udd90\ud83c\udfff\ufe0f" +
                                    "|\ud83d\udc69\ud83c\udffb\u200d" +
                                    "|\ud83d\udc69\ud83c\udffc\u200d" +
                                    "|\ud83d\udc69\ud83c\udffd\u200d" +
                                    "|\ud83d\udc69\ud83c\udffe\u200d" +
                                    "|\ud83d\udc69\ud83c\udfff\u200d" +
                                    "|\ud83d\udc68\ud83c\udffb\u200d" +
                                    "|\ud83d\udc68\ud83c\udffc\u200d" +
                                    "|\ud83d\udc68\ud83c\udffd\u200d" +
                                    "|\ud83d\udc68\ud83c\udffe\u200d" +
                                    "|\ud83d\udc68\ud83c\udfff\u200d" +
                                    "|\u270c\ud83c\udffd\ufe0f" +
                                    "|\u270d\ud83c\udfff\ufe0f" +
                                    "|\u261d\ud83c\udffb\ufe0f" +
                                    "|\ud83e\udd36\ud83c\udffb" +
                                    "|\ud83e\udd36\ud83c\udffb" +
                                    "|\ud83c\udf85\ud83c\udffb" +
                                    "|\ud83e\udd18\ud83c\udffb" +
                                    "|\ud83e\udd18\ud83c\udffc" +
                                    "|\ud83e\udd18\ud83c\udffd" +
                                    "|\ud83e\udd18\ud83c\udffe" +
                                    "|\ud83e\udd18\ud83c\udfff" +
                                    "|\ud83e\udd34\ud83c\udffb" +
                                    "|\ud83e\udd34\ud83c\udffc" +
                                    "|\ud83e\udd34\ud83c\udffd" +
                                    "|\ud83e\udd34\ud83c\udffe" +
                                    "|\ud83e\udd34\ud83c\udfff" +
                                    "|\ud83e\uddd3\ud83c\udffb" +
                                    "|\ud83e\uddd3\ud83c\udffc" +
                                    "|\ud83e\uddd3\ud83c\udffd" +
                                    "|\ud83e\uddd3\ud83c\udffe" +
                                    "|\ud83e\uddd3\ud83c\udfff" +
                                    "|\ud83e\uddd4\ud83c\udffb" +
                                    "|\ud83e\uddd4\ud83c\udffc" +
                                    "|\ud83e\uddd4\ud83c\udffd" +
                                    "|\ud83e\uddd4\ud83c\udffe" +
                                    "|\ud83e\uddd4\ud83c\udfff" +
                                    "|\ud83e\uddd1\ud83c\udffb" +
                                    "|\ud83e\uddd1\ud83c\udffc" +
                                    "|\ud83e\uddd1\ud83c\udffd" +
                                    "|\ud83e\uddd1\ud83c\udffe" +
                                    "|\ud83e\uddd1\ud83c\udfff" +
                                    "|\ud83e\uddd2\ud83c\udffb" +
                                    "|\ud83e\uddd2\ud83c\udffc" +
                                    "|\ud83e\uddd2\ud83c\udffd" +
                                    "|\ud83e\uddd2\ud83c\udffe" +
                                    "|\ud83e\uddd2\ud83c\udfff" +
                                    "|\ud83c\udfc7\ud83c\udffb" +
                                    "|\ud83c\udfc7\ud83c\udffc" +
                                    "|\ud83c\udfc7\ud83c\udffd" +
                                    "|\ud83c\udfc7\ud83c\udffe" +
                                    "|\ud83c\udfc7\ud83c\udfff" +
                                    "|([\ud83d\udc00-\ud83d\udfff][\ud83c\udc00-\ud83c\udfff])" +
                                    "|([\ud83c\udde6-\ud83c\uddff][\ud83c\udde6-\ud83c\uddff])" +
                                    "|\ud83c\ude02\ufe0f" +
                                    "|\ud83c\udff3\ufe0f" +
                                    "|\ud83d\udd78\ufe0f" +
                                    "|\ud83d\udd4a\ufe0f" +
                                    "|\ud83d\udc3f\ufe0f" +
                                    "|\ud83c\udff5\ufe0f" +
                                    "|\ud83c\udf36\ufe0f" +
                                    "|\ud83d\udc41\ufe0f" +
                                    "|\ud83c\udfce\ufe0f" +
                                    "|\ud83c\udfcd\ufe0f" +
                                    "|\ud83d\udee9\ufe0f" +
                                    "|\ud83d\udee5\ufe0f" +
                                    "|\ud83d\udef3\ufe0f" +
                                    "|\ud83d\udd76\ufe0f" +
                                    "|\ud83d\udda5\ufe0f" +
                                    "|\ud83d\udda8\ufe0f" +
                                    "|\ud83d\uddb1\ufe0f" +
                                    "|\ud83d\uddb2\ufe0f" +
                                    "|\ud83d\udd79\ufe0f" +
                                    "|\ud83d\udddc\ufe0f" +
                                    "|\ud83d\udcfd\ufe0f" +
                                    "|\ud83c\udf9e\ufe0f" +
                                    "|\ud83c\udf99\ufe0f" +
                                    "|\ud83c\udf9a\ufe0f" +
                                    "|\ud83c\udf9b\ufe0f" +
                                    "|\ud83d\udd70\ufe0f" +
                                    "|\ud83d\udd6f\ufe0f" +
                                    "|\ud83d\udee2\ufe0f" +
                                    "|\ud83d\udee0\ufe0f" +
                                    "|\ud83d\udde1\ufe0f" +
                                    "|\ud83d\udee1\ufe0f" +
                                    "|\ud83d\udd73\ufe0f" +
                                    "|\ud83c\udf21\ufe0f" +
                                    "|\ud83d\udece\ufe0f" +
                                    "|\ud83d\udddd\ufe0f" +
                                    "|\ud83d\udecb\ufe0f" +
                                    "|\ud83d\udecf\ufe0f" +
                                    "|\ud83d\udecd\ufe0f" +
                                    "|\ud83c\udff7\ufe0f" +
                                    "|\ud83d\uddd2\ufe0f" +
                                    "|\ud83d\uddd3\ufe0f" +
                                    "|\ud83d\uddd1\ufe0f" +
                                    "|\ud83d\uddc3\ufe0f" +
                                    "|\ud83d\uddf3\ufe0f" +
                                    "|\ud83d\uddc4\ufe0f" +
                                    "|\ud83d\uddc2\ufe0f" +
                                    "|\ud83d\uddde\ufe0f" +
                                    "|\ud83d\udd87\ufe0f" +
                                    "|\ud83d\udd8a\ufe0f" +
                                    "|\ud83d\udd8b\ufe0f" +
                                    "|\ud83d\udd8c\ufe0f" +
                                    "|\ud83d\udd8d\ufe0f" +
                                    "|\ud83c\udf9f\ufe0f" +
                                    "|\ud83d\uddfa\ufe0f" +
                                    "|\ud83d\udef0\ufe0f" +
                                    "|\ud83d\udd49\ufe0f" +
                                    "|\ud83c\udd7e\ufe0f" +
                                    "|\ud83c\udd71\ufe0f" +
                                    "|\ud83c\udd70\ufe0f" +
                                    "|\ud83c\udd7f\ufe0f" +
                                    "|\ud83c\ude37\ufe0f" +
                                    "|\ud83c\udf7d\ufe0f" +
                                    "|\ud83c\udf96\ufe0f" +
                                    "|\ud83d\udd77\ufe0f" +
                                    "|\ud83d\udd90\ufe0f" +
                                    "|\u270a\ud83c\udffb" +
                                    "|\u270a\ud83c\udffc" +
                                    "|\u270a\ud83c\udffd" +
                                    "|\u270a\ud83c\udffe" +
                                    "|\u270a\ud83c\udfff" +
                                    "|\u270b\ud83c\udffb" +
                                    "|\u270b\ud83c\udffc" +
                                    "|\u270b\ud83c\udffd" +
                                    "|\u270b\ud83c\udffe" +
                                    "|\u270b\ud83c\udfff" +
                                    "|\ud83c\udfdc\ufe0f" +
                                    "|\ud83d\udee3\ufe0f" +
                                    "|\ud83d\udee4\ufe0f" +
                                    "|\ud83c\udfd5\ufe0f" +
                                    "|\ud83c\udfdd\ufe0f" +
                                    "|\ud83c\udfd6\ufe0f" +
                                    "|\ud83c\udfd4\ufe0f" +
                                    "|\ud83c\udfd9\ufe0f" +
                                    "|\ud83c\udfde\ufe0f" +
                                    "|\ud83d\uddbc\ufe0f" +
                                    "|\u0030\u20e3\ufe0f" +
                                    "|\u0039\u20e3\ufe0f" +
                                    "|\ud83c\udf29\ufe0f" +
                                    "|\ud83c\udf27\ufe0f" +
                                    "|\ud83c\udf26\ufe0f" +
                                    "|\ud83c\udf25\ufe0f" +
                                    "|\ud83c\udf24\ufe0f" +
                                    "|\ud83c\udf28\ufe0f" +
                                    "|\ud83c\udf2b\ufe0f" +
                                    "|\ud83c\udf2a\ufe0f" +
                                    "|\ud83c\udfd8\ufe0f" +
                                    "|\ud83c\udfda\ufe0f" +
                                    "|([\u0030-\u0039]\u20e3\ufe0f)" +
                                    "|(\ud83c[\udd00-\ude40]\ufe0f)" +
                                    "|([\u0030-\u0039]\ufe0f\u20e3)" +
                                    "|\ud80c\udc00" +
                                    "|\ud80c\udc01" +
                                    "|\ud80c\udc03" +
                                    "|\ud80c\udc05" +
                                    "|\ud80c\udc07" +
                                    "|\ud80c\udc0b" +
                                    "|\ud80c\udc0c" +
                                    "|\ud80c\udc0e" +
                                    "|\ud80c\udc19" +
                                    "|\ud80c\udc20" +
                                    "|\ud80c\udc24" +
                                    "|\ud80c\udc25" +
                                    "|\ud80c\udc2b" +
                                    "|\ud80c\udc3c" +
                                    "|\ud80c\udc45" +
                                    "|\ud80c\udc4d" +
                                    "|\ud80c\udc5f" +
                                    "|\ud80c\udc60" +
                                    "|\ud80c\udc69" +
                                    "|\ud80c\udc76" +
                                    "|\ud80c\udc77" +
                                    "|\ud80c\udcd2" +
                                    "|\ud80c\udcd7" +
                                    "|\ud80c\udcd8" +
                                    "|\ud80c\udcdd" +
                                    "|\ud80c\udcdf" +
                                    "|\ud80c\udce0" +
                                    "|\ud80c\udce1" +
                                    "|\ud80c\udce9" +
                                    "|\ud80c\udcec" +
                                    "|\ud80c\udcef" +
                                    "|\ud80c\udcf0" +
                                    "|\ud80c\udcf1" +
                                    "|\ud80c\udcf2" +
                                    "|\ud80c\udcf5" +
                                    "|\ud80c\udcf7" +
                                    "|\ud80c\udcf9" +
                                    "|\ud80c\udcfb" +
                                    "|\ud80c\udd3f" +
                                    "|\ud80c\udd5c" +
                                    "|\ud80c\udd66" +
                                    "|\ud80c\udd6a" +
                                    "|\ud80c\udd6d" +
                                    "|\ud80c\udd70" +
                                    "|\ud80c\udd88" +
                                    "|\ud80c\udd89" +
                                    "|\ud80c\udd8c" +
                                    "|\ud80c\udd8f" +
                                    "|\ud80c\udd97" +
                                    "|\ud80c\udd9f" +
                                    "|\ud80c\udda1" +
                                    "|\ud80c\udda3" +
                                    "|\ud80c\udda6" +
                                    "|\ud80c\udda7" +
                                    "|\ud80c\ude0e" +
                                    "|\u2601\ufe0e" +
                                    "|\u2602\ufe0e" +
                                    "|\u2614\ufe0e" +
                                    "|\u2603\ufe0e" +
                                    "|\u2660\ufe0e" +
                                    "|\u2663\ufe0e" +
                                    "|\u2665\ufe0e" +
                                    "|\u2639\ufe0e" +
                                    "|\u263a\ufe0e" +
                                    "|\u2615\ufe0e" +
                                    "|\u270c\ufe0e" +
                                    "|\u270d\ufe0e" +
                                    "|\u270f\ufe0e" +
                                    "|\u2712\ufe0e" +
                                    "|\u2702\ufe0e" +
                                    "|\u26be\ufe0e" +
                                    "|\u2708\ufe0e" +
                                    "|\u2693\ufe0e" +
                                    "|\u2668\ufe0e" +
                                    "|\u2648\ufe0e" +
                                    "|\u2668\ufe0f" +
                                    "|\u25fc\ufe0f" +
                                    "|\u25aa\ufe0f" +
                                    "|\u25ab\ufe0f" +
                                    "|\u2603\ufe0f" +
                                    "|\u262e\ufe0f" +
                                    "|\u271d\ufe0f" +
                                    "|\u262a\ufe0f" +
                                    "|\u2638\ufe0f" +
                                    "|\u2721\ufe0f" +
                                    "|\u2626\ufe0f" +
                                    "|\u262f\ufe0f" +
                                    "|\u2623\ufe0f" +
                                    "|\u2622\ufe0f" +
                                    "|\u269b\ufe0f" +
                                    "|\u3299\ufe0f" +
                                    "|\u3297\ufe0f" +
                                    "|\u2328\ufe0f" +
                                    "|\u2708\ufe0f" +
                                    "|\u2764\ufe0f" +
                                    "|\u2763\ufe0f" +
                                    "|\u2618\ufe0f" +
                                    "|\u263a\ufe0f" +
                                    "|\u2639\ufe0f" +
                                    "|\u2620\ufe0f" +
                                    "|\u26a0\ufe0f" +
                                    "|\u26d1\ufe0f" +
                                    "|\u26f4\ufe0f" +
                                    "|\u265f\ufe0f" +
                                    "|\u2602\ufe0f" +
                                    "|\u26f1\ufe0f" +
                                    "|\u26d3\ufe0f" +
                                    "|\u2709\ufe0f" +
                                    "|\u26b0\ufe0f" +
                                    "|\u26b1\ufe0f" +
                                    "|\u2697\ufe0f" +
                                    "|\u2696\ufe0f" +
                                    "|\u2692\ufe0f" +
                                    "|\u23f1\ufe0f" +
                                    "|\u2699\ufe0f" +
                                    "|\u23f2\ufe0f" +
                                    "|\u260e\ufe0f" +
                                    "|\u2694\ufe0f" +
                                    "|\u26cf\ufe0f" +
                                    "|\u270f\ufe0f" +
                                    "|\u2712\ufe0f" +
                                    "|\u2702\ufe0f" +
                                    "|\u2649\ufe0e" +
                                    "|\u264a\ufe0e" +
                                    "|\u264b\ufe0e" +
                                    "|\u264c\ufe0e" +
                                    "|\u264d\ufe0e" +
                                    "|\u264e\ufe0e" +
                                    "|\u264f\ufe0e" +
                                    "|\u2650\ufe0e" +
                                    "|\u2651\ufe0e" +
                                    "|\u2652\ufe0e" +
                                    "|\u2653\ufe0e" +
                                    "|\u269c\ufe0e" +
                                    "|\u271d\ufe0e" +
                                    "|\u2696\ufe0e" +
                                    "|\u2697\ufe0e" +
                                    "|\u2618\ufe0e" +
                                    "|\u269b\ufe0e" +
                                    "|\u2692\ufe0e" +
                                    "|\u262f\ufe0e" +
                                    "|\u262e\ufe0e" +
                                    "|\u262a\ufe0e" +
                                    "|\u2638\ufe0e" +
                                    "|\u2694\ufe0e" +
                                    "|\u2604\ufe0e" +
                                    "|\u26b1\ufe0e" +
                                    "|\u26b0\ufe0e" +
                                    "|\u2626\ufe0e" +
                                    "|\u2699\ufe0e" +
                                    "|\u2721\ufe0e" +
                                    "|\u2611\ufe0e" +
                                    "|\u2744\ufe0e" +
                                    "|\u2714\ufe0e" +
                                    "|\u2764\ufe0e" +
                                    "|\u2b07\ufe0e" +
                                    "|\u2b06\ufe0e" +
                                    "|\u2b05\ufe0e" +
                                    "|\u27a1\ufe0e" +
                                    "|\u261d\ufe0e" +
                                    "|\u2935\ufe0e" +
                                    "|\u2934\ufe0e" +
//                                    "|\u2702\ufe0f" +
                                    "|[\ud83c\udc00-\ud83c\udfff]" +
                                    "|[\ud83d\udc00-\ud83d\udfff]" +
                                    "|[\ud83e\udc00-\ud83e\udfff]" +
                                    "|([\u3290-\u3290]\ufe0f)" +
                                    "|([\u2194-\u2b07]\ufe0f)" +
                                    "|[\u2500-\u29ff]" +
                                    "|[\u20d0-\u20e2]" +
                                    "|[\u20e4-\u20f0]" +
                                    "|[\u2b00-\u2bff]" +
                                    "|[\u3297-\u3299]" +
                                    "|[\u2300-\u23ff]" +
                                    "|[\u2190-\u21ff]" +
                                    "|[\u24b6-\u24e9]",
                            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                String s = string2Unicode(source);
//                Logs.e("unincode --> " + s);
                // \ufffd
                source = emojiMatcher.replaceAll("\ufffd");
                String s2 = string2Unicode(source);
//                Logs.e("after2 unincode --> " + s2);
//                Logs.e("final after2 source.len --> " + source.length());
                return source;
            }
//            Logs.e("final after source.len --> " + source.length());
            return source;
        }
//        Logs.e("final after0 source.len --> " + source.length());
        return source;
    }
    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

}
