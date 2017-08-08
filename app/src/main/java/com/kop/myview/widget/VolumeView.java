package com.kop.myview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.kop.myview.util.ViewUtils;

/**
 * 功    能: //TODO
 * 创 建 人: KOP
 * 创建日期: 2017/6/6 17:13
 */
public class VolumeView extends View {

  private int mWidth, mHeight;

  private RectF mArcRectF;

  private Paint mArcPaint, mArcPaint2;
  private Paint mScalePaint;

  private Path mArcPath;

  private float mArcWidth;
  private float mDegrees;
  private float mRotateAngle;

  public VolumeView(Context context) {
    super(context);
  }

  public VolumeView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mArcWidth = ViewUtils.dip2px(getContext(), 26F);

    initPaint();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mWidth = w;
    mHeight = h;

    float radius = Math.min(w / 2, h / 2) * 0.8F;
    mArcRectF = new RectF(-radius, -radius, radius, radius);

    mArcPath.addArc(mArcRectF, 135, 270);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.translate(mWidth / 2, mHeight / 2);

    canvas.save();
    canvas.drawPath(mArcPath, mArcPaint);
    canvas.restore();

    canvas.save();
    canvas.drawArc(mArcRectF, 135, mRotateAngle, false, mArcPaint2);
    canvas.restore();

    canvas.save();
    canvas.rotate(45F);
    for (int i = 0; i <= 100; i++) {
      canvas.drawLine(0, mArcRectF.bottom - mArcWidth / 2, 0, mArcRectF.bottom + mArcWidth / 2,
          mScalePaint);
      canvas.rotate(2.7F);//360/270
    }
    canvas.restore();
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    float x = event.getX();
    float y = event.getY();

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mDegrees =
            (float) ((Math.toDegrees(Math.atan2(y - mHeight / 2, x - mWidth / 2)) + 360) % 360);
        break;

      case MotionEvent.ACTION_MOVE:
        float degrees =
            (float) ((Math.toDegrees(Math.atan2(y - mHeight / 2, x - mWidth / 2)) + 360) % 360);
        float angleIncreased = degrees - mDegrees;
        if (angleIncreased < -270) {
          angleIncreased = angleIncreased + 360;
        } else if (angleIncreased > 270) {
          angleIncreased = angleIncreased - 360;
        }
        initTemp(angleIncreased);
        mDegrees = degrees;
        break;

      case MotionEvent.ACTION_UP:

        break;
    }
    invalidate();
    return true;
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
    mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mArcPaint.setColor(Color.WHITE);
    mArcPaint.setStyle(Paint.Style.STROKE);
    mArcPaint.setStrokeWidth(mArcWidth);

    mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mScalePaint.setColor(Color.GRAY);
    mScalePaint.setStyle(Paint.Style.FILL);
    mScalePaint.setStrokeWidth(ViewUtils.dip2px(getContext(), 1F));

    mArcPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    mArcPaint2.setColor(Color.BLUE);
    mArcPaint2.setStyle(Paint.Style.STROKE);
    mArcPaint2.setStrokeWidth(mArcWidth);

    mArcPath = new Path();
  }

  private void initTemp(float degrees) {
    mRotateAngle += degrees;
    if (mRotateAngle < 0) {
      mRotateAngle = 0;
    } else if (mRotateAngle > 270) {
      mRotateAngle = 270;
    }
    //mTemp = (int) (mRotateAngle / 4.5) / mAngleRate + mMinTemp;
  }
}
