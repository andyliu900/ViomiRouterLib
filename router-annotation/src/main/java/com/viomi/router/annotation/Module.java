package com.viomi.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiModuleFramework
 * @Package: com.viomi.tools_module_annotation
 * @ClassName: Module
 * @Description:   指定Module入口类，并在其中执行init方法
 * @Author: randysu
 * @CreateDate: 2020/3/27 6:31 PM
 * @UpdateUser:
 * @UpdateDate: 2020/3/27 6:31 PM
 * @UpdateRemark:
 * @Version: 1.0
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Module {

}