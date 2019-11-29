package com.viomi.router.core.utils;

import java.util.concurrent.CountDownLatch;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.utils
 * @ClassName: CancelableCountDownLatch
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-25 15:02
 * @UpdateUser:
 * @UpdateDate: 2019-11-25 15:02
 * @UpdateRemark:
 * @Version: 1.0
 */
public class CancelableCountDownLatch extends CountDownLatch {

    private String msg = "";

    public CancelableCountDownLatch(int count) {
        super(count);
    }

    /**
     * 当遇到特殊情况，需要将计步器清零
     *
     * @param msg
     */
    public void cancel(String msg) {
        this.msg = msg;
        while (getCount() > 0) {
            countDown();
        }
    }

    public String getMsg() {
        return msg;
    }
}
