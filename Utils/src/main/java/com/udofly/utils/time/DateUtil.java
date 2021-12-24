package com.udofly.utils.time;

import android.annotation.SuppressLint;

import com.udofly.utils.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


@SuppressLint("SimpleDateFormat")
public class DateUtil {

    public static final String DATE_TIME_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_2 = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_3 = "yyyy-MM-dd HH";
    public static final String DATE_TIME_4 = "MM-dd";
    public static final String DATE_TIME_5 = "YYYY-MM-dd";
    public static final String DATE_TIME_6 = "MM月dd日 HH:mm";
    public static final String DATE_TIME_7 = "HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_mm      = "HH:mm";

    private static List<String> list_time15 = new ArrayList<>();


    /**
     * 根据还剩余多少秒 进行倒计时
     *
     * @return
     */
    public static String updateBydaoJiShi(long timeRemain, boolean isShowHour, boolean isShowMinute) {
        timeRemain = (timeRemain * 1000);
        if (timeRemain == -1 || timeRemain == 0) {
            if (isShowHour) {
                return "00:00:00";
            } else if (isShowMinute) {
                return "00:00";
            } else {
                return "0";
            }

        }

        //秒钟
        long time_second = (timeRemain / 1000) % 60;

        if (!isShowMinute) {
            return time_second + "";
        }

        String str_second;
        if (time_second < 10) {
            str_second = "0" + time_second;
        } else {
            str_second = "" + time_second;
        }

        long time_temp = ((timeRemain / 1000) - time_second) / 60;
        //分钟
        long   time_minute = time_temp % 60;
        String str_minute;
        if (time_minute < 10) {
            str_minute = "0" + time_minute;
        } else {
            str_minute = "" + time_minute;
        }

        //展示小时  时:分:秒
        if (!isShowHour) {
            return str_minute + ":" + str_second;
        }
        //不展示小时,仅仅展示 分:秒
        else {
            time_temp = (time_temp - time_minute) / 60;
            //小时
            long   time_hour = time_temp;
            String str_hour;
            if (time_hour < 10) {
                str_hour = "0" + time_hour;
            } else {
                str_hour = "" + time_hour;
            }

            return str_hour + ":" + str_minute + ":" + str_second;
        }


    }


    public static List<String> getTime15() {
        list_time15.clear();
        for (int h = 0; h < 24; h++) {
            for (int m = 0; m < 4; m++) {
                String minute = "";
                if (m == 0) {
                    minute = "00";
                } else if (m == 1) {
                    minute = "15";
                } else if (m == 2) {
                    minute = "30";
                } else if (m == 3) {
                    minute = "45";
                }
                if (h < 10) {
                    list_time15.add("0" + h + ":" + minute);
                } else {
                    list_time15.add(h + ":" + minute);
                }
            }
        }
        return list_time15;
    }

    /**
     * 是否有新活动了
     *
     * @param data1 当前活动时间
     * @param data2 存储的活动时间
     * @return
     */
    public static boolean isAfter(String data1, String data2) {

        //首次打开时候.不显示红点
        if (StringUtil.isNull(data1)) {
            return false;
        }
        //时间为空.不显示红点
        if (StringUtil.isNull(data2)) {
            return false;
        }

        Date date1 = getDate(DATE_TIME_1, data1);
        long time1 = date1.getTime();

        Date date2 = getDate(DATE_TIME_1, data2);
        long time2 = date2.getTime();

        //活动时间>当前存储时间.代表有新活动了
        return time1 > time2;
    }


    public static int compare(String type, String dateStr, int minute) {
        String dateCurStr = getDate(type);
        Date   date       = getDate(type, dateStr);
        long   time       = date.getTime();
        long   time_cur   = getDate(type, dateCurStr).getTime();
        return (int) (time - time_cur - minute * 60 * 1000);
    }

    /**
     * 方法:时间格式化
     *
     * @param year
     * @param month
     * @param day
     * @return yyyy-MM-dd
     */
    public static String getYear_Month_Day(int year, int month, int day) {
        String _month = "";
        String _day   = "";
        if (month < 10) {
            _month = "0" + month;
        } else {
            _month = "" + month;
        }
        if (day < 10) {
            _day = "0" + day;
        } else {
            _day = "" + day;
        }
        return year + "-" + _month + "-" + _day;
    }

