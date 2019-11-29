package com.viomi.router.core.template;

import com.viomi.router.annotation.modle.RouteMeta;

import java.util.Map;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.template
 * @ClassName: IRouteGroup
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 15:20
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 15:20
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IRouteGroup {

    void loadInto(Map<String, RouteMeta> atlas);

}
