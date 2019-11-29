package com.viomi.router.core.utils;

import androidx.annotation.Nullable;

import java.util.TreeMap;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.utils
 * @ClassName: UniqueKeyTreeMap
 * @Description:  树查找更高效
 * @Author: randysu
 * @CreateDate: 2019-11-19 16:36
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 16:36
 * @UpdateRemark:
 * @Version: 1.0
 */
public class UniqueKeyTreeMap<K, V> extends TreeMap<K, V> {

    @Nullable
    @Override
    public V put(K key, V value) {
        if (containsKey(key)) {
            throw new RuntimeException("优先级为" + key + "的拦截器已经存在，不允许再次添加同级别的拦截器！");
        } else {
            return super.put(key, value);
        }
    }
}
