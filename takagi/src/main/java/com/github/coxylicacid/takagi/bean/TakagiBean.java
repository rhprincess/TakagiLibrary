package com.github.coxylicacid.takagi.bean;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.coxylicacid.takagi.R;
import com.github.coxylicacid.takagi.TakagiIndicator;
import com.github.coxylicacid.takagi.anim.ViewSizeChangeAnimation;

public class TakagiBean {
    private boolean show = false;
    private boolean isAnimationStarted = false;
    private LinearLayout container;
    private TakagiIndicator indicator;
    private TextView title;
    private TextView summary;
    private View contentView;
    private int height;
    private int id;

    public TakagiBean(final LinearLayout container) {
        this.container = container;
        indicator = container.findViewById(R.id.indicator);
        title = container.findViewById(android.R.id.title);
        summary = container.findViewById(android.R.id.summary);
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = container.getMeasuredHeight();
                getIndicator().setLayoutParams(new LinearLayout.LayoutParams(dp(25), height));
            }
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public LinearLayout getContainer() {
        return container;
    }

    public void setContainer(LinearLayout container) {
        this.container = container;
    }

    public TextView getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary.setText(summary);
    }

    public TakagiIndicator getIndicator() {
        return indicator;
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
        ((FrameLayout) container.findViewById(R.id.container)).addView(contentView);
    }

    public int getHeight() {
        return height == 0 ? container.getHeight() : height;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
        close();
    }

    public void expand() {
        if (height == 0) {
            container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    height = container.getMeasuredHeight();
                    expandPrivately();
                }
            });
        } else {
            expandPrivately();
        }
    }

    public void close() {
        if (height == 0) {
            container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    height = container.getMeasuredHeight();
                    closePrivately();
                }
            });
        } else {
            closePrivately();
        }
    }

    private void expandPrivately() {
        if (!isAnimationStarted) {
            container.clearAnimation();
            ViewSizeChangeAnimation animation = new ViewSizeChangeAnimation(container, height, container.getWidth());
            animation.setDuration(100);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isAnimationStarted = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    container.clearAnimation();
                    isAnimationStarted = false;
                    show = !show;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    container.clearAnimation();
                }
            });

            container.setAnimation(animation);
            container.startLayoutAnimation();
        }
    }

    private void closePrivately() {
        if (!isAnimationStarted) {
            container.clearAnimation();
            ViewSizeChangeAnimation animation = new ViewSizeChangeAnimation(container, dp(45), container.getWidth());
            animation.setDuration(100);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isAnimationStarted = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    container.clearAnimation();
                    isAnimationStarted = false;
                    show = !show;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    container.clearAnimation();
                }
            });

            container.setAnimation(animation);
            container.startLayoutAnimation();
        }
    }

    public int dp(float value) {
        float density = container.getContext().getResources().getDisplayMetrics().density;
        return value == 0 ? 0 : (int) Math.ceil(density * value);
    }
}
