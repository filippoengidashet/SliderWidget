package org.dalol.slidewidget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 5/8/2016
 */
public class SliderView extends View {

    private static final String TAG = SliderView.class.getSimpleName();

    private int mSliderColor;
    private int mSliderBackground;
    private Slider mSlider;
    private float mDx;
    private boolean mDragMode;
    private SliderListener mListener;
    private boolean mSliderAutoBackground;

    public SliderView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SliderView, defStyleAttr, 0);
        try {
            mSliderColor = array.getColor(R.styleable.SliderView_sliderColor, 0);
            mSliderBackground = array.getColor(R.styleable.SliderView_sliderBackground, 0);
            mSliderAutoBackground = array.getBoolean(R.styleable.SliderView_sliderAutoBackground, false);
            setBackgroundColor(mSliderBackground);
        } catch (Exception e) {

        } finally {
            array.recycle();
        }
        mSlider = new Slider(0, 0);
    }

    public void setSliderBackground(int sliderBackground) {
        mSliderBackground = sliderBackground;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = getMeasuredWidth();
        int height = getResources().getDimensionPixelOffset(R.dimen.slider_view_height);
        mSlider.setHeight(height);
        mSlider.setWidth(measuredWidth / 6);
        setMeasuredDimension(measuredWidth, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mSlider.isGripped((int) x, (int) event.getY())) {
                    setInDragMode(true);
                    mDx = x - mSlider.getX();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragMode()) {
                    mSlider.setX(x - mDx);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isDragMode()) {
                    animateSlider();
                    setInDragMode(false);
                }
                break;
        }
        return true;
    }

    private void notifySliderUpdate(SliderListener.SliderState state) {
        if (mListener != null) {
            mListener.onSlide(roundToPercent(mSlider.getX()), state);
        }
    }

    private float roundToPercent(float x) {
        return x * 100 / (getWidth() - mSlider.getSliderWidth());
    }

    private void animateSlider() {
        ObjectAnimator animator;

        float mX = mSlider.getX();

        if (isHalfWayThrough(mX)) {
            animator = ObjectAnimator.ofFloat(mSlider, "x", mSlider.getX(), 0);
        } else {
            animator = ObjectAnimator.ofFloat(mSlider, "x", mX, (getWidth() - mSlider.getSliderWidth()));
            animator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    notifySliderUpdate(SliderListener.SliderState.OPENED);
                }
            });
        }
        animator.setDuration(400L);
        animator.start();
    }

    private boolean isHalfWayThrough(float mX) {
        return mX < ((getWidth() / 2) - (mSlider.getSliderWidth() / 2));
    }

    public boolean isDragMode() {
        return mDragMode;
    }

    private void setInDragMode(boolean dragMode) {
        mDragMode = dragMode;
        Log.d(TAG, "DragMode -> " + dragMode);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        mSlider.drawSlider(canvas);
    }

    public void setListener(SliderListener listener) {
        mListener = listener;
    }

    private class Slider {

        private Paint mPaint;
        private int mWidth, mHeight;
        private float mX, mY;

        public Slider(float x, float y) {
            mX = x;
            mY = y;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mSliderColor);
        }

        public float getX() {
            return mX;
        }

        public void setX(float mX) {
            this.mX = mX;
            if (mSliderAutoBackground) {
                mPaint.setColor(ColorUtils.getHSVColor(getWidth(), 0, this.mX));
            }
            notifySliderUpdate(SliderListener.SliderState.CLOSED);
            invalidate();
        }

        public float getY() {
            return mY;
        }

        public void setY(float mY) {
            this.mY = mY;
            invalidate();
        }

        public void setHeight(int height) {
            mHeight = height;
        }

        public void setWidth(int width) {
            mWidth = width;
        }

        public int getSliderWidth() {
            return mWidth;
        }

        public boolean isGripped(int x, int y) {
            Rect rect = new Rect((int) mX, (int) mY, (int) (mX + mWidth), mHeight);
            return rect.intersect(x, y, x + mWidth, mHeight);
        }

        public void drawSlider(Canvas canvas) {
            canvas.drawRect(mX, mY, mX + mWidth, mHeight, mPaint);
        }
    }

    public interface SliderListener {

        enum SliderState {
            OPENED, CLOSED
        }

        void onSlide(float progress, SliderState state);
    }
}
