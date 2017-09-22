package com.kingz.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * author: King.Z <br>
 * date:  2017/7/9 22:33 <br>
 * description: 绘制的工具类 <br>
 *
 *   [对画布进行一些讲解]：
 *   new Canvas(Bitmap bitmap) 和canvas.setBitmap(bitMap)的作用:
 *    都是构造一个带有指定位图绘制的画布,且位图必须是可变的。
 *
 *   canvas.saveLayer(x, y, width, height, paint, flags);
 *    "新增"画布图层,前四个参数是位置参数，flags为保存标识，是一个注解的值，有：
 *      MATRIX_SAVE_FLAG = 0x01;        // 只保存图层的matrix矩阵,在restore（）被调用时恢复当前矩阵
 *      CLIP_SAVE_FLAG = 0x02;          // 只保存大小信息,在restore（）被调用时恢复当前clip区域
 *      HAS_ALPHA_LAYER_SAVE_FLAG = 0x04;   //表明该图层有透明度，和下面的标识冲突，都设置时以下面的标志为准。 该层需要每像素的alpha值
 *      FULL_COLOR_LAYER_SAVE_FLAG = 0x08;  //完全保留该图层颜色（和上一图层合并时，清空上一图层的重叠区域，保留该图层的颜色）。 该层需要每颜色分量为8位
 *      CLIP_TO_LAYER_SAVE_FLAG = 0x10      //创建图层时，会把canvas（所有图层）裁剪到参数指定的范围，
 *      									如果省略这个flag将导致图层开销巨大（实际上图层没有裁剪，与原图层一样大）。
 *      									增对该图层边界进行Clip裁剪
 *      ALL_SAVE_FLAG = 0x1F;               //保存所有信息,restore（）被调用时恢复所有内容
 *  XUL中，黄大师绘制带影印效果的bitmap的时候，采用的是HAS_ALPHA_LAYER_SAVE_FLAG的模式。
 * 	注：参数讲解，博客：http://blog.csdn.net/cquwentao/article/details/51423371
 *
 *
 */
public class DrawUtils {

    /**
     * 添加投影效果
     * @param canvas        画布对象
     * @param shadowSize    投影大小
     * @param shadowColor   投影颜色
     * @param src           原始图片
     * @return  加上投影后的Bitmap
     */
    public Bitmap addShadowEffects(Canvas canvas, int shadowSize, int shadowColor, Bitmap src){

        return src;
    }

    public static void offsetRect(Rect rc, int offX, int offY) {
		rc.left += offX;
		rc.right += offX;
		rc.top += offY;
		rc.bottom += offY;
	}

	public static void offsetRect(RectF rc, float offX, float offY) {
		rc.left += offX;
		rc.right += offX;
		rc.top += offY;
		rc.bottom += offY;
	}

}
