package com.kingz.module.common.utils.ktx

import android.annotation.SuppressLint
import android.os.Build


/**
 * author：ZekeWang
 * date：2021/1/4
 * description：系统版本判断工具类
 */
@SuppressLint("ObsoleteSdkInt")
class SDKVersion {
    companion object {
        /** >=4.0 14  */
        fun hasICS(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
        }

        /**
         * >= 4.1 16
         *
         * @return
         */
        fun hasJellyBean(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
        }

        /** >= 4.2 17  */
        fun hasJellyBeanMr1(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
        }

        /** >= 4.3 18  */
        fun hasJellyBeanMr2(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
        }

        /** >=4.4 19  */
        fun hasKitkat(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        }
        /** >=4.4W 20  */
        fun hasKitkatW(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH
        }

        /** >=5.0 21 */
        fun afterLOLLIPOP(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }

        /** >=5.1 22 */
        fun afterLOLLIPOPMR1(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1
        }

        /** >=6.0 23 */
        fun afterMarshmallow(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        }

        /** >=7.0 24 */
        fun afterNougat(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        }

        /** >=7.1.1 25 */
        fun afterNougatMR1(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1
        }

        /** >=8.0 26 */
        fun afterOreo(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        }

        /** >=8.1 27 */
        fun afterOreoMR1(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1
        }

        /** >=9.0 28 */
        fun afterPie(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
        }

        fun getSDKVersionInt(): Int {
            return Build.VERSION.SDK_INT
        }
    }

}