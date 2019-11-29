package com.viomi.router.core.exception;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.exception
 * @ClassName: NoRouteFoundException
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 18:12
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 18:12
 * @UpdateRemark:
 * @Version: 1.0
 */
public class NoRouteFoundException extends RuntimeException {

    public NoRouteFoundException(String detailMessage) {
        super(detailMessage);
    }

}
