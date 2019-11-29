package com.viomi.router.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.viomi.router.annotation.Route;
import com.viomi.router.annotation.modle.RouteMeta;
import com.viomi.router.core.callback.InterceptorCallback;
import com.viomi.router.core.callback.NavigationCallback;
import com.viomi.router.core.exception.NoRouteFoundException;
import com.viomi.router.core.implments.InterceptorImpl;
import com.viomi.router.core.template.IInterceptorGroup;
import com.viomi.router.core.template.IRouteGroup;
import com.viomi.router.core.template.IRouteRoot;
import com.viomi.router.core.template.IService;
import com.viomi.router.core.utils.ClassUtils;
import com.viomi.router.core.utils.RouterLogX;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core
 * @ClassName: ViomiRouter
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-19 10:58
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 10:58
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ViomiRouter {

    private static final String SUB_TAG = ViomiRouter.class.getName();

    private static final String ROUTE_ROOT_PAKCAGE = "com.viomi.router.routes";
    private static final String SDK_NAME = "ViomiRouter";
    private static final String SEPARATOR = "_";
    private static final String SUFFIX_ROOT = "Root";
    private static final String SUFFIX_INTERCEPTOR = "Interceptor";

    private static ViomiRouter instance;
    private static Application mContext;
    private Handler mHandler;

    private ViomiRouter() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static ViomiRouter getInstance() {
        if (instance == null) {
            synchronized (ViomiRouter.class) {
                if (instance == null) {
                    instance = new ViomiRouter();
                }
            }
        }

        return instance;
    }

    public static void init(Application application) {
        mContext = application;

        try {
            loadInfo();
            InterceptorImpl.init(application.getApplicationContext());
            RouterLogX.i(Env.ROUTER_TAG, SUB_TAG, "初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            RouterLogX.e(Env.ROUTER_TAG, SUB_TAG, "初始化失败  " + e.getMessage());
        }
    }

    /**
     * 路由分组表制作
     */
    private static void loadInfo() throws PackageManager.NameNotFoundException,
            InterruptedException,
            ClassNotFoundException,
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        // 获取所有apt生成的路由类的全类名(路由表)
        Set<String> routerMap = ClassUtils.getFileNameByPackageName(mContext, ROUTE_ROOT_PAKCAGE);
        for (String className : routerMap) {
            if (className.startsWith(ROUTE_ROOT_PAKCAGE + "." + SDK_NAME + SEPARATOR + SUFFIX_ROOT)) {
                // root种注册的是分组信息，将分组信息加入仓库中
                ((IRouteRoot)Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.groupIndex);
            } else if (className.startsWith(ROUTE_ROOT_PAKCAGE + "." + SDK_NAME + SEPARATOR + SUFFIX_INTERCEPTOR)) {
                ((IInterceptorGroup)Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.interceptorsIndex);
            }
        }

        for (Map.Entry<String, Class<? extends IRouteGroup>> stringClassEntry : Warehouse.groupIndex.entrySet()) {
            RouterLogX.i(Env.ROUTER_TAG, SUB_TAG, "Root映射表[ " + stringClassEntry.getKey() + " : " + stringClassEntry.getValue() + "]");
        }
    }

    public PostCard build(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路由地址无效");
        }

        return build(path, extractGroup(path));
    }

    public PostCard build(String path, String group) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            throw new RuntimeException("路由地址无效!");
        } else {
            return new PostCard(path, group);
        }
    }

    /**
     * 默认从path中获取前部分路径作为group
     *
     * @param path
     * @return
     */
    private String extractGroup(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException(path + " : 不能提取group");
        }

        try {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (TextUtils.isEmpty(defaultGroup)) {
                throw new RuntimeException(path + " : 不能提取group");
            }

            return defaultGroup;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object navigation(Context context, PostCard postCard, int requestCode, NavigationCallback callback) {

        if (callback != null) {
            InterceptorImpl.onInterceptions(postCard, new InterceptorCallback() {
                @Override
                public void onNext(PostCard postCard) {
                    _navigation(context, postCard, requestCode, callback);
                }

                @Override
                public void onInterrupt(String interruptMsg) {
                    callback.onInterrupt(new Throwable(interruptMsg));
                }
            });
        } else {
            return _navigation(context, postCard, requestCode, callback);
        }

        return null;
    }

    private Object _navigation(Context context, PostCard postCard, int requestCode, NavigationCallback callback) {
        try {
            prepareCard(postCard);
        } catch (NoRouteFoundException e) {
            e.printStackTrace();
            // 没找到
            if (callback != null) {
                callback.onLost(postCard);
            }
            return null;
        }

        if (null != callback) {
            callback.onFound(postCard);
        }

        switch (postCard.getType()) {
            case ACTIVITY:
                final Context currentContext = null == context ? mContext : context;
                Intent intent = new Intent(currentContext, postCard.getDestination());
                intent.putExtras(postCard.getExtras());
                int flags = postCard.getFlags();
                if (-1 != flags) {
                    intent.setFlags(flags);
                } else if (!(currentContext instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                mHandler.post(() -> {
                    // 可能需要返回码
                    if (requestCode > 0) {
                        ActivityCompat.startActivityForResult((Activity)currentContext, intent,
                                requestCode, postCard.getOptionCompat());
                    } else {
                        ActivityCompat.startActivity(currentContext, intent,
                                postCard.getOptionCompat());
                    }

                    if ((0 != postCard.getEnterAnim() || 0 != postCard.getExitAnim()
                        && currentContext instanceof Activity)) {
                        ((Activity)currentContext).overridePendingTransition(postCard.getEnterAnim(), postCard.getExitAnim());
                    }

                    // 跳转完成
                    if (null != callback) {
                        callback.onArrival(postCard);
                    }
                });
                break;
            case ISERVICE:
                return postCard.getService();
                default:
                    break;
        }

        return null;

    }

    /**
     * 准备卡片
     *
     * @param card
     */
    private void prepareCard(PostCard card) {
        RouteMeta routeMeta = Warehouse.routes.get(card.getPath());
        if (null == routeMeta) {
            Class<? extends  IRouteGroup> groupMeta = Warehouse.groupIndex.get(card.getGroup());
            if (null == groupMeta) {
                throw new NoRouteFoundException("没找到对应路由：分组=" + card.getGroup() + "   路径=" + card.getPath());
            }
            IRouteGroup iGroupInstance;
            try {
                iGroupInstance = groupMeta.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("路由分组映射表记录失败。", e);
            }
            iGroupInstance.loadInto(Warehouse.routes);
            // 已经准备过了就可以移除（不会一直存在内存中）
            Warehouse.groupIndex.remove(card.getGroup());
            // 再次进入
            prepareCard(card);
        } else {
            // 类要跳转的activity 或IService实现类
            card.setDestination(routeMeta.getDestination());
            card.setType(routeMeta.getType());
            switch (routeMeta.getType()) {
                case ISERVICE:
                    Class<?> destination = routeMeta.getDestination();
                    IService service = Warehouse.services.get(destination);
                    if (null == service) {
                        try {
                            service = (IService)destination.getConstructor().newInstance();
                            Warehouse.services.put(destination, service);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    card.setService(service);
                    break;
                    default:
                        break;
            }
        }
    }

    /**
     * 注入
     *
     * @param instance
     */
    public void inject(Activity instance) {
        ExtraManager.getInstance().loadExtras(instance);
    }

}
