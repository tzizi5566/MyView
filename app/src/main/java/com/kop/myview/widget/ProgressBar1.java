package com.kop.myview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.kop.myview.R;
import com.kop.myview.util.ViewUtils;

/**
 * 功    能: 圆形进度条
 * 创 建 人: KOP
 * 创建日期: 2017/5/23 15:25
 */
public class ProgressBar1 extends View {

  private static float MAX_PROGRESS = 100.0f;
  private float mProgress;
  private int mValue;
  private int mProgressColor;
  private int mTextColor;
  private float mTextSize;
  private float mProgressWidth;

  private Paint mPaint;
  private Paint mTextPaint;
  private float mRadius;
  private int mX;
  private int mY;
  private RectF mRectF;
  private Rect mTextRect;
  private ValueAnimator mAnimator;

  public ProgressBar1(Context context) {
    super(context);
    init();
  }

  public ProgressBar1(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar1);
    mProgressWidth = typedArray.getDimension(R.styleable.ProgressBar1_progressWidth,
        ViewUtils.dip2px(context, 4f));
    mProgressColor = typedArray.getColor(R.styleable.ProgressBar1_progressColor,
        ContextCompat.getColor(context, R.color.colorPrimary));
    mTextColor = typedArray.getColor(R.styleable.ProgressBar1_progressTextColor, Color.BLACK);
    mTextSize = typedArray.getDimension(R.styleable.ProgressBar1_progressTextSize,
        ViewUtils.dip2px(context, 10f));
    typedArray.recycle();
    init();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(measureWidth(widthMeasureSpec), measuredHeight(heightMeasureSpec));
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mX = w / 2;
    mY = h / 2;
    mRadius = (float) (Math.min(mX, mY) * 0.8);
    mRectF = new RectF(mX - mRadius, mY - mRadius, mX + mRadius, mY + mRadius);
    mTextRect = new Rect();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    mPaint.setColor(Color.GRAY);
    canvas.drawCircle(mX, mY, mRadius, mPaint);

    mPaint.setColor(mProgressColor);
    float currentAngle = 360 * mProgress;
    canvas.drawArc(mRectF, 0, currentAngle, false, mPaint);

    String text = mValue + "%";
    mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);
    Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
    int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2
        - fontMetrics.top;//获得文字的基准线
    canvas.drawText(text, mX - mTextRect.width() / 2, baseline, mTextPaint);
  }

  private int measureWidth(int widthMeasureSpec) {
    int result;
    int specMode = MeasureSpec.getMode(widthMeasureSpec);
    int specSize = MeasureSpec.getSize(widthMeasureSpec);
    if (specMode == MeasureSpec.EXACTLY) {
      result = specSize;
    } else {
      result = 200;
      if (specMode == MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize);
      }
    }
    return result;
  }

  private int measuredHeight(int heightMeasureSpec) {
    int result;
    int specMode = MeasureSpec.getMode(heightMeasureSpec);
    int specSize = MeasureSpec.getSize(heightMeasureSpec);
    if (specMode == MeasureSpec.EXACTLY) {
      result = specSize;
    } else {
      result = 200;
      if (specMode == MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize);
      }
    }
    return result;
  }

  private void init() {
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(mProgressWidth);
    mPaint.setStrokeCap(Paint.Cap.ROUND);// 圆角

    mTextPaint = new Paint();
    mTextPaint.setAntiAlias(true);
    mTextPaint.setColor(mTextColor);
    mTextPaint.setTextSize(mTextSize);
    mTextPaint.setStyle(Paint.Style.FILL);
  }

  public void setProgress(float progress) {
    if (progress < 0) {
      throw new IllegalArgumentException("progress should not be less than 0");
    }
    if (progress > MAX_PROGRESS) {
      stopAnim();
      return;
    }

    float start = mProgress;
    float end = progress / MAX_PROGRESS;

    startAnim(start, end);
  }

  private void startAnim(float start, float end) {
    mAnimator = ObjectAnimator.ofFloat(start, end);
    mAnimator.setDuration(600);
    mAnimator.setInterpolator(new LinearInterpolator());//动画匀速
    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mProgress = (float) animation.getAnimatedValue();
        mValue = (int) (mProgress * MAX_PROGRESS);
        postInvalidate();
      }
    });
    if (!mAnimator.isRunning() && !mAnimator.isStarted()) {
      mAnimator.start();
    }
  }

  private void stopAnim() {
    if (mAnimator != null && mAnimator.isRunning()) {
      mAnimator.cancel();
    }
  }

  private static final String TAG = "ProgressBar1";
}
