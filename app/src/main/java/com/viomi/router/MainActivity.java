package com.viomi.router;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.viomi.router.annotation.Route;
import com.viomi.router.annotation.modle.RouteMeta;
import com.viomi.router.core.Env;
import com.viomi.router.core.ViomiRouter;
import com.viomi.router.core.Warehouse;
import com.viomi.router.core.utils.RouterLogX;

import java.util.Map;

@Route(path = "/main/main")
public class MainActivity extends AppCompatActivity {

    private static final String SUB_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startModule1MainActivity(View view) {
        ViomiRouter.getInstance().build("/module1/module1main")
                .withString("msg", "从MainActivity").navigation();
    }

    public void startModule1Fragment(View view) {
        Intent intent = new Intent();
        intent.setClass(this, AutoLoadModuleFragmentActivity.class);
        startActivity(intent);
    }
}
