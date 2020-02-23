package com.viomi.router.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.viomi.router.annotation.Event;
import com.viomi.router.annotation.Route;
import com.viomi.router.annotation.modle.EventMeta;
import com.viomi.router.annotation.modle.RouteMeta;
import com.viomi.router.core.callback.InterceptorCallback;
import com.viomi.router.core.callback.NavigationCallback;
import com.viomi.router.core.exception.NoRouteFoundException;
import com.viomi.router.core.implments.InterceptorImpl;
import com.viomi.router.core.template.IEvent;
import com.viomi.router.core.template.IInterceptorGroup;
import com.viomi.router.core.template.IRouteGroup;
import com.viomi.router.core.template.IRouteRoot;
import com.viomi.router.core.template.IService;
import com.viomi.router.core.utils.ClassUtils;
import com.viomi.router.core.utils.RouterLogX;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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
 *
 * 打开页面处理流程
 *
 * 1、构造PostCard对象
 * 2、通过PostCard对象从  Warehouse.groupIndex  中生成出 RouteMeta，这个 RouteMeta 包含了path、group、type、目标class 信息
 * 3、拿到 RouteMeta 信息后，构建 Intent 对象，将 目标class 填进Intent中，进行普通  startActivity
 *
 */
public class ViomiRouter {

    private static final String SUB_TAG = ViomiRouter.class.getName();


    private static String ROUTE_ROOT_PAKCAGE = "com.viomi.router";
    private static String ROUTER_PACKAGE = "routes";
    private static String EVENT_PACKAGE = "events";
    private static final String SDK_NAME = "ViomiRouter";
    private static final String SEPARATOR = "_";
    private static final String SUFFIX_ROOT = "Root";
    private static final String SUFFIX_INTERCEPTOR = "Interceptor";
    private static final String SUFFIX_EVENT = "Event";

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

            RouterLogX.i(Env.ROUTER_TAG, SUB_TAG, "className[ " + className + "]");

            if (className.startsWith(ROUTE_ROOT_PAKCAGE + "." + ROUTER_PACKAGE + "." + SDK_NAME + SEPARATOR + SUFFIX_ROOT)) {
                // root种注册的是分组信息，将分组信息加入仓库中
                ((IRouteRoot)Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.groupIndex);
            } else if (className.startsWith(ROUTE_ROOT_PAKCAGE + "." + ROUTER_PACKAGE + "." + SDK_NAME + SEPARATOR + SUFFIX_INTERCEPTOR)) {
                ((IInterceptorGroup)Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.interceptorsIndex);
            } else if (className.startsWith(ROUTE_ROOT_PAKCAGE + "." + EVENT_PACKAGE + "." + SDK_NAME + SEPARATOR + SUFFIX_EVENT)) {
                ((IEvent)Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.events);
            }
        }

        for (Map.Entry<String, Class<? extends IRouteGroup>> stringClassEntry : Warehouse.groupIndex.entrySet()) {
            RouterLogX.i(Env.ROUTER_TAG, SUB_TAG, "Root映射表[ " + stringClassEntry.getKey() + " : " + stringClassEntry.getValue() + "]");
        }

        for (Map.Entry<String, HashMap<String, EventMeta>> moduleEvent : Warehouse.events.entrySet()) {
            String moduleName = moduleEvent.getKey();
            RouterLogX.e(Env.ROUTER_TAG, SUB_TAG, "moduleName: " + moduleName);
            HashMap<String, EventMeta> eventList = moduleEvent.getValue();
            for (Map.Entry<String, EventMeta> event : eventList.entrySet()) {
                String eventKey = event.getKey();
                EventMeta eventMeta = event.getValue();
                RouterLogX.e(Env.ROUTER_TAG, SUB_TAG, "eventKey: " + eventKey + "  event fullName: " + eventMeta.getFullJavaClassName());
            }
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

    public void inject(Fragment instance) {
        ExtraManager.getInstance().loadExtras(instance);
    }

    /**
     * 通过构建PostCard，生成RouteMeta，获得其中的 destination，返回这个 destination class
     *
     * @param card
     * @return  返回destination 的 class，在在外面反射获得class的实例
     *
     * 这种情况主要针对Fragment
     */
    public Pair<Class<?>, Bundle> getProvidePage(PostCard card) {
        if (card == null) {
            return null;
        }

        RouteMeta routeMeta = Warehouse.routes.get(card.getPath());
        if (routeMeta == null) {
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
            // 要对外提供Fragment实现类
            card.setDestination(routeMeta.getDestination());
            card.setType(routeMeta.getType());
        }

        if (card == null) {
            return null;
        }

        return new Pair(card.getDestination(), card.getExtras());
    }

    /**
     * 设置Warehouse.events中的Event中传递参数，根据fullJavaClassName进行判断，理论上可以规避不同module相同名称的Event问题
     *
     * @param fullJavaClassName
     * @param params
     */
    public void setEventParams(String fullJavaClassName, HashMap<String, Object> params) {
        if (TextUtils.isEmpty(fullJavaClassName)) {
            return;
        }

        for (Map.Entry<String, HashMap<String, EventMeta>> entry : Warehouse.events.entrySet()) {
            HashMap<String, EventMeta> eventList = entry.getValue();
            for (Map.Entry<String, EventMeta> eventMetaEntry : eventList.entrySet()) {
                EventMeta eventMeta = eventMetaEntry.getValue();
                if (fullJavaClassName.equals(eventMeta.getFullJavaClassName())) {
                    eventMeta.setParams(params);
                }
            }
        }
    }

    /**
     * 根据moduleName和key找到对应的Event Class对象
     *
     * @param moduleName
     * @param key
     *
     * @return
     */
    public <T> Class<T> getEventByKey(String moduleName, String key) {
        if (TextUtils.isEmpty(moduleName) || TextUtils.isEmpty(key)) {
            return null;
        }

        try {
            for (Map.Entry<String, HashMap<String, EventMeta>> entry : Warehouse.events.entrySet()) {
                if (entry.getKey().equals(moduleName)) {
                    HashMap<String, EventMeta> eventList = entry.getValue();
                    for (Map.Entry<String, EventMeta> eventMetaEntry : eventList.entrySet()) {
                        if (eventMetaEntry.getKey().equals(key)) {
                            EventMeta eventMeta = eventMetaEntry.getValue();
                            String eventFullJavaClassName = eventMeta.getFullJavaClassName();
                            Class eventClass = Class.forName(eventFullJavaClassName);
                            return eventClass;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
