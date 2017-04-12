package com.kingz.pages.photo;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/6 23:04
 * description:
 *  Bitmap深入学习
 */
public class OverallBitmap {

    private OverallBitmap overallBitmap = null;
    private Matrix mMatrix;

    public OverallBitmap getInstance(){
        if(overallBitmap ==null){
            overallBitmap = new OverallBitmap();
        }
        return overallBitmap;
    }

    public OverallBitmap() {
        mMatrix = new Matrix(); //每一种变化都包括set，pre，post三种，分别为设置、矩阵先乘、矩阵后乘。
    }

    public Bitmap setBitMapRotate(){
        mMatrix.setRotate(90);
        return null;
    }

}
