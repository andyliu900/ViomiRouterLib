package com.viomi.router.core;

import com.viomi.router.annotation.modle.RouteMeta;
import com.viomi.router.core.template.IInterceptor;
import com.viomi.router.core.template.IRouteGroup;
import com.viomi.router.core.template.IService;
import com.viomi.router.core.utils.UniqueKeyTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core
 * @ClassName: Warehouse
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 16:29
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 16:29
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Warehouse {

    // root 映射表  保存分组信息
    static Map<String, Class<? extends IRouteGroup>> groupIndex = new HashMap<>();

    // group 映射表  保存组中的所有数据
    static Map<String, RouteMeta> routes = new HashMap<>();

    // service
    static Map<Class, IService> services = new HashMap<>();

    /**
     * 以键值对优先级的方式保存拦截对象
     */
    public static Map<Integer, Class<? extends IInterceptor>> interceptorsIndex = new UniqueKeyTreeMap();

    /**
     * 以集合方式保存所有拦截器对象
     */
    public static List<IInterceptor> interceptors = new ArrayList<>();

}
