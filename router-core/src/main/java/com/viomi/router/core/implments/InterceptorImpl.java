package com.viomi.router.core.implments;

import android.content.Context;

import com.viomi.router.core.PostCard;
import com.viomi.router.core.Warehouse;
import com.viomi.router.core.callback.InterceptorCallback;
import com.viomi.router.core.template.IInterceptor;
import com.viomi.router.core.thread.DefaultPoolExecutor;
import com.viomi.router.core.utils.CancelableCountDownLatch;
import com.viomi.router.core.utils.Tools;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.implments
 * @ClassName: InterceptorImpl
 * @Description:  拦截器的实现，初始化、路由跳转时都会用到这个类
 * @Author: randysu
 * @CreateDate: 2019-11-25 14:26
 * @UpdateUser:
 * @UpdateDate: 2019-11-25 14:26
 * @UpdateRemark:
 * @Version: 1.0
 */
public class InterceptorImpl {

    /**
     * 初始化时，需要轮询每个拦截器中的init()方法
     *
     * @param context
     */
    public static void init(Context context) {
        DefaultPoolExecutor.executor.execute(() -> {
            if (Tools.isEmpty(Warehouse.interceptorsIndex)) {
                for (Map.Entry<Integer, Class<? extends IInterceptor>> entry : Warehouse.interceptorsIndex.entrySet()) {
                    Class<? extends IInterceptor> interceptorClass = entry.getValue();
                    try {
                        IInterceptor iInterceptor = interceptorClass.getConstructor().newInstance();
                        iInterceptor.init(context);
                        Warehouse.interceptors.add(iInterceptor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 执行拦截逻辑
     *
     * @param postCard
     * @param callback
     */
    public static void onInterceptions(PostCard postCard, InterceptorCallback callback) {
        if (Warehouse.interceptors.size() > 0) {
            DefaultPoolExecutor.executor.execute(() -> {
                CancelableCountDownLatch countDownLatch = new CancelableCountDownLatch(Warehouse.interceptors.size());
                execute(0, countDownLatch, postCard);
                try {
                    countDownLatch.await(300, TimeUnit.SECONDS);
                    if (countDownLatch.getCount() > 0) {
                        callback.onInterrupt("拦截器超时");
                    } else if (!Tools.isEmpty(countDownLatch.getMsg())) {
                        callback.onInterrupt(countDownLatch.getMsg());
                    } else {
                        callback.onNext(postCard);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } else {
            callback.onNext(postCard);
        }
    }

    /**
     * 以递归的方式走完左右拦截器的process
     *
     * @param index
     * @param countDownLatch
     * @param postCard
     */
    private static void execute(int index, CancelableCountDownLatch countDownLatch, PostCard postCard) {
        if (index < Warehouse.interceptors.size()) {
            IInterceptor iInterceptor = Warehouse.interceptors.get(index);
            iInterceptor.process(postCard, new InterceptorCallback() {
                @Override
                public void onNext(PostCard postCard) {
                    countDownLatch.countDown();
                    execute(index + 1, countDownLatch, postCard);
                }

                @Override
                public void onInterrupt(String interruptMsg) {
                    countDownLatch.cancel(interruptMsg);
                }
            });
        }
    }

}
