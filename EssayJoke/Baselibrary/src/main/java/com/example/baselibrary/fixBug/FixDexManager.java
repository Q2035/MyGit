package com.example.baselibrary.fixBug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * @Author Q
 * @Date 2020/7/22 9:18 AM
 * @Description
 */
public class FixDexManager {

    private Context context;

    private File dexDir;

    private static final String TAG = "FixDexManager";

    public FixDexManager(Context context) {
        this.context = context;
//        获取应用可以访问的dex目录
        this.dexDir = context.getDir("odex",Context.MODE_PRIVATE);
    }

    /**
     * 修复dex包
     * @param fixDexPath
     */
    public void fixDex(String fixDexPath) throws Exception {
//        1. 先获取已经运行的dexElement
        ClassLoader classLoader = context.getClassLoader();

        Object applicationDexElements = getDexElementsByClassLoader(classLoader);
//        2. 获取下载好的补丁的dexElement
//        2.1 移动到系统能够访问的dex目录下
        File srcFile = new File(fixDexPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }
        File targetFile = new File(dexDir, srcFile.getName());
        if (targetFile.exists()) {
            Log.d(TAG, "fixDex: 已加载");

            return;
        }

        copyFile(srcFile, targetFile);
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(targetFile);

        File optimizedDirectory = new File(dexDir, "odex");
        if (!optimizedDirectory.exists()) {
            optimizedDirectory.mkdirs();
        }

//        修复
//        2.2 ClassLoader读取fixDex路径
        for (File fixDexFile : fixDexFiles) {
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),//dex路径
                    optimizedDirectory,//解压路径
                    null,//libraryPath:so文件位置
                    classLoader//父classLoader
                    );
            Object fixDexElements = getDexElementsByClassLoader(classLoader);
//        3. 将补丁的dexElement插入到运行的dexElement前面
//            classLoader数组合并fixDexElements数组

            applicationDexElements = combineArray(fixDexElements, applicationDexElements);

        }

//        注入原来的类中 applicationDexElements
        injectDexElements(classLoader, applicationDexElements);

    }

    /**
     * 将dexElements注入到classLoader中
     * @param classLoader
     * @param applicationDexElements
     */
    private void injectDexElements(ClassLoader classLoader, Object applicationDexElements) throws Exception{
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, applicationDexElements);

    }

    private Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int i1 = 0; i1 < j; i1++) {
            if (i1 < i) {
                Array.set(result, i1, Array.get(arrayLhs, i1));
            } else {
                Array.set(result, i1, Array.get(arrayRhs, i1 - i));
            }
        }
        return result;
    }

    public void copyFile(File src, File dest) throws IOException {
        if (src.exists()) {
            FileChannel inChannel = null;
            FileChannel outChannel = null;
            try {
                if (!dest.exists()) {
                    dest.createNewFile();
                }
                inChannel = new FileInputStream(src).getChannel();
                outChannel = new FileOutputStream(dest).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
            }finally {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            }
        } else {
            Log.e(TAG, "copyFile: the file doesn't exist [" + src.getName() + "]");
        }
    }

    /**
     * 从ClassLoader中获取dexElements
     * @param classLoader
     * @return
     */
    private Object getDexElementsByClassLoader(ClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException {
//        先获取pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
//        pathList里面的dexElements
        Field dexElementsField = pathList.getClass().getField("dexElements");
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }
}
