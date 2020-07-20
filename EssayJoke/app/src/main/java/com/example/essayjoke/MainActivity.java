package com.example.essayjoke;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselibrary.ioc.CheckNet;
import com.example.baselibrary.ioc.OnClick;
import com.example.baselibrary.ioc.ViewById;
import com.example.baselibrary.ioc.ViewUtils;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.test_tv)
    private TextView textView;

    private int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);
        textView.setText("HI");
    }

    /**
     * 没网就不执行该方法，而是打印Toast
     * @param view
     */
    @OnClick({R.id.test_tv})
    @CheckNet
    private void login(View view) {
        Toast.makeText(this, "Click1", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.test_iv})
    private void onClick(View view) {
        Toast.makeText(this, "Click2", Toast.LENGTH_SHORT).show();
    }
}