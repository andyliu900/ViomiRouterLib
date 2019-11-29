package com.viomi.router.core.callback;

import com.viomi.router.core.PostCard;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.callback
 * @ClassName: InterceptorCallback
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 16:16
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 16:16
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface InterceptorCallback {

    /**
     * 未拦截，走正常流程
     *
     * @param postCard
     */
    void onNext(PostCard postCard);

    /**
     * 拦截成功，中断流程
     *
     * @param interruptMsg
     */
    void onInterrupt(String interruptMsg);

}
