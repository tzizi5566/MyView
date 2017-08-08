package com.kop.myview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.kop.myview.R;
import com.kop.myview.util.ViewUtils;

/**
 * 功    能: 仿ios APP安装进度条
 * 创 建 人: KOP
 * 创建日期: 2017/5/25 12:24
 */
public class ProgressBar2 extends View {

  private static final float MAX_PROGRESS = 100.0f;

  private Drawable mDrawable;
  private Bitmap mBitmap;
  private BitmapShader mBitmapShader;

  private Paint mBitmapPaint;
  private Paint mRingPaint;
  private Paint mArcPaint;
  private Paint mCirclePaint;
  private Paint mNormalPaint;

  private int mW, mH;
  private float mCenterX, mCenterY;
  private float mRingRadius;
  private float mCircleRadiusMax;
  private float mPercent;
  private RectF mRectF;
  private RectF mArcRectF;
  private int mAnimationState = 2;

  private ValueAnimator mAnimator;
  private Matrix mMatrix;

  public ProgressBar2(Context context) {
    super(context);
  }

  public ProgressBar2(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar2);
    mDrawable = typedArray.getDrawable(R.styleable.ProgressBar2_progressImage);
    typedArray.recycle();

    if (mDrawable != null) {
      mBitmap = ViewUtils.drawableToBitamp(mDrawable);
    }

    initData();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(measureWidth(widthMeasureSpec), measuredHeight(heightMeasureSpec));
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mW = w;
    mH = h;
    initData();
  }

  private void initData() {
    if (mBitmap == null) return;

    mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

    mCenterX = mW / 2.0f;
    mCenterY = mH / 2.0f;
    mRingRadius = mCenterX * 0.8f;
    mCircleRadiusMax = (float) Math.sqrt(mW * mW + mH * mH) / 2f;

    mMatrix = new Matrix();
    RectF src = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
    mRectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
    mMatrix.setRectToRect(src, mRectF, Matrix.ScaleToFit.CENTER);

    mArcRectF =
        new RectF((mCenterX - mRingRadius), (mCenterY - mRingRadius), mCenterX + mRingRadius,
            mCenterY + mRingRadius);

    initPaint();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mBitmap == null) return;

    if (mPercent == 0) {
      canvas.drawRoundRect(mRectF, 14, 14, mBitmapPaint);
    } else if (mPercent < 1.0) {
      canvas.drawRoundRect(mRectF, 14, 14, mBitmapPaint);
      canvas.drawCircle(mCenterX, mCenterY, mRingRadius, mRingPaint);
      float currentAngle = 360 * mPercent;
      canvas.drawArc(mArcRectF, -90, currentAngle, true, mArcPaint);
      if (mAnimationState == 2) {
        mAnimationState = 0;
      }
    } else if (mPercent >= 1.0 && mAnimationState == 0) {
      canvas.drawRoundRect(mRectF, 14, 14, mBitmapPaint);
      canvas.drawCircle(mCenterX, mCenterY, mRingRadius, mCirclePaint);
      mRingRadius += 5;
      mAnimationState = 1;
      postInvalidate();
    } else if (mAnimationState == 1) {
      canvas.drawRoundRect(mRectF, 14, 14, mBitmapPaint);
      canvas.drawCircle(mCenterX, mCenterY, mRingRadius, mCirclePaint);
      mRingRadius += 5;
      if (mRingRadius >= mCircleRadiusMax) {
        mAnimationState = 2;
        mRingRadius = mCenterX * 0.8f;
      }
      postInvalidateDelayed(20);
    } else {
      canvas.drawRoundRect(mRectF, 14, 14, mNormalPaint);
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

  public void setImage(Bitmap bitmap) {
    mBitmap = bitmap;
    initData();
  }

  private void initPaint() {
    mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    ColorMatrix cm = new ColorMatrix();
    cm.setScale(0.382f, 0.382f, 0.382f, 1f);
    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
    mBitmapPaint.setColorFilter(f);
    mBitmapShader.setLocalMatrix(mMatrix);
    mBitmapPaint.setShader(mBitmapShader);

    mRingPaint.setStyle(Paint.Style.STROKE);
    mRingPaint.setStrokeWidth(6f);
    mRingPaint.setShader(mBitmapShader);

    mArcPaint.setShader(mBitmapShader);

    mCirclePaint.setShader(mBitmapShader);

    mNormalPaint.setShader(mBitmapShader);
  }

  public void setProgress(float progress) {
    if (progress < 0) {
      throw new IllegalArgumentException("progress should not be less than 0");
    }
    if (progress > MAX_PROGRESS) {
      stopAnim();
      return;
    }

    float start = mPercent;
    float end = progress / MAX_PROGRESS;

    startAnim(start, end);
  }

  private void startAnim(float start, float end) {
    mAnimator = ObjectAnimator.ofFloat(start, end);
    mAnimator.setDuration(600);
    mAnimator.setInterpolator(new LinearInterpolator());//动画匀速
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
