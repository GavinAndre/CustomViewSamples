package com.gavinandre.customviewsamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gavinandre.customviewsamples.ui.activity.CustomViewActivity1;
import com.gavinandre.customviewsamples.ui.activity.CustomViewActivity2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.custom_view1)
    TextView mCustomView1;
    @BindView(R.id.custom_view2)
    TextView mCustomView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.custom_view1, R.id.custom_view2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_view1:
                Intent intent1 = new Intent(this, CustomViewActivity1.class);
                startActivity(intent1);
                break;
            case R.id.custom_view2:
                Intent intent2 = new Intent(this, CustomViewActivity2.class);
                startActivity(intent2);
                break;
        }
    }
}
