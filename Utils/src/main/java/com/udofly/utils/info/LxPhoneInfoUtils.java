package com.udofly.utils.info;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import com.udofly.utils.JsonUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * describe：获取手机信息
 * author： Gao Chunfa.
 * time： 2017/4/5-11:18.
 */
public class LxPhoneInfoUtils {

    private static LxPhoneInfoUtils instance;

    private LxPhoneInfoUtils() {
    }

    public static LxPhoneInfoUtils get() {
        if (instance == null) {
            synchronized (JsonUtils.class) {
                if (instance == null) {
                    instance = new LxPhoneInfoUtils();
                }
            }
        }
        return instance;
    }

    /**
     * android.os.Build是安卓的一个类，获取安卓的一些系统信息
     android.os.Build.VERSION.RELEASE  获取当前系统版本号字符串：4.4.2
     android.os.Build.MODEL       获取手机型号
     android.os.Build.VERSION.SDK_INT   获取当前系统版本号数字：19
     android.os.Build.BRAND         手机定制商
     Build.BOARD // 主板
     Build.CPU_ABI // cpu指令集
     Build.DEVICE // 设备参数
     Build.DISPLAY // 显示屏参数
     Build.FINGERPRINT // 硬件名称
     Build.HOST
     Build.ID // 修订版本列表
     Build.MANUFACTURER // 硬件制造商
     Build.MODEL // 版本
     Build.PRODUCT // 手机制造商
     Build.TAGS // 描述build的标签
     Build.TIME
     Build.TYPE // builder类型
     Build.USER
     * @return
     */

    /**
     * 获取手机制造商
     * @return
     */
    public String getPhoneBrand() {
        try {
            return Build.BRAND;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取手机型号:华为 P20 pro
     * @return
     */
    public String getPhoneModel() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public String getPhoneRelease() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }


    private static final String FILE_MEMORY = "/proc/meminfo";
    private static final String FILE_CPU    = "/proc/cpuinfo";

    /**
     * 判断手机是否联网
     *
     * @param context
     * @return
     */
    public static boolean isConnectInternet(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isAvailable()) {

            return true;
        }
        return false;
    }

