package com.kop.myview.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * 功    能: View工具类
 * 创 建 人: KOP
 * 创建日期: 2017/5/23 15:43
 */
public class ViewUtils {

  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  public static int sp2px(Context context, float sp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
        context.getResources().getDisplayMetrics());
  }

  public static Bitmap drawableToBitamp(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      BitmapDrawable bd = (BitmapDrawable) drawable;
      return bd.getBitmap();
    }
    int w = drawable.getIntrinsicWidth();
    int h = drawable.getIntrinsicHeight();
    Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, w, h);
    drawable.draw(canvas);
    return bitmap;
  }
}
