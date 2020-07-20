package com.example.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViewUtils {
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    /**
     * 兼容以上方法
     * param: object 反射需要执行的类
     */
    private static void inject(ViewFinder finder, Object object) {
        injectField(finder, object);
        injectEvent(finder, object);
    }

    /**
     * 事件注入
     * @param finder
     * @param object
     */
    private static void injectEvent(ViewFinder finder, Object object) {
//        1. 获取类里面所有的方法
        Class<?> aClass = object.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
//        2. 获取OnClick中的value值
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(OnClick.class)) {

                int[] value = declaredMethod.getAnnotation(OnClick.class).value();
                for (int viewId : value) {
//                  3. findViewById找到View
                    View view = finder.findViewById(viewId);
                    boolean isCheckNet = false;
                    //                    扩展：检测网络
                    if (declaredMethod.isAnnotationPresent(CheckNet.class)) {
                        isCheckNet = true;
                    }
                    if (view != null) {
//                   4. setOnClickListener
//                   5. 反射执行方法
                        view.setOnClickListener(new DeclaredOnClickListener(declaredMethod, object, isCheckNet));
                    }
                }
            }
        }
    }

    /**
     * 注入属性
     * 1. 获取类所有属性
     * 2. 获取ViewById中的Value
     * 3. findViewById找到View
     * 4. 动态注入View
     * @param finder
     * @param object
     */
    private static void injectField(ViewFinder finder, Object object) {
        Class<?> clazz = object.getClass();
//        获取所有的属性
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
//                获取注解中的ID值->R.id.test_tv
                int viewId = viewById.value();
//                findViewById找到View
                View view = finder.findViewById(viewId);
                if (view != null) {
//                  动态注入找到的View
                    try {
                        field.setAccessible(true);
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {
        private Object object;
        private Method method;
        private boolean isCheckNet;
        public DeclaredOnClickListener(Method declaredMethod, Object object, boolean isCheckNet) {
            this.method = declaredMethod;
            this.object = object;
            this.isCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View view) {
//            是否需要检测网络
            if (isCheckNet) {
                if (!networkAvailable(view.getContext())) {
                    Toast.makeText(view.getContext(), "无网络连接", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
//            点击会调用该方法
            try {
                method.setAccessible(true);
                method.invoke(object, view);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    method.invoke(object, null);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static boolean networkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
