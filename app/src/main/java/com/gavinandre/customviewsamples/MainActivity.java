package com.gavinandre.customviewsamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gavinandre.customviewsamples.ui.activity.CustomView1;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.custom_view1)
    TextView mCustomView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.custom_view1)
    public void onClick() {
        Intent intent = new Intent(this, CustomView1.class);
        startActivity(intent);
    }
}
