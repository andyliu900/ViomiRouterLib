package com.viomi.compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.compiler.utils
 * @ClassName: LogUtils
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-25 17:37
 * @UpdateUser:
 * @UpdateDate: 2019-11-25 17:37
 * @UpdateRemark:
 * @Version: 1.0
 */
public class LogUtils {

    private Messager messager;

    private LogUtils(Messager messager) {
        this.messager = messager;
    }

    public static LogUtils newLog(Messager messager) {
        return new LogUtils(messager);
    }

    public void i(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

}
