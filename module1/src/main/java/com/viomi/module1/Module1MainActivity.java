package com.viomi.module1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.viomi.router.annotation.Extra;
import com.viomi.router.annotation.Route;
import com.viomi.router.core.ViomiRouter;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.module1
 * @ClassName: Module1MainActivity
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-29 15:01
 * @UpdateUser:
 * @UpdateDate: 2019-11-29 15:01
 * @UpdateRemark:
 * @Version: 1.0
 */

@Route(path = "/module1/module1main")
public class Module1MainActivity extends AppCompatActivity {

    @Extra
    String msg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module1_main);
        ViomiRouter.getInstance().inject(this);
        Toast.makeText(this, "msg=" + msg, Toast.LENGTH_SHORT).show();
        Log.i("LLL", "msg=" + msg);
    }
}
