package org.telegram.peifeng.utils;




import android.text.TextUtils;
import android.util.Log;

import org.telegram.messenger.ApplicationLoader;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/2.
 */

public class MyLog {
    public static String path = ApplicationLoader.applicationContext.getExternalFilesDir(null).getAbsolutePath();
    public static String name = path + File.separator + "peifeng.txt";

    public static String filePath="";
    public static void write2File(String str) {
        StackTraceElement ste[] = Thread.currentThread().getStackTrace();
        StackTraceElement heihei=ste[3];
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("["+heihei.getClassName()+"]").append("["+heihei.getMethodName()+" "+ heihei.getLineNumber()+"]");
//        stringBuffer.append("["+log.getClassName()+"]").append("["+ log.getLineNumber()+"]");
        Log.e("hello",str);
        BufferedWriter out = null;
        try {
            init();
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream( getFilePath(), true)));
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

            String dateString = formatter.format(currentTime);
//            out.write(dateString + " " +stringBuffer.toString()+" "+ str + "\r\n");
            out.write(dateString + " " + str + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void writeException(String str) {
        StackTraceElement ste[] = Thread.currentThread().getStackTrace();
        StackTraceElement heihei=ste[3];
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("["+heihei.getClassName()+"]").append("["+heihei.getMethodName()+" "+ heihei.getLineNumber()+"]");
        Log.e("nimabi",str);
        BufferedWriter out = null;
        try {
            init();
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path + File.separator + "exception.text", true)));
            Date currentTime = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat(
                    " HH:mm:ss");
            String dateString = formatter.format(currentTime);
            out.write(dateString + " " + str + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void writeSwitchAccount(String str) {
        StackTraceElement ste[] = Thread.currentThread().getStackTrace();
        StackTraceElement heihei=ste[3];
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("["+heihei.getClassName()+"]").append("["+heihei.getMethodName()+" "+ heihei.getLineNumber()+"]");
        Log.e("jyh",str);
        BufferedWriter out = null;
        try {
            init();
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path + File.separator + "account.text", true)));
            Date currentTime = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            out.write(dateString + " " + str  + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public  static String getFilePath(){
        if (!TextUtils.isEmpty(filePath)){
            return filePath;
        }
        Date currentTime = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        filePath=path + File.separator+dateString+"peifeng.txt";
        return filePath;
    }

    public static void writeHeartbeat(String str) {
        StackTraceElement ste[] = Thread.currentThread().getStackTrace();
        StackTraceElement heihei=ste[3];
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("["+heihei.getClassName()+"]").append("["+heihei.getMethodName()+" "+ heihei.getLineNumber()+"]");
//        stringBuffer.append("["+log.getClassName()+"]").append("["+ log.getLineNumber()+"]");
        Log.e("jyh",str);
        BufferedWriter out = null;
        try {
            init();
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream( path + File.separator + "Heartbeat.txt", true)));
            Date currentTime = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            out.write(dateString + " " + str + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private static void init() {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void dele() {
        File f = new File(name);
        if (f.exists()) {
            f.delete();
        }
    }
}
