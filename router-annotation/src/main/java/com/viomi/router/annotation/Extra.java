package com.viomi.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.annotation
 * @ClassName: Extra
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 15:38
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 15:38
 * @UpdateRemark:
 * @Version: 1.0
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface Extra {

    String name() default "";

}
