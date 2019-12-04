package com.kingz.config;

import java.util.HashMap;

/**
 * author：KingZ
 * date：2019/12/4
 * description：
 */

public class DemoSample extends Sample {
    public final String clazz;
    private HashMap<String,Class> cacheClass = new HashMap<>();

    public DemoSample(String name,String clazz) {
        super(name);
        this.clazz = clazz;
    }

    @Override
    protected Class getDemoClass() {
        if(cacheClass.containsKey(clazz)){
            return cacheClass.get(clazz);
        }
        try {
            Class<?> clazzObj = Class.forName(clazz);
            cacheClass.put(clazz,clazzObj);
            return clazzObj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