    public static long dateToTime(String type, String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        try {
            Date date = format.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 方法:当前时间
     *
     * @param type 格式 如:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDate(String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date             date   = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * 方法:时间格式化 Date转String
     *
     * @param type 格式 如:yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String getDate(String type, Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(type);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 按格式获取时间字符串
     *
     * @param type 格式 如:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getDate(String type, String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date             date   = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * 方法:当前时间
     *
     * @param type 格式 如:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDate(String type, long time) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date             date   = new Date(time);
        return format.format(date);
    }

    /**
     * 方法:多少时间之前
     *
     * @param type  格式 如:yyyy-MM-dd HH:mm:ss
     * @param times 前移时间
     * @return
     */
    public static String getBeforeTime(String type, int times) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date             date   = new Date(System.currentTimeMillis() - times);
        return format.format(date);
    }

    /**
     * 得到days天之前的日期 days==1 昨天
     *
     * @param days 前移天数 如:1
     * @return yyyy-MM-dd
     */
    public static String getBeforeDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1 * days);
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        return format.format(calendar.getTime());
    }

    /**
     * 得到今天的日期
     *
     * @return yyyy-MM-dd
     */
    public static String getToday() {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        return format.format(new Date());
    }

    /**
     * 得到days天之后的日期 days==1 明天
     *
     * @param days 后移天数 如:1
     * @return yyyy-MM-dd
     */
    public static String getAfterDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        return format.format(calendar.getTime());
    }

    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param type 格式 如:yyyy-MM-dd HH:mm:ss
     * @param time
     * @return
     */
    public static long getTime(String type, String time) {
        SimpleDateFormat format = new SimpleDateFormat(type, Locale.CHINA);
        Date             date;
        String           times  = null;
        try {
            date = format.parse(time);
            long   ltime = date.getTime();
            String stime = String.valueOf(ltime);
            times = stime.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.parseLong(times);
    }

    public static long getTimes(String type, String time) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        try {
            Date date = format.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取格林威治时间(1970年至今的秒数)
     */
    public static long getGMTime() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_1);
        format.setTimeZone(TimeZone.getTimeZone("Etc/Greenwich"));
        String           time    = format.format(new Date());
        SimpleDateFormat format1 = new SimpleDateFormat(DATE_TIME_1);
        Date             date    = null;
        try {
            date = format1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * 方法:多少个月之前
     *
     * @param type   格式 如:yyyy-MM-dd HH:mm:ss
     * @param number 前移月份
     * @return
     */
    public static String getBeforeMonth(String type, int number) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        Calendar         ca     = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.MONTH, -1 * number); //年份减1
        Date lastMonth = ca.getTime(); //结果
        return format.format(lastMonth);
    }

