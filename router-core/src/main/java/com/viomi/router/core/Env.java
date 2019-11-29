package com.viomi.router.core;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core
 * @ClassName: Env
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 11:07
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 11:07
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Env {

    public static final boolean DEBUG = BuildConfig.IS_DEBUG;

    public static final String VERSION = BuildConfig.VERSION;

    public static final String getVersionName() {
        return VERSION;
    }

    /**
     * apm日志输出TAG
     */
    public static final String ROUTER_TAG = "< viomi-router V" + getVersionName() + " >";

}
