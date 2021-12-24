package com.udofly.utils;

import android.util.Log;

import com.udofly.constains.UConstants;
import com.udofly.utils.time.DateUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Calendar;

/**
 * Describe:
 * Created by Gao Chunfa on 4/21/21.
 * Company: Hainan DaDi(Jinan) Network Technology Co. Ltd
 */
public class Logs {

    private static final int MaxDay = 3;

    public static String path() {
        return UConstants.LX_PATH_LOG;
    }

    public static void test(final String log) {
        Log.d("test:", log);
    }

    public static void tag(final String log) {
        Log.d("tag:", log);
    }

    public static void flag(final String log) {
        Log.d("flag:", log);
    }

    public static void socket(final String log) {
        Log.d("socket:", log);
    }

    public static void msk(final String log) {
        Log.d("msk:", log);
    }

    public static synchronized void log(final String data, String type) {

        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String dir = path() + "/" + DateUtil.getDate(DateUtil.YYYY_MM_DD, System.currentTimeMillis()) + "/";
                mkdir(dir);
                String fileName = type + "~" + DateUtil.getDate(DateUtil.DATE_TIME_3, System.currentTimeMillis()) + ".txt";
                writeFile(dir, fileName, DateUtil.getDate(DateUtil.DATE_TIME_1, System.currentTimeMillis()) + ": " + data);
                ThreadPool.remve(this);
            }
        });
    }

    public static synchronized void clear() {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                File   file    = new File(path());
                File[] subFile = file.listFiles();
                if (subFile == null) {
                    return;
                }
                for (int i = 0; i < subFile.length; i++) {
                    String filename = subFile[i].getName();
                    // 判断是否为文件夹
                    if (subFile[i].isDirectory()) {
                        boolean overdue = isOverdue(filename, MaxDay);
//                        tag("logs 日期: " + filename + ", overdue = " + overdue);
                        if (overdue) {
                            boolean delete = delete(subFile[i].getAbsoluteFile());
//                            tag("logs 删除: " + filename + ", delete = " + delete);
                        }
                    } else {
                        boolean delete = subFile[i].delete();
//                        tag("logs 删除: " + filename + ", delete = " + delete);
                    }
                }
                ThreadPool.remve(this);
            }
        });
    }

    public static boolean isOverdue(String date, int days) {
        return DateUtil.getTimes(DateUtil.YYYY_MM_DD, date) < getBeforeDays(days);
    }

    private synchronized static void writeFile(String dir, String fileName, String text) {
        try {
            File basedir = new File(dir);
            if (!basedir.exists()) {
                basedir.mkdirs();
            }
            File file = new File(dir, fileName);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    return;
                }
            }

            //如果为追加则在原来的基础上继续写文件
            RandomAccessFile fos = new RandomAccessFile(file, "rw");
            fos.seek(file.length());
//            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath(), true);
            fos.write(text.getBytes());
            fos.write("\r\n".getBytes());//写入换行符
            fos.close();
        } catch (Exception e) {
//            Logs.tag("logs write error " + e.getMessage());
        }
    }

    /**
     * @param path 文件夹路径
     */
    public static void mkdir(String path) {
        File file = new File(path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static boolean delete(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                delete(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
        return true;
    }

    public static long getBeforeDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1 * days);
        return calendar.getTime().getTime();
    }

}
