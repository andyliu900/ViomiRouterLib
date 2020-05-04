package com.viomi.module1;

import android.util.Log;

import com.viomi.router.annotation.Module;
import com.viomi.router.core.template.IModuleEntrance;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.module1
 * @ClassName: Module1Entrance
 * @Description:
 * @Author: randysu
 * @CreateDate: 2020/3/30 4:24 PM
 * @UpdateUser:
 * @UpdateDate: 2020/3/30 4:24 PM
 * @UpdateRemark:
 * @Version: 1.0
 */

@Module
public class Module1Entrance implements IModuleEntrance {

    @Override
    public void init() {
        Log.i("XXXXX", "Module1Entrance   init");
    }

}
