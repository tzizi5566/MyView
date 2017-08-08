package com.kop.myview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.kop.myview.R;

/**
 * 功    能: 圆弧刻度温度进度条
 * 创 建 人: KOP
 * 创建日期: 2017/5/27 14:06
 */
public class ProgressBar4 extends View {

  private Point mCentrePoint;

  private float mBgRadius;
  private float mArcWidth;
  private float mScaleWidth;
  private float mSmallCircleRadius;

  private Paint mBgPaint;
  private Paint mArcPaint;
  private Paint mScalePaint;
  private Paint mSmallCirclePaint;
  private Paint mLeftPaint;
  private Paint mRightPaint;
  private Paint mTextPaint;

  private RectF mArcRectF;
  private RectF mScaleRectF;
  private Rect mTextRect;

  private Path mLeftPath;
  private Path mRightPath;

  private float mPercent;
  private int mValue;

  private ValueAnimator mAnimator;

  public ProgressBar4(Context context) {
    super(context);
  }

  public ProgressBar4(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mArcWidth = 30F;
    mScaleWidth = 4F;
    initPaint();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mCentrePoint = new Point();
    mCentrePoint.x = w / 2;
    mCentrePoint.y = h / 2;

    mBgRadius = Math.min(mCentrePoint.x, mCentrePoint.y) * 0.8F;

    mArcRectF = new RectF(mCentrePoint.x - mBgRadius + mArcWidth / 2,
        mCentrePoint.y - mBgRadius + mArcWidth / 2, mCentrePoint.x + mBgRadius - mArcWidth / 2,
        mCentrePoint.y + mBgRadius - mArcWidth / 2);
    mScaleRectF = new RectF(mCentrePoint.x - mBgRadius + mArcWidth + mScaleWidth / 2,
        mCentrePoint.y - mBgRadius + mArcWidth + mScaleWidth / 2,
        mCentrePoint.x + mBgRadius - mArcWidth - mScaleWidth / 2,
        mCentrePoint.y + mBgRadius - mArcWidth - mScaleWidth / 2);

    mSmallCircleRadius = Math.min(mCentrePoint.x, mCentrePoint.y) * 0.2F;
    float mPointRadius = Math.min(mCentrePoint.x, mCentrePoint.y) * 0.1F;

    RectF mPointRectF = new RectF(mCentrePoint.x - mPointRadius, mCentrePoint.y - mPointRadius,
        mCentrePoint.x + mPointRadius, mCentrePoint.y + mPointRadius);

    //init path
    mLeftPath.addArc(mPointRectF, 0, 360);
    mLeftPath.lineTo(mCentrePoint.x, mCentrePoint.y + mBgRadius - mArcWidth - 50);
    mLeftPath.lineTo(mCentrePoint.x - mPointRadius, mCentrePoint.y);

    mRightPath.addArc(mPointRectF, 0, -180);
    mRightPath.lineTo(mCentrePoint.x, mCentrePoint.y + mBgRadius - mArcWidth - 50);
    mRightPath.lineTo(mCentrePoint.x, mCentrePoint.y + mPointRadius);
    mRightPath.close();

    mTextRect = new Rect();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //背景圆
    canvas.drawCircle(mCentrePoint.x, mCentrePoint.y, mBgRadius, mBgPaint);

    //圆弧
    canvas.save();
    canvas.rotate(150, mCentrePoint.x, mCentrePoint.y);
    mArcPaint.setColor(Color.GREEN);
    mArcPaint.setStrokeCap(Paint.Cap.ROUND);
    canvas.drawArc(mArcRectF, 0, 120, false, mArcPaint);
    mArcPaint.setColor(Color.RED);
    canvas.drawArc(mArcRectF, 180, 60, false, mArcPaint);
    mArcPaint.setColor(Color.YELLOW);
    mArcPaint.setStrokeCap(Paint.Cap.BUTT);
    canvas.drawArc(mArcRectF, 120, 60, false, mArcPaint);
    canvas.restore();

    //进度条文字
    canvas.save();
    canvas.rotate(-60, mCentrePoint.x, mCentrePoint.y);
    mTextPaint.setColor(Color.BLACK);
    String content1 = "正常";
    mTextPaint.getTextBounds(content1, 0, content1.length(), mTextRect);
    canvas.drawText(content1, mCentrePoint.x - mTextRect.width() / 2,
        mCentrePoint.y - mBgRadius + mArcWidth / 2 + mTextRect.height() / 2, mTextPaint);

    canvas.rotate(90, mCentrePoint.x, mCentrePoint.y);
    String content2 = "预警";
    mTextPaint.getTextBounds(content2, 0, content2.length(), mTextRect);
    canvas.drawText(content2, mCentrePoint.x - mTextRect.width() / 2,
        mCentrePoint.y - mBgRadius + mArcWidth / 2 + mTextRect.height() / 2, mTextPaint);

    canvas.rotate(60, mCentrePoint.x, mCentrePoint.y);
    String content3 = "警告";
    mTextPaint.getTextBounds(content3, 0, content3.length(), mTextRect);
    canvas.drawText(content3, mCentrePoint.x - mTextRect.width() / 2,
        mCentrePoint.y - mBgRadius + mArcWidth / 2 + mTextRect.height() / 2, mTextPaint);
    canvas.restore();

    //刻度
    canvas.save();
    canvas.rotate(150, mCentrePoint.x, mCentrePoint.y);
    canvas.drawArc(mScaleRectF, 0, 240, false, mScalePaint);
    for (int i = 0; i <= 40; i++) {
      if (i % 5 == 0) {
        canvas.drawLine(mCentrePoint.x + mBgRadius - mArcWidth, mCentrePoint.y,
            mCentrePoint.x + mBgRadius - mArcWidth - 20F, mCentrePoint.y, mScalePaint);
      } else {
        canvas.drawLine(mCentrePoint.x + mBgRadius - mArcWidth, mCentrePoint.y,
            mCentrePoint.x + mBgRadius - mArcWidth - 10F, mCentrePoint.y, mScalePaint);
      }
      canvas.rotate(6, mCentrePoint.x, mCentrePoint.y);
    }
    canvas.restore();

    //刻度上的数字
    canvas.save();
    canvas.rotate(-120, mCentrePoint.x, mCentrePoint.y);
    mTextPaint.setColor(Color.BLACK);
    for (int i = 0; i <= 40; i++) {
      if (i % 5 == 0) {
        String num = i + "";
        mTextPaint.getTextBounds(num, 0, num.length(), mTextRect);
        canvas.drawText(num, mCentrePoint.x - mTextRect.width() / 2,
            mCentrePoint.y - mBgRadius + mArcWidth + mTextRect.height() + 22F, mTextPaint);
      }
      canvas.rotate(6, mCentrePoint.x, mCentrePoint.y);
    }
    canvas.restore();

    //小圆
    canvas.save();
    canvas.rotate(150, mCentrePoint.x, mCentrePoint.y);
    canvas.drawCircle(mCentrePoint.x, mCentrePoint.y, mSmallCircleRadius, mSmallCirclePaint);
    canvas.restore();

    //指针
    canvas.save();
    canvas.rotate(150, mCentrePoint.x, mCentrePoint.y);
    canvas.rotate(-90, mCentrePoint.x, mCentrePoint.y);
    float angle = mPercent * 240.0F;
    canvas.rotate(angle, mCentrePoint.x, mCentrePoint.y);

    canvas.drawPath(mLeftPath, mLeftPaint);
    canvas.drawPath(mRightPath, mRightPaint);
    canvas.restore();

    //指针内的小圆
    canvas.save();
    canvas.rotate(150, mCentrePoint.x, mCentrePoint.y);
    canvas.drawCircle(mCentrePoint.x, mCentrePoint.y, mSmallCircleRadius * 0.2F, mSmallCirclePaint);
    canvas.restore();

    //当前温度
    mTextPaint.setColor(Color.BLACK);
    String tempContent = "当前温度";
    mTextPaint.getTextBounds(tempContent, 0, tempContent.length(), mTextRect);
    canvas.drawText(tempContent, mCentrePoint.x - mTextRect.width() / 2,
        mCentrePoint.y + mBgRadius - mArcWidth - mTextRect.height() - 22F, mTextPaint);

    if (mValue <= 20) {
      mTextPaint.setColor(Color.GREEN);
    } else if (mValue > 20 && mValue <= 30) {
      mTextPaint.setColor(Color.YELLOW);
    } else {
      mTextPaint.setColor(Color.RED);
    }
    String temp = mValue + "℃";
    mTextPaint.getTextBounds(temp, 0, temp.length(), mTextRect);
    canvas.drawText(temp, mCentrePoint.x - mTextRect.width() / 2,
        mCentrePoint.y + mBgRadius - mArcWidth - mTextRect.height(), mTextPaint);
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
    mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mBgPaint.setColor(Color.WHITE);

    mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mArcPaint.setStyle(Paint.Style.STROKE);
    mArcPaint.setStrokeWidth(mArcWidth);

    mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mScalePaint.setColor(Color.BLACK);
    mScalePaint.setStyle(Paint.Style.STROKE);
    mScalePaint.setStrokeWidth(mScaleWidth);

    mSmallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mSmallCirclePaint.setColor(Color.GRAY);

    mLeftPath = new Path();
    mLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mLeftPaint.setColor(ContextCompat.getColor(getContext(), R.color.leftPointer));

    mRightPath = new Path();
    mRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mRightPaint.setColor(ContextCompat.getColor(getContext(), R.color.rightPointer));

    mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setTextSize(20F);
    mTextPaint.setStyle(Paint.Style.FILL);
    mTextPaint.setColor(Color.BLACK);
    mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
  }

  public void setPercent(float percent) {
    if (percent < 0) {
      percent = 0;
    } else if (percent > 40) {
      stopAnim();
      return;
    }
    float start = mPercent;
    float end = percent / 40.0F;

    startAnim(start, end);
  }

  private void startAnim(float start, float end) {
    mAnimator = ObjectAnimator.ofFloat(start, end);
    mAnimator.setDuration(600);
    mAnimator.setInterpolator(new LinearInterpolator());
    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mPercent = (float) animation.getAnimatedValue();
        mValue = (int) (40 * mPercent);
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
