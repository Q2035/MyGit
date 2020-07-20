package com.example.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baselibrary.ioc.ViewUtils;

/**
 * @Author Q
 * @Date 2020/7/20 9:06 PM
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置布局Layout
        setContentView();

        ViewUtils.inject(this);
//        初始化头部
        initTitle();
//        初始化界面
        initView();
//        初始化数据
        initData();
    }

    protected abstract void initTitle();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setContentView();

    /**
     * 启动新的Activity
     * @param clazz
     */
    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * findViewById
     * @param viewId
     * @param <T>
     * @return
     */
    protected <T extends View>T viewById(int viewId) {
        return (T)findViewById(viewId);
    }
}
