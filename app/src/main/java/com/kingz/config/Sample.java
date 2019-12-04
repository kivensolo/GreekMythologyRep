package com.kingz.config;

import android.content.Context;
import android.content.Intent;

/**
 * author：KingZ
 * date：2019/12/4
 * description：
 */
public abstract class Sample {
    public final String name;

    public Sample(String name) {
        this.name = name;
    }

    public Intent buildIntent(Context context) {
        Class targetClass = getDemoClass();
        return targetClass != null ? new Intent(context, targetClass) : null;
    }

    protected abstract Class getDemoClass();

}