package com.viomi.router.core.callback;

import com.viomi.router.core.PostCard;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.callback
 * @ClassName: NavigationCallback
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 16:18
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 16:18
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface NavigationCallback {

    /**
     * 找到跳转页面
     *
     * @param postCard
     */
    void onFound(PostCard postCard);

    /**
     * 未找到
     *
     * @param postCard
     */
    void onLost(PostCard postCard);

    /**
     * 成功跳转
     *
     * @param postCard
     */
    void onArrival(PostCard postCard);

    /**
     * 中断了路由跳转
     *
     * @param throwable
     */
    void onInterrupt(Throwable throwable);

}
