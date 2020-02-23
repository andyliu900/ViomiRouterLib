package com.viomi.router.core;

import java.util.HashMap;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.router.core
 * @ClassName: BaseEvent
 * @Description:  Event事件传递
 * @Author: randysu
 * @CreateDate: 2020-02-21 17:37
 * @UpdateUser:
 * @UpdateDate: 2020-02-21 17:37
 * @UpdateRemark:
 * @Version: 1.0
 */
public abstract class BaseEvent {

    protected HashMap<String, Object> params;

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
}
