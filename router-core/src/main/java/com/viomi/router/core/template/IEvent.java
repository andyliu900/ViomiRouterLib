package com.viomi.router.core.template;

import com.viomi.router.annotation.modle.EventMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.router.core.template
 * @ClassName: IEvent
 * @Description:
 * @Author: randysu
 * @CreateDate: 2020-02-20 17:27
 * @UpdateUser:
 * @UpdateDate: 2020-02-20 17:27
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IEvent {

    /**
     * key为moduleName，value为EventMeta集合
     *
     * @param map
     */
    void loadInto(HashMap<String, HashMap<String, EventMeta>> map);

}
