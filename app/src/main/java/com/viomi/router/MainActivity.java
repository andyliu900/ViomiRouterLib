package com.viomi.router;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.viomi.router.annotation.Route;
import com.viomi.router.core.ViomiRouter;

@Route(path = "/main/main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startModule1MainActivity(View view) {
        ViomiRouter.getInstance().build("/module1/module1main")
                .withString("msg", "从MainActivity").navigation();
    }
}
