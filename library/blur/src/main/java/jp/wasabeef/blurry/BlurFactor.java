package jp.wasabeef.blurry;

import android.graphics.Color;

/**
 * Blur参数设置
 */
class BlurFactor {

  public static final int DEFAULT_RADIUS = 25;
  public static final int DEFAULT_SAMPLING = 1;

  public int width;
  public int height;
  public int radius = DEFAULT_RADIUS;
  //采样因子，采样因子越大越模糊
  public int sampling = DEFAULT_SAMPLING;
  //叠加颜色
  public int color = Color.TRANSPARENT;
}
