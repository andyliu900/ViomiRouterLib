package com.viomi.router.core.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.viomi.router.core.thread.DefaultPoolExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.utils
 * @ClassName: ClassUtils
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 11:33
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 11:33
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ClassUtils {

    /**
     * 得到路由表类名
     *
     * @param context
     * @param packageName
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static Set<String> getFileNameByPackageName(Application context, String packageName)
            throws PackageManager.NameNotFoundException, InterruptedException {
        Set<String> classNames = new HashSet<>();
        List<String> paths = getSourcePaths(context);

        // 使用同步计数器判断处理
        CountDownLatch countDownLatch = new CountDownLatch(paths.size());
        ThreadPoolExecutor threadPoolExecutor = DefaultPoolExecutor.newDefaultPoolExecutor(paths.size());
        for (String path : paths) {
            threadPoolExecutor.execute(() -> {
                DexFile dexFile = null;
                try {
                    // 加载apk中的dex 并遍历获得所有包名为{packageName}的类
                    dexFile = new DexFile(path);
                    Enumeration<String> dexEntries = dexFile.entries();
                    while (dexEntries.hasMoreElements()) {
                        String className = dexEntries.nextElement();
                        if (!TextUtils.isEmpty(className) && className.startsWith(packageName)) {
                            classNames.add(className);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != dexFile) {
                        try {
                            dexFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //释放一个
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        return classNames;
    }

    /**
     * 获取程序所有apk
     * 如果是 instant run 会产生很多 splite apk
     *
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    private static List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir);
        // instant run
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null != applicationInfo.splitSourceDirs) {
                sourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
            }
        }

        return sourcePaths;
    }

}
