package com.gavinandre.customviewsamples.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gavinandre.customviewsamples.R;
import com.gavinandre.customviewsamples.view.ClickCircleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gavinandre on 17-2-17.
 */

public class CustomViewActivity1 extends AppCompatActivity {
    @BindView(R.id.xiuyixiu_button)
    ImageView mXiuyixiuButton;
    @BindView(R.id.cardview)
    CardView mCardview;
    @BindView(R.id.xiuyixiu_layout)
    RelativeLayout mXiuyixiuLayout;
    private Handler handler = new Handler();
    private List<ClickCircleView> clickCircleViewList;
    private ClickCircleView clickCircleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_1);
        ButterKnife.bind(this);
        initClickAnimator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(circleViewRunnable);
    }

    private void initClickAnimator() {
        clickCircleViewList = new ArrayList<>();
        mXiuyixiuButton.post(new Runnable() {
            @Override
            public void run() {
                clickCircleView = new ClickCircleView(CustomViewActivity1.this, mXiuyixiuButton.getWidth()
                        , mXiuyixiuButton.getHeight(), mXiuyixiuLayout.getMeasuredWidth(),
                        mXiuyixiuLayout.getMeasuredHeight());
                clickCircleView.setVisibility(View.VISIBLE);
                mXiuyixiuLayout.addView(clickCircleView);
                mXiuyixiuLayout.postInvalidate();
                // 加载动画
                final Animator anim = AnimatorInflater.loadAnimator(CustomViewActivity1.this,
                        R.animator.circle_scale_animator);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (anim != null) {
                            anim.start();//循环执行动画
                        }
                    }
                });
                anim.setTarget(clickCircleView);
                anim.start();
            }
        });
        mXiuyixiuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCircleView.setVisibility(View.GONE);//发射圆圈，即将循环动画View隐藏
                final ClickCircleView item = new ClickCircleView(CustomViewActivity1.this, mXiuyixiuButton.getWidth()
                        , mXiuyixiuButton.getHeight(), mXiuyixiuLayout.getWidth(),
                        mXiuyixiuLayout.getHeight());
                Animator spreadAnim = AnimatorInflater.loadAnimator(CustomViewActivity1.this,
                        R.animator.circle_spread_animator);
                spreadAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        item.setIsSpreadFlag(true);//动画执行完成，标记一下
                    }
                });
                spreadAnim.setTarget(item);
                spreadAnim.start();
                clickCircleViewList.add(item);
                mXiuyixiuLayout.addView(item);
                mXiuyixiuLayout.invalidate();
                handler.post(circleViewRunnable);
            }
        });
    }

    private Runnable circleViewRunnable = new Runnable() {
        public void run() {
            for (int i = 0; i < clickCircleViewList.size(); i++) {
                if (clickCircleViewList.get(i).isSpreadFlag()) {
                    mXiuyixiuLayout.removeView(clickCircleViewList.get(i));
                    clickCircleViewList.remove(i);
                    mXiuyixiuLayout.postInvalidate();
                }
            }
            if (clickCircleViewList.size() <= 0) {
                clickCircleView.setVisibility(View.VISIBLE);
            }
            handler.postDelayed(this, 100);
        }
    };

}
