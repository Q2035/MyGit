package com.example.baselibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Q
 * @Date 2020/7/21 4:12 PM
 * @Description 借助单例设计模式完成目的
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static volatile ExceptionCrashHandler mInstance;

    private static final String TAG = "ExceptionCrashHandler";

//    获取系统默认异常
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    public static ExceptionCrashHandler getInstance() {
        if (mInstance == null) {
//            并发
            synchronized (ExceptionCrashHandler.class) {
                mInstance = new ExceptionCrashHandler();
                return mInstance;
            }
        }
        return mInstance;
    }

//    获取应用信息
    private Context context;

    public void init(Context context){
        this.context = context;
//        设置全局异常类为this
        Thread.currentThread().setUncaughtExceptionHandler(this);
        Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
//        全局异常
        Log.e(TAG, "异常");
//        写入本地文件 throwable 当前版本Version 手机信息等
//        1. 奔溃详细信息
//        2. 应用信息 包名 版本号
//        3. 手机信息
        String crashFileName = saveInfoToSD(throwable);
        Log.e(TAG, "filename --> " + crashFileName);
        cacheCrashFile(crashFileName);
//        上传问题，并非在此处处理 ，而是等待应用重启后上传
//        让系统默认处理
        mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
    }

    private String saveInfoToSD(Throwable throwable) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();
//        1. 手机信息 应用信息
        for (Map.Entry<String ,String > entry : obtainSimpleInfo(context).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

//        2. 奔溃的详细信息
        sb.append(obtainExceptionInfo(throwable));
//        3. 保存文件
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//             getFilesDir获取手机应用目录，没有拿SD卡目录，6之后需要动态申请权限
            File dir = new File(context.getFilesDir() + File.separator + "crash");
//            删除之前的异常信息
            if (dir.exists()) {
                deleteDir(dir);
            }
            if (!dir.exists()){
                dir.mkdir();
            }
            fileName = dir.toString() + File.separator + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
            try(FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {

                fileOutputStream.write(sb.toString().getBytes());
                fileOutputStream.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 递归删除文件夹
     * @param dir
     * @return
     */
    private void deleteDir(File dir) {
        if (dir.exists()) {
            if (dir.isFile()) {
                dir.delete();
            } else {
                File[] files = dir.listFiles();
                if (files.length == 0) {
                    dir.delete();
                }
                for (File file : files) {
                    deleteDir(file);
                }
            }
        }
    }

    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBLE_INFO", getMobileInfo());
        return map;
    }

    /**
     * 获取手机信息
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
//                因为field中的内容为静态，可以传入null
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getAssignTime(String dateFormatStr) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        long currentTime = System.currentTimeMillis();
        return dateFormat.format(currentTime);
    }

    /**
     * 缓存奔溃日志文件
     * @param fileName
     */
    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = context.getSharedPreferences("crash", Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME", fileName).commit();
    }

    public File getCrashFile(){
        String crashFileName = context.getSharedPreferences("crash", Context.MODE_PRIVATE).getString("CRASH_FILE_NAME", "");
        return new File(crashFileName);
    }
}
