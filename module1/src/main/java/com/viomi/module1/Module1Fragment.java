package com.viomi.module1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.viomi.router.annotation.Extra;
import com.viomi.router.annotation.Route;
import com.viomi.router.core.ViomiRouter;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.module1
 * @ClassName: Module1Fragment
 * @Description:
 * @Author: randysu
 * @CreateDate: 2020-02-10 10:07
 * @UpdateUser:
 * @UpdateDate: 2020-02-10 10:07
 * @UpdateRemark:
 * @Version: 1.0
 */

@Route(path = "/module1/module1Fragment")
public class Module1Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module_test, container, false);

        ViomiRouter.getInstance().inject(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
