package com.example.essayjoke;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.baselibrary.ExceptionCrashHandler;
import com.example.baselibrary.ioc.OnClick;
import com.example.baselibrary.ioc.ViewById;
import com.example.baselibrary.ioc.ViewUtils;
import com.example.framelibrary.BaseSkinActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends BaseSkinActivity {

    private int mPage = 0;

    private static final String TAG = "MainActivity";
    /****Hello World!****/
    @ViewById(R.id.test_tv)
    private Button mTestTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
//        初始化View
        viewById(R.id.test_tv).setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, 2 / 0 + "TEST", Toast.LENGTH_LONG).show();
        });
    }

    /**
     * 热修复可以在这里进行
     * 每次启动的时候后台获取差分包fix.apatch 然后修复本地bug
     */
    @Override
    protected void initData(){
//        获取前次的奔溃信息上传
//        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
//        if (crashFile.exists()) {
////            上传服务器
//            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(crashFile)))) {
//                char[] tempRead = new char[1024];
//                int len = bufferedReader.read(tempRead);
////                其实传送完成之后需要删除或者用其他手段阻止多次传输
////                这里就不上传了，而是直接打印
//                while (len != -1) {
//                    String readString = new String(tempRead);
//                    Log.w(TAG, readString);
//                    len = bufferedReader.read(tempRead);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        热启动
//        测试，直接获取本地内存卡中的fix.apatch
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
        if (fixFile.exists()) {
//            尝试修复Bug
            try {
                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

//    @OnClick(R.id.test_tv)
//    private void testTvClick() {
//        Toast.makeText(this,  "test", Toast.LENGTH_LONG).show();
//    }
}