package com.viomi.router.core.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core.thread
 * @ClassName: DefaultPoolExecutor
 * @Description:  线程池
 * @Author: randysu
 * @CreateDate: 2019-11-19 14:12
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 14:12
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DefaultPoolExecutor {

    public static ThreadPoolExecutor executor;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "ViomiRouter #" + mCount.getAndIncrement());
        }
    };

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MAX_CORE_POOL_SIZE = CPU_COUNT + 1;

    // 存活30秒 回收线程
    private static final long SURPLUS_THREAD_LIFE = 30L;

    public static ThreadPoolExecutor newDefaultPoolExecutor(int corePoolSize) {
        if (corePoolSize <= 0) {
            return null;
        }

        corePoolSize = Math.min(corePoolSize, MAX_CORE_POOL_SIZE);
        executor = new ThreadPoolExecutor(corePoolSize, corePoolSize, SURPLUS_THREAD_LIFE, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(64), sThreadFactory);
        // 核心线程也会被销毁
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

}
