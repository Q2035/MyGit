package com.example.essayjoke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alipay.euler.andfix.patch.PatchManager;
import com.example.baselibrary.ExceptionCrashHandler;

/**
 * @Author Q
 * @Date 2020/7/21 4:37 PM
 * @Description
 */
public class BaseApplication extends Application {

    public static PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
//        设置全局异常捕捉
        ExceptionCrashHandler.getInstance().init(this);

//        初始化阿里的热修复
        mPatchManager = new PatchManager(this);
//        初始化版本，获取当前应用的版本
        PackageManager manager = this.getPackageManager();
        try {
            manager.getPackageInfo(this.getPackageName(), 0);
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            mPatchManager.init(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        加载之前的patch包
        mPatchManager.loadPatch();
    }
}
