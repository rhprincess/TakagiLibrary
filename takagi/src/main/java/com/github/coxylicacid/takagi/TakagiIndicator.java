package com.github.coxylicacid.takagi;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;

public class TakagiIndicator extends View {

    private Paint p;
    private Matrix matrix;
    private float thickness;
    private float circleRadius;
    private int indicatorColorActive;
    private int indicatorColorNormal;
    private int indicatorAlpha = 175;
    private Bitmap indicatorIcon;
    private float offset;
    private int circleTop = 0;
    private int circleBottom = 0;
    private boolean isStateOn = false;
    private boolean changingState = false;
    private float lastCircleRadius;
    private boolean onFocus = false;

    public static final int TOP = -1;
    public static final int CENTER = 0;
    public static final int BOTTOM = 1;

    private int defaultCircleGravity = TOP;

    private int defaultTakagiViewheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());

    public TakagiIndicator(Context context) {
        super(context);
        indicatorColorNormal = 0xFF959595;
        indicatorColorActive = 0xFF1871EF;
        offset = 0f;

        p = new Paint();
        p.setColor(indicatorColorNormal);
        p.setAntiAlias(true);
        thickness = dp(3);
        p.setStrokeWidth(thickness);
        circleRadius = dp(6);
        setMinimumWidth((int) circleRadius);
        setMinimumHeight((int) (circleRadius * 2));

        matrix = new Matrix();
    }

    public TakagiIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TakagiIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public TakagiIndicator(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TakagiIndicator);
        indicatorColorNormal = a.getColor(R.styleable.TakagiIndicator_indicatorNormalColor, 0xFF959595);
        indicatorColorActive = a.getColor(R.styleable.TakagiIndicator_indicatorActiveColor, 0xFF1871EF);
        thickness = a.getDimension(R.styleable.TakagiIndicator_indicatorLineThickness, dp(3));
        circleRadius = a.getDimension(R.styleable.TakagiIndicator_circleRadius, dp(6));
        defaultCircleGravity = a.getInt(R.styleable.TakagiIndicator_circleGravity, TOP);
        isStateOn = a.getInt(R.styleable.TakagiIndicator_state, 0) == 1;
        offset = a.getDimension(R.styleable.TakagiIndicator_offset, 0f);
        a.recycle();

        p = new Paint();
        p.setColor(indicatorColorNormal);
        p.setAntiAlias(true);
        p.setStrokeWidth(thickness);
        setMinimumWidth((int) circleRadius);
        setMinimumHeight((int) (circleRadius * 2));
        performGravity(defaultCircleGravity);

        setPadding(dp(5), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    private int rippleTime = 100;
    private int current = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //  绘制指示器线条
        p.setAlpha(indicatorAlpha);
        canvas.drawRect(circleRadius + getPaddingLeft(), 0,
                thickness + circleRadius + getPaddingLeft(),
                getHeight(), p);

        // 当聚集焦点时
        if (onFocus) {
            //  绘制指示器圆圈
            float dy = (float) ((getPaddingTop() * (circleTop == 0 ? 1 : 0)) + circleRadius + (getHeight() / 2) * circleTop);
            if (circleBottom == 1 && circleTop == 0)
                dy = getHeight() - circleRadius + getPaddingBottom();

            p.setAlpha(55);
            canvas.drawCircle(getPaddingLeft() + circleRadius + thickness / 2, dy + offset, circleRadius * 1.5f, p);
            p.setAlpha(125);
            canvas.drawCircle(getPaddingLeft() + circleRadius + thickness / 2, dy + offset, circleRadius * 0.75f, p);
        } else {
            //  绘制指示器圆圈
            p.setAlpha(255);
            float dy = (float) ((getPaddingTop() * (circleTop == 0 ? 1 : 0)) + circleRadius + (getHeight() / 2) * circleTop);
            if (circleBottom == 1 && circleTop == 0)
                dy = getHeight() - circleRadius + getPaddingBottom();

            canvas.drawCircle(getPaddingLeft() + circleRadius + thickness / 2, dy + offset, circleRadius, p);
        }

        // 如果指示器设置了图标，那么往圆圈里面画图标
        if (indicatorIcon != null) {
            float scaleW = circleRadius / indicatorIcon.getWidth();
            float scaleH = circleRadius / indicatorIcon.getHeight();
            matrix.postScale(scaleW, scaleH);
            matrix.postTranslate(0, offset);
            canvas.drawBitmap(indicatorIcon, matrix, p);
        }
    }

    public TakagiIndicator setIndicatorLineAlpha(int alpha) {
        this.indicatorAlpha = alpha;
        invalidate();
        return this;
    }

    public TakagiIndicator setIndicatorThickness(float thickness) {
        this.thickness = thickness;
        p.setStrokeWidth(thickness);
        invalidate();
        return this;
    }

    public float getIndicatorThickness() {
        return thickness;
    }

    public TakagiIndicator setIndicatorColorActive(@ColorInt int active) {
        this.indicatorColorActive = active;
        if (isStateOn) {
            p.setColor(active);
            invalidate();
        }
        return this;
    }

    public TakagiIndicator setIndicatorColorNormal(@ColorInt int normal) {
        this.indicatorColorNormal = normal;
        if (!isStateOn) {
            p.setColor(normal);
            invalidate();
        }
        return this;
    }

    public TakagiIndicator setIndicatorColor(@ColorInt int active, @ColorInt int normal) {
        this.indicatorColorActive = active;
        this.indicatorColorNormal = normal;
        p.setColor(isStateOn ? active : normal);
        invalidate();
        return this;
    }

    public int getIndicatorColorActive() {
        return indicatorColorActive;
    }

    public int getIndicatorColorNormal() {
        return indicatorColorNormal;
    }

    public TakagiIndicator setCircleRadius(float radius) {
        this.circleRadius = radius;
        invalidate();
        return this;
    }

    public float getCircleRadius() {
        return circleRadius;
    }

    public TakagiIndicator setCircleGravity(int gravity) {
        performGravity(gravity);
        invalidate();
        return this;
    }

    public TakagiIndicator setIndicatorIcon(Bitmap bitmap) {
        this.indicatorIcon = bitmap;
        invalidate();
        return this;
    }

    public TakagiIndicator setIndicatorIcon(Drawable drawable) {
        this.indicatorIcon = toBitmap(drawable);
        invalidate();
        return this;
    }

    public int getCircleGravity() {
        return this.defaultCircleGravity;
    }

    private void performGravity(int gravity) {
        switch (gravity) {
            case TOP:
                this.circleTop = 0;
                this.circleBottom = 0;
                break;
            case CENTER:
                this.circleTop = 1;
                this.circleBottom = 0;
                break;
            case BOTTOM:
                this.circleTop = 0;
                this.circleBottom = 1;
                break;
        }
        defaultCircleGravity = gravity;
    }

    public void focus(boolean f) {
        this.onFocus = f;
        invalidate();
    }

    public void changeState() {
        if (!changingState) {
            changingState = true;
            ValueAnimator animator = ValueAnimator.ofArgb(
                    isStateOn ? indicatorColorActive : indicatorColorNormal,
                    isStateOn ? indicatorColorNormal : indicatorColorActive).setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int color = (int) animation.getAnimatedValue();
                    if (color != (isStateOn ? indicatorColorNormal : indicatorColorActive)) {
                        p.setColor(color);
                    } else {
                        changingState = false;
                        isStateOn = !isStateOn;
                    }
                    invalidate();
                }
            });
            animator.start();
        }
    }

    public void finish() {
        if (!changingState) {
            changingState = true;
            clearAnimation();
            focus(false);
            ValueAnimator animator = ValueAnimator.ofArgb(indicatorColorNormal, indicatorColorActive).setDuration(150);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int color = (int) animation.getAnimatedValue();
                    if (color != indicatorColorActive) {
                        p.setColor(color);
                    } else {
                        changingState = false;
                        isStateOn = false;
                    }
                    invalidate();
                }
            });
            animator.start();
        }
    }

    public void redo() {
        if (!changingState) {
            changingState = true;
            clearAnimation();
            ValueAnimator animator = ValueAnimator.ofArgb(indicatorColorActive, indicatorColorNormal).setDuration(150);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int color = (int) animation.getAnimatedValue();
                    if (color != indicatorColorNormal) {
                        p.setColor(color);
                    } else {
                        changingState = false;
                        isStateOn = true;
                    }
                    invalidate();
                }
            });
            animator.start();
        }
    }

    public TakagiIndicator setStateOn(boolean stateOn) {
        isStateOn = stateOn;
        return this;
    }

    public boolean isStateOn() {
        return isStateOn;
    }

    private int dp(float value) {
        float density = getResources().getDisplayMetrics().density;
        return value == 0 ? 0 : (int) Math.ceil(density * value);
    }

    // Drawable To Bitmap
    private Bitmap toBitmap(Drawable drawable) {
        // 获取 drawable 长宽
        int width = drawable.getIntrinsicWidth();
        int heigh = drawable.getIntrinsicHeight();
        drawable.setBounds(0, 0, width, heigh);
        // 获取drawable的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 创建bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, heigh, config);
        // 创建bitmap画布
        Canvas canvas = new Canvas(bitmap);
        // 将drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
