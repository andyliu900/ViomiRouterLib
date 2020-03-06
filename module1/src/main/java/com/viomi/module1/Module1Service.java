package com.viomi.module1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.viomi.router.annotation.Route;
import com.viomi.router.core.template.IService;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.module1
 * @ClassName: Module1Service
 * @Description:   module1 中的 Service
 * @Author: randysu
 * @CreateDate: 2020/3/6 10:19 AM
 * @UpdateUser:
 * @UpdateDate: 2020/3/6 10:19 AM
 * @UpdateRemark:
 * @Version: 1.0
 */

@Route(path = "/module1/module1service")
public class Module1Service extends Service implements IService {

    private static final String SUB_TAG = Module1Service.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(SUB_TAG, "Module1Service  created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(SUB_TAG, "Module1Service  onStartCommand");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(SUB_TAG, "Module1Service  onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
