package com.udofly.constains;


import android.os.Environment;

/**
 * Describe: 常量
 * Created by udofly on 2021/12/24.
 */
public class UConstants {


    public static String LX_PATH       = Environment.getExternalStorageDirectory().getPath() + "/udofly/utils";
    public static String LX_PATH_LOG   = LX_PATH + "/log";//存放一些logcat文件
    public static String LX_PATH_TEMP  = LX_PATH + "/temp";//下载apk路径
    public static String LX_PATH_IMAGE = LX_PATH + "/image";

}
