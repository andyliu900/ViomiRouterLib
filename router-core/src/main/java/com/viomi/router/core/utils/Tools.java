package com.viomi.router.core.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.utils
 * @ClassName: Tools
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-25 14:44
 * @UpdateUser:
 * @UpdateDate: 2019-11-25 14:44
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Tools {

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.equals("") || str.isEmpty();
    }

    /**
     * 判断集合是否为空
     *
     * @param coll
     * @return
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * 判断Map是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

}
