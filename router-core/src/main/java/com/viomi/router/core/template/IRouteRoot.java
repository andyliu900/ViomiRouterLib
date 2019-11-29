package com.viomi.router.core.template;

import java.util.Map;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.template
 * @ClassName: IRouteRoot
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 15:19
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 15:19
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IRouteRoot {

    void loadInto(Map<String, Class<? extends IRouteGroup>> routes);

}
