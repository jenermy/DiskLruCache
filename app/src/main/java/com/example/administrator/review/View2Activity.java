package com.example.administrator.review;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class View2Activity extends AppCompatActivity {
    private Button scrollToBtn,scrollByBtn;
//    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view2);
        scrollByBtn = (Button)findViewById(R.id.scrollByBtn);
        scrollToBtn = (Button)findViewById(R.id.scrollToBtn);
//        layout = (LinearLayout)findViewById(R.id.layout);
        scrollToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                layout.scrollTo(-60,-100);
            }
        });
        scrollByBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                layout.scrollBy(-60,-100);
            }
        });

    }
}
