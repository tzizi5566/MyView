package com.kop.myview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 功    能: 旋转等待进度条
 * 创 建 人: KOP
 * 创建日期: 2017/5/28 16:23
 */
public class ProgressBar5 extends View {

  private static final String TAG = "ProgressBar5";

  private Point mCenterPoint;
  private float mRadius;
  private RectF mRectF;
  //private Paint mCirclePaint;
  private Paint mArcPaint;
  private int[] mGradientColors = { Color.GRAY, Color.TRANSPARENT };
  private float mStartAngle;
  private ValueAnimator mAnimator;

  public ProgressBar5(Context context) {
    super(context);
  }

  public ProgressBar5(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initPaint();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mCenterPoint = new Point();
    mCenterPoint.x = w / 2;
    mCenterPoint.y = h / 2;

    mRadius = Math.min(mCenterPoint.x, mCenterPoint.y) * 0.8F;

    mRectF = new RectF(mCenterPoint.x - mRadius, mCenterPoint.y - mRadius, mCenterPoint.x + mRadius,
        mCenterPoint.y + mRadius);

    //SweepGradient sweepGradient =
    //        new SweepGradient(mCenterPoint.x, mCenterPoint.y, Color.GRAY, Color.TRANSPARENT);
    //mArcPaint.setShader(sweepGradient);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCirclePaint);

    canvas.drawArc(mRectF, mStartAngle, 300, false, mArcPaint);
  }

  private int measureWidth(int widthMeasureSpec) {
    int result;
    int mode = MeasureSpec.getMode(widthMeasureSpec);
    int size = MeasureSpec.getSize(widthMeasureSpec);
    if (mode == MeasureSpec.EXACTLY) {
      result = size;
    } else {
      result = 200;
      if (mode == MeasureSpec.AT_MOST) {
        result = Math.min(result, size);
      }
    }
    return result;
  }

  private int measureHeight(int heightMeasureSpec) {
    int result;
    int mode = MeasureSpec.getMode(heightMeasureSpec);
    int size = MeasureSpec.getSize(heightMeasureSpec);
    if (mode == MeasureSpec.EXACTLY) {
      result = size;
    } else {
      result = 200;
      if (mode == MeasureSpec.AT_MOST) {
        result = Math.min(result, size);
      }
    }
    return result;
  }

  private void initPaint() {
    //mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //mCirclePaint.setColor(Color.TRANSPARENT);
    //mCirclePaint.setStyle(Paint.Style.FILL);

    mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mArcPaint.setStyle(Paint.Style.STROKE);
    mArcPaint.setStrokeWidth(10F);
    mArcPaint.setStrokeCap(Paint.Cap.ROUND);
  }

  public void startAnim(float start, float end) {
    mAnimator = ObjectAnimator.ofFloat(start, end);
    mAnimator.setDuration(1500);
    mAnimator.setRepeatCount(ValueAnimator.INFINITE);
    mAnimator.setInterpolator(new LinearInterpolator());
    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        mStartAngle = value * 360;
        invalidate();
      }
    });
    mAnimator.start();
  }
}
