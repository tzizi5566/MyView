package com.kop.myview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 功    能: 带刻度进度条
 * 创 建 人: KOP
 * 创建日期: 2017/5/26 14:39
 */
public class ProgressBar3 extends View {

  private static final float MAX_PROGRESS = 100.0F;

  private int mWidth, mHeight;
  private float mRadius;
  private float mPercent;
  private float mArcWidth;
  private float mDegrees;
  private int[] mGradientColors = { Color.GREEN, Color.YELLOW, Color.RED };
  private float[] mGradientPoints = { 0.0F, 0.3F, 1.0F };

  private Paint mArcPaint1, mArcPaint2, mScalePaint;

  private RectF mRectF;

  private ValueAnimator mAnimator;

  public ProgressBar3(Context context) {
    super(context);
  }

  public ProgressBar3(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mArcWidth = 26F;
    mDegrees = 5F;
    initPaint();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(measureWidth(widthMeasureSpec), measuredHeight(heightMeasureSpec));
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mWidth = w;
    mHeight = h;
    mRadius = Math.min(mWidth / 2, mHeight / 2) * 0.8f;
    mRectF = new RectF(mWidth / 2 - mRadius, mHeight / 2 - mRadius, mWidth / 2 + mRadius,
        mHeight / 2 + mRadius);

    SweepGradient sweepGradient =
        new SweepGradient(mWidth / 2, mHeight / 2, mGradientColors, mGradientPoints);
    mArcPaint2.setShader(sweepGradient);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.rotate(150, mWidth / 2, mHeight / 2);
    canvas.drawArc(mRectF, 0, 240, false, mArcPaint1);

    float currentAngle = 240 * mPercent;
    canvas.drawArc(mRectF, 0, currentAngle, false, mArcPaint2);

    int total = (int) (240 / mDegrees);
    for (int i = 0; i <= total; i++) {
      canvas.drawLine(mWidth / 2 + mRadius - mArcWidth / 2, mHeight / 2,
          mWidth / 2 + mRadius + mArcWidth / 2, mHeight / 2, mScalePaint);
      canvas.rotate(mDegrees, mWidth / 2, mHeight / 2);
    }
  }

  private int measureWidth(int widthMeasureSpec) {
    int result;
    int specMode = MeasureSpec.getMode(widthMeasureSpec);
    int specSize = MeasureSpec.getSize(widthMeasureSpec);
    if (specMode == MeasureSpec.EXACTLY) {
      result = specSize;
    } else {
      result = 100;
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
      result = 100;
      if (specMode == MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize);
      }
    }
    return result;
  }

  private void initPaint() {
    mArcPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    mArcPaint1.setColor(Color.GRAY);
    mArcPaint1.setStyle(Paint.Style.STROKE);
    mArcPaint1.setStrokeWidth(mArcWidth);
    //mArcPaint1.setStrokeCap(Paint.Cap.BUTT);

    mArcPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    mArcPaint2.setColor(Color.GREEN);
    mArcPaint2.setStyle(Paint.Style.STROKE);
    mArcPaint2.setStrokeWidth(mArcWidth);
    //mArcPaint2.setStrokeCap(Paint.Cap.BUTT);

    mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mScalePaint.setColor(Color.WHITE);
    mScalePaint.setStrokeWidth(6f);
  }

  public void setProgress(float progress) {
    if (progress > MAX_PROGRESS) {
      stopAnim();
      return;
    }

    float start = mPercent;
    float end = progress / MAX_PROGRESS;

    startAnimator(start, end);
  }

  private void startAnimator(float start, float end) {
    mAnimator = ObjectAnimator.ofFloat(start, end);
    mAnimator.setDuration(600);
    mAnimator.setInterpolator(new LinearInterpolator());
    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mPercent = (float) animation.getAnimatedValue();
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
}
