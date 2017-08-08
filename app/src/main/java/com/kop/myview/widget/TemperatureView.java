package com.kop.myview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.kop.myview.R;
import com.kop.myview.util.ViewUtils;

/**
 * 功    能: 温度旋转按钮
 * 创 建 人: KOP
 * 创建日期: 2017/6/1 14:47
 */
public class TemperatureView extends View {

  private int mWidth, mHeight;
  private float mScaleRadius;
  //四格（每格4.5度）代表温度1度
  private int mAngleRate = 4;
  //当前按钮旋转的角度
  private float mRotateAngle;

  private Paint mScalePaint;
  private Paint mTextPaint;
  private Paint mBitmapPaint;
  private Paint mTempTextPaint;

  private int mMinTemp, mMaxTemp, mTemp;
  private float mDegrees;

  private RectF mArcRectF;
  private Rect mTextRect;

  private Bitmap mButtonBitmap, mButtonShadowBitmap;

  private Matrix mButtonMatrix, mButtonShadowMatrix;
  private float mScale;
  private float mShadoScale;

  public TemperatureView(Context context) {
    super(context);
  }

  public TemperatureView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mMinTemp = 15;
    mMaxTemp = 30;
    mTemp = 15;
    initPaint();

    mButtonBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.btn_rotate);
    mButtonShadowBitmap =
        BitmapFactory.decodeResource(getResources(), R.drawable.btn_rotate_shadow);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mWidth = w;
    mHeight = h;

    mScaleRadius = Math.min(w / 2, h / 2) * 0.8F;

    float arcRadius = mScaleRadius * 0.9F;
    mArcRectF = new RectF(-arcRadius, -arcRadius, arcRadius, arcRadius);
    mTextRect = new Rect();

    mButtonMatrix = new Matrix();
    mButtonShadowMatrix = new Matrix();

    float width = mScaleRadius * 0.7F / (mButtonBitmap.getWidth() / 2);
    float height = mScaleRadius * 0.7F / (mButtonBitmap.getHeight() / 2);
    mScale = Math.min(width, height);

    float shadowWidth = mScaleRadius * 0.7F / (mButtonShadowBitmap.getWidth() / 2);
    float shadowHeight = mScaleRadius * 0.7F / (mButtonShadowBitmap.getHeight() / 2);
    mShadoScale = Math.min(shadowWidth, shadowHeight);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.translate(mWidth / 2, mHeight / 2);

    //刻度盘
    canvas.save();
    canvas.rotate(137);
    mScalePaint.setColor(Color.parseColor("#3CB7EA"));
    for (int i = 0; i < 60; i++) {
      canvas.drawLine(mScaleRadius, 0, mScaleRadius + 14, 0, mScalePaint);
      canvas.rotate(4.5F);
    }
    canvas.restore();

    canvas.save();
    canvas.rotate(137);
    mScalePaint.setColor(Color.parseColor("#E37364"));
    for (int i = 0; i < (mTemp - mMinTemp) * mAngleRate; i++) {
      canvas.drawLine(mScaleRadius, 0, mScaleRadius + 14, 0, mScalePaint);
      canvas.rotate(4.5F);
    }
    canvas.restore();

    //刻度盘下的圆弧
    canvas.save();
    canvas.rotate(137);
    mScalePaint.setColor(Color.parseColor("#3CB7EA"));
    canvas.drawArc(mArcRectF, 0, 265, false, mScalePaint);
    canvas.restore();

    //标题与温度标识
    canvas.save();
    String title = "最高温度设置";
    float textWidth = mTextPaint.measureText(title);
    canvas.drawText(title, 0 - textWidth / 2, mArcRectF.bottom, mTextPaint);

    String minTemp = mMinTemp + "";
    String maxTemp = mMaxTemp + "";
    canvas.rotate(47.5F);
    mTextPaint.getTextBounds(minTemp, 0, minTemp.length(), mTextRect);
    canvas.drawText(minTemp, 0 - mTextRect.width() / 2, mScaleRadius + 20 + mTextRect.height(),
        mTextPaint);
    canvas.rotate(-95F);
    mTextPaint.getTextBounds(maxTemp, 0, maxTemp.length(), mTextRect);
    canvas.drawText(maxTemp, 0 - mTextRect.width() / 2, mScaleRadius + 20 + mTextRect.height(),
        mTextPaint);
    canvas.restore();

    //旋转按钮
    canvas.save();
    int shadowWidth = mButtonShadowBitmap.getWidth();
    int shadowHeight = mButtonShadowBitmap.getHeight();
    mButtonShadowMatrix.setTranslate(-shadowWidth / 2, -shadowHeight / 2);
    mButtonShadowMatrix.postScale(mShadoScale, mShadoScale);
    canvas.drawBitmap(mButtonShadowBitmap, mButtonShadowMatrix, mBitmapPaint);

    int buttonWidth = mButtonBitmap.getWidth();
    int buttonHeight = mButtonBitmap.getHeight();
    mButtonMatrix.setTranslate(-buttonWidth / 2, -buttonHeight / 2);
    mButtonMatrix.postRotate(47.5F + mRotateAngle);
    mButtonMatrix.postScale(mScale, mScale);
    canvas.drawBitmap(mButtonBitmap, mButtonMatrix, mBitmapPaint);
    canvas.restore();

    //温度
    canvas.save();
    String temp = mTemp + "°";
    float tempWidth = mTempTextPaint.measureText(temp);
    float tempHeight = (mTempTextPaint.ascent() + mTempTextPaint.descent()) / 2;
    canvas.drawText(temp, -tempWidth / 2, -tempHeight, mTempTextPaint);
    canvas.restore();
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
    mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mScalePaint.setStyle(Paint.Style.STROKE);
    mScalePaint.setStrokeWidth(4F);

    mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setTextSize(ViewUtils.sp2px(getContext(), 14F));
    mTextPaint.setColor(Color.BLACK);

    mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    mTempTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTempTextPaint.setTextSize(ViewUtils.sp2px(getContext(), 60F));
    mTempTextPaint.setColor(Color.parseColor("#E27A3F"));
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    float mX = event.getX();
    float mY = event.getY();

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mDegrees =
            (float) ((Math.toDegrees(Math.atan2(mY - mHeight / 2, mX - mWidth / 2)) + 360) % 360);
        break;

      case MotionEvent.ACTION_MOVE:
        float degrees =
            (float) ((Math.toDegrees(Math.atan2(mY - mHeight / 2, mX - mWidth / 2)) + 360) % 360);
        float angleIncreased = degrees - mDegrees;
        if (angleIncreased < -270) {
          angleIncreased = angleIncreased + 360;
        } else if (angleIncreased > 270) {
          angleIncreased = angleIncreased - 360;
        }
        initTemp(angleIncreased);
        mDegrees = degrees;
        invalidate();
        break;

      case MotionEvent.ACTION_UP:
        mRotateAngle = (float) ((mTemp - mMinTemp) * mAngleRate * 4.5);
        invalidate();
        break;
    }

    return true;
  }

  private void initTemp(float degrees) {
    mRotateAngle += degrees;
    if (mRotateAngle < 0) {
      mRotateAngle = 0;
    } else if (mRotateAngle > 270) {
      mRotateAngle = 270;
    }
    mTemp = (int) (mRotateAngle / 4.5) / mAngleRate + mMinTemp;
  }
}
