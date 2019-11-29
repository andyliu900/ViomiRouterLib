package com.viomi.router.core.template;

import android.content.Context;

import com.viomi.router.core.PostCard;
import com.viomi.router.core.callback.InterceptorCallback;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.template
 * @ClassName: IInterceptor
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 16:03
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 16:03
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IInterceptor {

    /**
     * 拦截器流程
     *
     * @param postCard
     * @param callback
     */
    void process(PostCard postCard, InterceptorCallback callback);

    /**
     * 在调用ViomiRouter.init()初始化时，会调用此方法
     *
     * @param context
     */
    void init(Context context);

}
