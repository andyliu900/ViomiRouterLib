package com.viomi.router;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.viomi.router.core.Env;
import com.viomi.router.core.ViomiRouter;
import com.viomi.router.core.utils.RefInvoke;
import com.viomi.router.core.utils.RouterLogX;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.router
 * @ClassName: AutoLoadModuleFragmentActivity
 * @Description:
 * @Author: randysu
 * @CreateDate: 2020-02-10 10:29
 * @UpdateUser:
 * @UpdateDate: 2020-02-10 10:29
 * @UpdateRemark:
 * @Version: 1.0
 */
public class AutoLoadModuleFragmentActivity extends AppCompatActivity {

    private static final String SUB_TAG = AutoLoadModuleFragmentActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoload_modulefragment);

        Pair<Class<?>, Bundle> fragmentPair = ViomiRouter.getInstance().build("/module1/module1Fragment")
                .withString("msg", "我来自MainModule")
                .getProviderPage();
        RouterLogX.i(Env.ROUTER_TAG, SUB_TAG, "fragment class = " + fragmentPair.first.getCanonicalName());

        Fragment module1Fragment = (Fragment)RefInvoke.createObject(fragmentPair.first);
        module1Fragment.setArguments(fragmentPair.second);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_content, module1Fragment, "Module1Fragment").commit();
    }
}