    /**
     * @param @param  myContext
     * @param @return
     * @return 判断用户网络链接类型 -1：没有网络 1：WIFI网络2：wap网络3：net网络
     * @throws
     * @Title: connNetType
     */
    public static int connNetType(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo() != null && "cmnet".equals(networkInfo.getExtraInfo().toLowerCase())) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }

    /**
     * @param @param  myContext
     * @param @param  packageName
     * @param @return
     * @return boolean
     * @throws
     * @Title: isAvilible
     * @Description: 断手机已安装某程序
     */
    public static boolean isAviliblePackage(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo>    pinfo          = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String>         pName          = new ArrayList<String>();// 用于存储所有已安装程序的包名
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    // 获取手机信息
    public static String getPhoneInfo(Context context) {
        String imei  = getIMEI(context, "");
        String mac   = getMacAddress(context);
        String sdk   = getSDKRelease();
        String model = getProductModel();
        String mem   = getTotalMem(context);
        String cpu   = getCpuInfo();
        return imei + "," + mac + "," + sdk + "," + model + "," + mem + ","
                + cpu;
    }

    // 获取SD卡的大小
    @SuppressWarnings("deprecation")
    public static String getSDCard() {
        long blocSize        = 0;
        long availableBlocks = 0;
        // 首先判断SD卡是否已经插好
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File   path   = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            blocSize = statfs.getBlockSize();
            availableBlocks = statfs.getBlockCount();
            return "" + (blocSize * availableBlocks);
        }
        return "0";
    }

    // 获取手机的IMEI码(国际移动用户识别码)
    public static String getIMEI(Context context, String nsrsbh) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        String mIMEI = manager.getDeviceId();
        return mIMEI == null ? (nsrsbh + "an") : mIMEI;
    }

    // 获取手机的MAC地址
    public static String getMacAddress(Context context) {
        String result = "";
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        return result;
    }

    // 获取SDK版本
    public static int getSysVersion() {
        return Build.VERSION.SDK_INT;
    }

    // 获取SDK release
    public static String getSDKRelease() {
        return Build.VERSION.RELEASE;
    }

    // 获取手机型号
    public static String getProductModel() {
        return Build.MODEL;
    }

    // 获取空闲内存(MB)
    public static long getFreeMem(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        MemoryInfo info = new MemoryInfo();
        am.getMemoryInfo(info);
        long free = info.availMem / 1024 / 1024;
        return free;
    }

    // 获取总内存(MB)
    public static String getTotalMem(Context context) {
        try {
            FileReader     fr    = new FileReader(FILE_MEMORY);
            BufferedReader br    = new BufferedReader(fr);
            String         text  = br.readLine();
            String[]       array = text.split("\\s+");
            br.close();
            return "" + Long.valueOf(array[1]) / 1024;
        } catch (Exception e) {
            //
            return "" + (-1);
        }
    }

    // 获取CPU信息
    public static String getCpuInfo() {
        String   str     = "";
        String[] cpu     = {"", ""}; // 1-cpu型号 //2-cpu频率
        String   cpuInfo = "";
        String[] arrayOfString;
        try {
            FileReader     fr                  = new FileReader(FILE_CPU);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str = localBufferedReader.readLine();
            arrayOfString = str.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpu[0] = cpu[0] + arrayOfString[i] + " ";
            }
            str = localBufferedReader.readLine();
            arrayOfString = str.split("\\s+");
            cpu[1] += arrayOfString[2];
            localBufferedReader.close();
            cpuInfo = cpu[0] + "-" + cpu[1];
        } catch (IOException e) {
            //
        }
        return cpuInfo;
    }
    // 获取CPU信息
    public static String getCpuName() {
        String   str     = "";
        String[] cpu     = {"", ""}; // 1-cpu型号 //2-cpu频率
        String   cpuInfo = "";
        String[] arrayOfString;
        try {
            FileReader     fr                  = new FileReader(FILE_CPU);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str = localBufferedReader.readLine();
            arrayOfString = str.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpu[0] = cpu[0] + arrayOfString[i] + " ";
            }

        } catch (IOException e) {
            //
        }
        return cpu[0];
    }

    /**
     * 获取系统SDK版本
     *
     * @return
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }


    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     */
    public static String getAPNType(Context context) {
        //结果返回值
        String netType = "no";
        try {
            //获取手机所有连接管理对象
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //NetworkInfo对象为空 则代表没有网络
            if (networkInfo == null) {
                return netType;
            }
            //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_WIFI) {
                //WIFI
                netType = "WIFI";
            } else if (nType == ConnectivityManager.TYPE_MOBILE) {
                int              nSubType         = networkInfo.getSubtype();
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
                if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                        && !telephonyManager.isNetworkRoaming()) {
                    netType = "4G";
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                        || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                        || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                        && !telephonyManager.isNetworkRoaming()) {
                    netType = "3G";
                    //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                        || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                        || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                        && !telephonyManager.isNetworkRoaming()) {
                    netType = "2G";
                } else {
                    netType = "2G";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netType;
    }

    /**
     * 获取运营商
     *
     * @return 中国移动/中国联通/中国电信/未知
     */
    public static String getProvider(Context context) {
        String provider = "未知";
        try {
            TelephonyManager                          telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String IMSI             = telephonyManager.getSubscriberId();
//            Log.d(TAG, "getProvider.IMSI:" + IMSI);
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager.getSimState()) {
                    String operator = telephonyManager.getSimOperator();
//                    Log.d(TAG, "getProvider.operator:" + operator);
                    if (operator != null) {
                        if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                            provider = "中国移动";
                        } else if (operator.equals("46001")) {
                            provider = "中国联通";
                        } else if (operator.equals("46003")) {
                            provider = "中国电信";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                    provider = "中国移动";
                } else if (IMSI.startsWith("46001")) {
                    provider = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    provider = "中国电信";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;

    }
}