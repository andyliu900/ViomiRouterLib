package com.viomi.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.router.annotation
 * @ClassName: Event
 * @Description:
 * @Author: randysu
 * @CreateDate: 2020-02-18 16:58
 * @UpdateUser:
 * @UpdateDate: 2020-02-18 16:58
 * @UpdateRemark:
 * @Version: 1.0
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Event {

    /**
     * 事件的key
     *
     * @return
     */
    String key();

}
