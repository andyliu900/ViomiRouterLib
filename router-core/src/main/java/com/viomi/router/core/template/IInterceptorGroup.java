package com.viomi.router.core.template;

import java.util.Map;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.template
 * @ClassName: IInterceptorGroup
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 16:24
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 16:24
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IInterceptorGroup {

    /**
     * key为拦截器的优先级，value为拦截器
     *
     * @param map
     */
    void loadInto(Map<Integer, Class<? extends IInterceptor>> map);

}
