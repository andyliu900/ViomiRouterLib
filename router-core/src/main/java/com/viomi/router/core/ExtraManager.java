package com.viomi.router.core;

import android.app.Activity;
import android.util.LruCache;

import com.viomi.router.core.template.IExtra;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core
 * @ClassName: ExtraManager
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-25 15:47
 * @UpdateUser:
 * @UpdateDate: 2019-11-25 15:47
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ExtraManager {

    public static final String SUFFIX_AUTOWIRED = "_Extra";

    public static ExtraManager instance;
    private LruCache<String, IExtra> classCache;

    public static ExtraManager getInstance() {
        if (instance == null) {
            synchronized (ExtraManager.class) {
                if (instance == null) {
                    instance = new ExtraManager();
                }
            }
        }
        return instance;
    }

    private ExtraManager() {
        classCache = new LruCache<>(66);
    }

    /**
     * 注入
     *
     * @param instance
     */
    public void loadExtras(Activity instance) {
        String className = instance.getClass().getName();
        IExtra iExtra = classCache.get(className);
        try {
            if (null == iExtra) {
                iExtra = (IExtra)Class.forName(instance.getClass().getName()
                + SUFFIX_AUTOWIRED).getConstructor().newInstance();
            }
            iExtra.loadExtra(instance);
            classCache.put(className, iExtra);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
