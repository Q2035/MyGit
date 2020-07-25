package com.example.essayjoke;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselibrary.ioc.OnClick;
import com.example.baselibrary.ioc.ViewById;
import com.example.baselibrary.ioc.ViewUtils;

public class HelloActivity extends AppCompatActivity {

    @ViewById(R.id.test_tv)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        ViewUtils.inject(this);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
//
//        textView.setOnClickListener(view->{
//        });

//        textView = findViewById(R.id.test_tv);
        textView.setText("NIHAO");

    }

    @OnClick(R.id.test_tv)
    public void onClick(){
        Toast.makeText(this, "HIHIHI", Toast.LENGTH_SHORT).show();
    }

}