    /**
     * 获取当前日期前几个月的Date
     *
     * @param month
     * @return
     */
    public static Date getDate(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(Calendar.MONTH) == 1) {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
            calendar.set(Calendar.MONTH, 12);
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        }
        return calendar.getTime();
    }

    /**
     * 获取一个时间 距离现在有多少毫秒.用于正计时
     *
     * @param dateStr
     * @return
     */
    public static long getTime(String dateStr) {
        Date date       = getDate(DATE_TIME_1, dateStr);
        long time_order = date.getTime();
        long time_cur   = new Date().getTime();

        return time_cur - time_order;
    }

    public static long getTime(String dateStr, double hours) {
        Date date         = getDate(DATE_TIME_1, dateStr);
        long time_order   = date.getTime();
        long time_3_hours = (long) (hours * 60 * 60 * 1000);

        long time_cur    = new Date().getTime();
        long time_retain = time_3_hours + time_order - time_cur;
        if (time_retain <= 0) {
            return -1;
        }
        return time_retain;
    }

    public static long getTimeBySecond(String dateStr, double second) {
        Date date         = getDate(DATE_TIME_1, dateStr);
        long time_order   = date.getTime();
        long time_3_hours = (long) (second * 1000);

        long time_cur    = new Date().getTime();
        long time_retain = time_3_hours + time_order - time_cur;
        if (time_retain <= 0) {
            return -1;
        }
        return time_retain;
    }

    public static long getTime(String dateStr, long time) {
        Date date       = getDate(DATE_TIME_1, dateStr);
        long time_order = date.getTime();

        long time_cur    = new Date().getTime();
        long time_retain = time + time_order - time_cur;
        if (time_retain <= 0) {
            return -1;
        }
        return time_retain;
    }

    /**
     * 倒计时 更新 UI
     *
     * @param lastTime 开始的时间戳
     * @return 返回正计时 00:00:00
     */
    public static String updateTime(String lastTime) {

        long times_now = DateUtil.getTime(lastTime);

        //秒钟
        long   time_second = (times_now / 1000) % 60;
        String str_second;
        if (time_second < 10) {
            str_second = "0" + time_second;
        } else {
            str_second = "" + time_second;
        }

        long time_temp = ((times_now / 1000) - time_second) / 60;
        //分钟
        long   time_minute = time_temp % 60;
        String str_minute;
        if (time_minute < 10) {
            str_minute = "0" + time_minute;
        } else {
            str_minute = "" + time_minute;
        }

        time_temp = (time_temp - time_minute) / 60;
        //小时
        long   time_hour = time_temp;
        String str_hour;
        if (time_hour < 10) {
            str_hour = "0" + time_hour;
        } else {
            str_hour = "" + time_hour;
        }
        return str_hour + ":" + str_minute + ":" + str_second;

    }

    /**
     * maxMinute 分钟后停止
     *
     * @param lastTime  开始的时间戳
     * @param maxMinute 分钟数
     * @return 返回正计时 00:00
     */
    public static String updateTime(String lastTime, int maxMinute) {

        long times_now = DateUtil.getTime(lastTime);

        //秒钟
        long   time_second = (times_now / 1000) % 60;
        String str_second;
        if (time_second < 10) {
            str_second = "0" + time_second;
        } else {
            str_second = "" + time_second;
        }

        long time_temp = ((times_now / 1000) - time_second) / 60;
        //分钟
        long time_minute = time_temp % 60;
        if (time_minute >= maxMinute) {
            return "over";
        }
        String str_minute;
        if (time_minute < 10) {
            str_minute = "0" + time_minute;
        } else {
            str_minute = "" + time_minute;
        }
        return str_minute + ":" + str_second;

    }

    /**
     * maxSecond 秒钟后停止
     *
     * @param lastTime  开始的时间戳
     * @param maxSecond 秒钟数
     * @return 返回正计时 00:00
     */
    public static String updateTimeSecond(String lastTime, int maxSecond) {

        long times_now = DateUtil.getTime(lastTime);

        //秒钟
        long time_second = times_now / 1000;
        if (time_second >= maxSecond) {
            return "over";
        }
        return time_second + "";

    }

    /**
     * maxMinute 分钟后停止
     *
     * @param lastTime 开始的时间戳
     * @return 返回正计时 00:00
     */
    public static String updateByzhengTime(String lastTime) {

        long times_now = DateUtil.getTime(lastTime);

        //秒钟
        long   time_second = (times_now / 1000) % 60;
        String str_second;
        if (time_second < 10) {
            str_second = "0" + time_second;
        } else {
            str_second = "" + time_second;
        }

        long time_temp = ((times_now / 1000) - time_second) / 60;
        //分钟
        long time_minute = time_temp % 60;

        String str_minute;
        if (time_minute < 10) {
            str_minute = "0" + time_minute;
        } else {
            str_minute = "" + time_minute;
        }
        return str_minute + ":" + str_second;

    }



    //    倒计时 更新 UI 00:00
    public static String updateBydaoJiShiSecond(String lastTime, long count) {

        if (StringUtil.isNull(lastTime)) {
            return "00:00";
        }
        long times_now = getTimeBySecond(lastTime, count);
        if (times_now == -1) {
            return "00:00";
        }

        //秒钟
        long   time_second = (times_now / 1000) % 60;
        String str_second;
        if (time_second < 10) {
            str_second = "0" + time_second;
        } else {
            str_second = "" + time_second;
        }

        long time_temp = ((times_now / 1000) - time_second) / 60;
        //分钟
        long   time_minute = time_temp % 60;
        String str_minute;
        if (time_minute < 10) {
            str_minute = "0" + time_minute;
        } else {
            str_minute = "" + time_minute;
        }

        return str_minute + ":" + str_second;

    }

    //    倒计时 dan当前时间 UI 00:00:00
    public static String getCurrBydaoJiShi(long count) {
        String lastTime  = DateUtil.getDate(DateUtil.DATE_TIME_1);
        long   times_now = getTime(lastTime, count);
        if (times_now == -1) {
            return "00:00:00";
        }
        //秒钟
        long   time_second = (times_now / 1000) % 60;
        String str_second;
        if (time_second < 10) {
            str_second = "0" + time_second;
        } else {
            str_second = "" + time_second;
        }

        long time_temp = ((times_now / 1000) - time_second) / 60;
        //分钟
        long   time_minute = time_temp % 60;
        String str_minute;
        if (time_minute < 10) {
            str_minute = "0" + time_minute;
        } else {
            str_minute = "" + time_minute;
        }

        time_temp = (time_temp - time_minute) / 60;
        //小时
        long   time_hour = time_temp;
        String str_hour;
        if (time_hour < 10) {
            str_hour = "0" + time_hour;
        } else {
            str_hour = "" + time_hour;
        }

        return str_hour + ":" + str_minute + ":" + str_second;

    }

    /**
     * 获取当前时间的年月日时分秒
     *
     * @return
     */
    public static String current() {
        Calendar c      = Calendar.getInstance();
        int      year   = c.get(Calendar.YEAR);
        int      month  = c.get(Calendar.MONTH) + 1;
        int      day    = c.get(Calendar.DAY_OF_MONTH);
        int      hour   = c.get(Calendar.HOUR_OF_DAY);
        int      minute = c.get(Calendar.MINUTE);
        int      second = c.get(Calendar.SECOND);
        return year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒";
    }


    public static int getDifferenceYear(Date bigDate, Date smallDate) {
        Calendar bigCalendar = Calendar.getInstance();
        bigCalendar.setTime(bigDate);

        Calendar smallCalendar = Calendar.getInstance();
        smallCalendar.setTime(smallDate);

        int year = bigCalendar.get(Calendar.YEAR) - smallCalendar.get(Calendar.YEAR);
        if (year > 0) {
            if (bigCalendar.get(Calendar.MONTH) > smallCalendar.get(Calendar.MONTH)) {
                year = year - 1;
            } else if (bigCalendar.get(Calendar.DATE) > smallCalendar.get(Calendar.DATE)) {
                year = year - 1;
            }
        }
        return year;
    }


    /**
     * 根据指定年，月，日返回date
     *
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static Date getDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar.getTime();
    }

    /**
     * 将一个时间转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time    = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 显示时间方式
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        Date             time   = DateUtil.getDate(DateUtil.DATE_TIME_1, sdate);
        if (time == null) {
            return "";
        }
        String   ftime = "";
        Calendar cal   = Calendar.getInstance();

        //判断是否是同一天
        String curDate   = format.format(cal.getTime());
        String paramDate = format.format(time);
        if (curDate.equals(paramDate)) {
            int minute = (int) ((cal.getTimeInMillis() - time.getTime()) / 60000);
            if (minute < 5) {
                ftime = "刚刚";
            } else {
                int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
                if (hour == 0)
                    ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
                else
                    ftime = hour + "小时前";
            }
            return ftime;
        }

        long lt   = time.getTime() / 86400000;
        long ct   = cal.getTimeInMillis() / 86400000;
        int  days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days < 30) {
            ftime = days + "天前";
        } else if (days < 365) {
            ftime = getDate(DATE_TIME_4, time);
        } else {
            ftime = getDate(DATE_TIME_5, time);
        }
        return ftime;
    }

    /**
     * 试音队列中 显示时间方式
     *
     * @param sdate
     * @return
     */
    public static String friendly_time_order(String sdate) {
        Date time = DateUtil.getDate(DateUtil.DATE_TIME_1, sdate);
        if (time == null) {
            return "";
        }
        String   ftime = "";
        Calendar cal   = Calendar.getInstance();

        int second = (int) ((cal.getTimeInMillis() - time.getTime()) / 1000);
        if (second < 60) {
            ftime = second + "秒前";
        } else if (second < 60 * 60) {
            ftime = second / 60 + "分钟前";
        } else {
            int hour = second / 3660;
            ftime = hour + "小时前";
        }
        return ftime;

    }

    /**
     * 按格式获取时间字符串
     *
     * @return
     */
    public static long getTimeByDate(String type, String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        try {
            Date date = format.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long dateToStamp(String dateStr) {
        String[] time = dateStr.split(":");
        long     data = 0;
        if (time.length >= 3) {
            data += Integer.parseInt(time[2]);
        }
        if (time.length >= 2) {
            data += (Integer.parseInt(time[1]) * 60);
        }
        if (time.length >= 1) {
            data += (Integer.parseInt(time[0]) * 60 * 60);
        }
        return data * 1000;
    }

    /**
     * 引导好评
     *
     * @return
     */
//    public static boolean isTwoDays(Context context) {
//
////        是否已经谈过窗了.
//        boolean isFirstShowGuide = SharedPreferencesUtil.getPrefBoolean(context, IS_GUIDE_APPRAISE, true);
//        if (!isFirstShowGuide) {
//            return false;
//        }
//
//        String           time_last        = SharedPreferencesUtil.getPrefString(context, CUR_TIME, "");
//        long             cur_time         = System.currentTimeMillis();
//        long             las_day_time     = cur_time - (24 * 3600000);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
//        String           time_cur         = simpleDateFormat.format(cur_time);
//        String           time_last_day    = simpleDateFormat.format(las_day_time);
//        if (time_last_day.equals(time_last)) {
//            //符合条件弹窗
//            SharedPreferencesUtil.setPrefBoolean(context, IS_CAN_GUIDE_APPRAISE, true);
//            return true;
//        }
//        SharedPreferencesUtil.setPrefString(context, CUR_TIME, time_cur);
//        return false;
//
//    }

    /**
     * 每天弹窗一次
     *
     * @return
     */
//    public static boolean isTodayFirst(Context context, String key) {
//
//        String           time_last        = SharedPreferencesUtil.getPrefString(context, MAIN_POP_LAST_TIME + key, "");
//        long             cur_time         = System.currentTimeMillis();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
//        String           time_cur         = simpleDateFormat.format(cur_time);
//        if (time_cur.equals(time_last)) {
//            //说明今天已经谈过一次了.不能再谈了
//            return false;
//        }
//        SharedPreferencesUtil.setPrefString(context, MAIN_POP_LAST_TIME + key, time_cur);
//        return true;
//    }
//
//    public static boolean isTwoDays(Context context, String account_id, String key, int max) {
//        int count = SharedPreferencesUtil.getPrefInt(context, key, 0);
//        if (count >= max) {
//            return true;
//        }
//        boolean isFirstShow = SharedPreferencesUtil.getPrefBoolean(context, key + DateUtil.getToday() + account_id, true);
//        if (isFirstShow) {
//            count++;
//            SharedPreferencesUtil.setPrefBoolean(context, key + DateUtil.getToday() + account_id, false);
//            SharedPreferencesUtil.setPrefInt(context, key, count);
//        } else {
//            return true;
//        }
//        return false;
//    }

    /**
     * 是否可以取消订单
     *
     * @return
     */
    public static boolean isCanCancelOrder(int timespan, String date_create) {
        if (StringUtil.isNull(date_create)) {
            return false;
        }

        long time_dur    = timespan * 60 * 1000;
        long time_cur    = new Date().getTime();
        long time_create = getTimeByDate(DATE_TIME_1, date_create);

        if (time_cur - time_create >= time_dur) {
            return true;
        }

        return false;

    }

    /**
     * 日期变量转成对应的星期字符串
     *
     * @param date
     * @return
     */
    public static final int      WEEKDAYS = 7;
    //星期字符数组
    public static       String[] WEEK     = {"周日", "周一", "周二", "周三",
            "周四", "周五", "周六"};

    public static String DateToWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }
        return WEEK[dayIndex - 1];
    }

    /**
     * 时间转化为时间格式
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String           date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 时间转化为时间格式
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr1(long timeStamp) {
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String           date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 时间转化为时间(几点)
     *
     * @param time
     * @return
     */
    public static String timeStampToTime(long time) {
        SimpleDateFormat sdf  = new SimpleDateFormat("HH:mm");
        String           date = sdf.format(time * 1000);
        return date;
    }

    /**
     * 将日期格式转化为时间(秒数)
     *
     * @param time
     * @return
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date             date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 将日期格式转化为时间(秒数)
     *
     * @param time
     * @return
     */
    public static long getString2Date(String time) {
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        Date             date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * 判断是否大于当前时间
     *
     * @param time
     * @return
     */
    public static boolean judgeCurrTime(String time) {
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date             date = new Date();
        try {
            date = sdf.parse(time);
            long t     = date.getTime();
            long round = System.currentTimeMillis();
            if (t - round > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    /**
     * 判断是否大于当前时间
     *
     * @param time
     * @return
     */
    public static boolean judgeCurrTime(long time) {
        long round = System.currentTimeMillis();
        if (time - round > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较后面的时间是否大于前面的时间
     *
     * @param
     * @return
     */
    public static boolean judgeTime2Time(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            //转化为时间
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            //获取秒数作比较
            long l1 = date1.getTime() / 1000;
            long l2 = date2.getTime() / 1000;
            if (l2 - l1 > 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获得系统时间 年、月、日、小时、分钟
     *
     * @return HashMap
     */
    public static HashMap<String, Object> getTimeNew() {
        HashMap<String, Object> map      = new HashMap<>();
        Calendar                calendar = Calendar.getInstance();
        map.put("year", calendar.get(Calendar.YEAR));

        int month = calendar.get(Calendar.MONTH);
        if (month > 11) {
            month = 1;
        } else {
            month = month + 1;
        }

        map.put("month", month);
//        map.put("day",calendar.get(Calendar.DAY_OF_MONTH));
//        map.put("hour",calendar.get(Calendar.HOUR_OF_DAY));
//        map.put("minute",calendar.get(Calendar.MINUTE));
        return map;
    }

    /**
     * 得到日期 yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String formatDate(long time) {
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        String           date = sdf.format(time * 1000);
        return date;
    }

    /**
     * 得到时间 HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public static String getTime(long timeStamp) {
        String           time  = null;
        SimpleDateFormat sdf   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String           date  = sdf.format(timeStamp * 1000);
        String[]         split = date.split("\\s");
        if (split.length > 1) {
            time = split[1];
        }
        return time;
    }

    /**
     * 将一个时间转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time    = curTime - timeStamp;
        return time / 60 + "";
    }

    /**
     * 获得当前时间差
     *
     * @param timeStamp
     * @return
     */
    public static int nowCurrentTime(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time    = timeStamp - curTime;
        return (int) time;
    }


    /**
     * 获得规定时间戳的 Calendar
     * 用于 设置抽奖时间 (7天以后)
     *
     * @return Calendar
     */
    public static Calendar getCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        int yyyy = Integer.parseInt(new SimpleDateFormat("yyyy").format(time));
        int mm   = Integer.parseInt(new SimpleDateFormat("MM").format(time));
        int dd   = Integer.parseInt(new SimpleDateFormat("dd").format(time));
        calendar.set(
                yyyy,
                mm - 1,
                dd
        );
        return calendar;
    }


    /**
     * 获取当前的时 -->flag==true
     * 获取当前的分 -->flag==false
     *
     * @return
     */
    public static String nowCurrentPoint(boolean flag) {
        SimpleDateFormat sdf    = new SimpleDateFormat("HH:mm");
        String           date   = sdf.format(System.currentTimeMillis());
        String[]         split  = date.split(":");
        String           hour   = null;
        String           minute = null;
        if (flag) {
            if (split.length > 1) {
                hour = split[0];
                return hour;
            }
        } else {
            if (split.length > 1) {
                minute = split[1];
                return minute;
            }
        }
        return null;
    }

    /**
     * 将标准时间格式HH:mm:ss化为当前的时间差值
     *
     * @param str
     * @return
     */
    public static String StandardFormatStr(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d         = sdf.parse(str);
            long timeStamp = d.getTime();

            long curTime = System.currentTimeMillis() / (long) 1000;
            long time    = curTime - timeStamp / 1000;
            return time / 60 + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断Date是否在指定的时间之内
     *
     * @param minutes
     * @return
     */
    public static boolean isLessThanMinutes(Date date, int minutes) {
        Date currentDate = new Date();
        if (date.getTime() + (minutes * 60 * 1000) >= currentDate.getTime()) {
            return true;
        }
        return false;
    }

    /**
     * 判断Date是否在指定的时间之内
     *
     * @param minutes
     * @return
     */
    public static boolean isLessThanMinutes(String dateStr, int minutes) {
        Date date        = getDate(DATE_TIME_1, dateStr);
        Date currentDate = new Date();
        if (date.getTime() + (minutes * 60 * 1000) >= currentDate.getTime()) {
            return true;
        }
        return false;
    }

    /**
     * 获取两个Date相差的天数
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static long getDateMargin(Date beginDate, Date endDate) {
        long margin = 0;
        margin = endDate.getTime() - beginDate.getTime();
        margin = margin / (1000 * 60 * 60 * 24);
        return margin;
    }

}
