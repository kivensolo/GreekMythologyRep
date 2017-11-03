package com.kingz.inject;

import android.app.Activity;
import android.view.View;
import com.kingz.utils.ZLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InjectViewService {
    public static void register(Activity activity) {
        Class<? extends Activity> object = activity.getClass(); //获取activity的字节码对象
        //getDeclaredMethod获取类自身的所有方法。不包括继承自父类的成员
        //getMethod获取包括父类的和自身的所有方法。
        Field[] fields = object.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (!View.class.isAssignableFrom(field.getType())) {   //判断该字段的类型是不是View或是View的子类
                continue;
            }
            InjectView injectView = field.getAnnotation(InjectView.class); //成员属性是否有ViewField注解
            if (injectView != null) {
                int id = injectView.id();
                if (id != 0) {
                    try {
                        Method method = object.getMethod("findViewById", int.class);
                        //调用方法，指定参数  获取到view对象
                        Object viewObject = method.invoke(activity, id);
                        field.set(activity, viewObject); //给成员属性赋值
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        ZLog.e("kingz", "==============e=" + e);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        ZLog.e("kingz", "==========e=" + e);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        ZLog.e("kingz", "=========e=" + e);
                    }
                }
            }
        }
    }
}
