package com.kingz.inject;

public @interface InjectView {
    public int value() default 0;
    public int id();
}