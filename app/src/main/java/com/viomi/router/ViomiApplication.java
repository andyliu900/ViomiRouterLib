package com.viomi.router;

import android.app.Application;

import com.viomi.router.core.ViomiRouter;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router
 * @ClassName: ViomiApplication
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-29 14:10
 * @UpdateUser:
 * @UpdateDate: 2019-11-29 14:10
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ViomiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ViomiRouter.init(this, true);
    }
}
