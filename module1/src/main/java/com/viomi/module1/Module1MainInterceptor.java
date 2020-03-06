package com.viomi.module1;

import android.content.Context;

import com.viomi.router.annotation.Interceptor;
import com.viomi.router.core.Env;
import com.viomi.router.core.PostCard;
import com.viomi.router.core.callback.InterceptorCallback;
import com.viomi.router.core.template.IInterceptor;
import com.viomi.router.core.utils.RouterLogX;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.router
 * @ClassName: MainInterceptor
 * @Description:
 * @Author: randysu
 * @CreateDate: 2020/3/5 5:12 PM
 * @UpdateUser:
 * @UpdateDate: 2020/3/5 5:12 PM
 * @UpdateRemark:
 * @Version: 1.0
 */

@Interceptor(priority = 1, name = "Module1MainInterceptor")
public class Module1MainInterceptor implements IInterceptor {

    private static final String SUB_TAG = Module1MainInterceptor.class.getName();

    private Context context;

    @Override
    public void process(PostCard postCard, InterceptorCallback callback) {
        RouterLogX.i(Env.ROUTER_TAG, SUB_TAG, "postCard.getPath = " + postCard.getPath());

        RouterLogX.i(Env.ROUTER_TAG, SUB_TAG, "context name = " + context.getPackageName());

        if ("/module1/module1main".equals(postCard.getPath())) {
            postCard.withString("extra", "拦截器额外增加的参数");
            callback.onNext(postCard);
        } else {
            callback.onNext(postCard);
        }
    }

    @Override
    public void init(Context context) {
        this.context = context;
        RouterLogX.i(Env.ROUTER_TAG, SUB_TAG, Module1MainInterceptor.class.getName() + " has init.");
    }
}
