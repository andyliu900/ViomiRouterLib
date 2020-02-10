package com.viomi.router.core;

import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import com.viomi.router.annotation.modle.RouteMeta;
import com.viomi.router.core.callback.NavigationCallback;
import com.viomi.router.core.template.IService;

import java.util.ArrayList;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.router.core
 * @ClassName: PostCard
 * @Description:   这是跳转时携带的数据对象，构造器模式，最后调用 navigation 方法跳转页面
 * @Author: randysu
 * @CreateDate: 2019-11-19 16:04
 * @UpdateUser:
 * @UpdateDate: 2019-11-19 16:04
 * @UpdateRemark:
 * @Version: 1.0
 */
public class PostCard extends RouteMeta {

    private Bundle mBundle;
    private int flags = -1;
    // 转场新风格
    private Bundle optionCompat;
    // 转场旧风格
    private int enterAnim;
    private int exitAnim;
    // 服务
    private IService service;
    
    public PostCard(String path, String group) {
        this(path, group, null);
    }
    
    public PostCard(String path, String group, Bundle bundle) {
        setPath(path);
        setGroup(group);
        this.mBundle = (null == bundle ? new Bundle() : bundle);
    }

    public Bundle getExtras() {return mBundle;}

    public int getEnterAnim() {return enterAnim;}

    public int getExitAnim() {return exitAnim;}

    public IService getService() {
        return service;
    }

    public void setService(IService service) {
        this.service = service;
    }

    /**
     * Intent.FLAG_ACTIVITY**  打开Activity的方式
     * 
     * @param flag
     * @return
     */
    public PostCard withFlags(int flag) {
        this.flags = flag;
        return this;
    }
    
    public int getFlags() {
        return flags;
    }

    /**
     * 跳转动画
     * 
     * @param enterAnim
     * @param exitAnim
     * @return
     */
    public PostCard withTransition(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    /**
     * 转场动画
     * 
     * @param compat
     * @return
     */
    public PostCard withOptionsCompat(ActivityOptionsCompat compat) {
        if (null != compat) {
            this.optionCompat = compat.toBundle();
        }
        return this;
    }

    public PostCard withString(@Nullable String key, @Nullable String value) {
        mBundle.putString(key, value);
        return this;
    }


    public PostCard withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }


    public PostCard withShort(@Nullable String key, short value) {
        mBundle.putShort(key, value);
        return this;
    }


    public PostCard withInt(@Nullable String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }


    public PostCard withLong(@Nullable String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }


    public PostCard withDouble(@Nullable String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }


    public PostCard withByte(@Nullable String key, byte value) {
        mBundle.putByte(key, value);
        return this;
    }


    public PostCard withChar(@Nullable String key, char value) {
        mBundle.putChar(key, value);
        return this;
    }


    public PostCard withFloat(@Nullable String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }


    public PostCard withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }


    public PostCard withStringArray(@Nullable String key, @Nullable String[] value) {
        mBundle.putStringArray(key, value);
        return this;
    }


    public PostCard withBooleanArray(@Nullable String key, boolean[] value) {
        mBundle.putBooleanArray(key, value);
        return this;
    }


    public PostCard withShortArray(@Nullable String key, short[] value) {
        mBundle.putShortArray(key, value);
        return this;
    }


    public PostCard withIntArray(@Nullable String key, int[] value) {
        mBundle.putIntArray(key, value);
        return this;
    }


    public PostCard withLongArray(@Nullable String key, long[] value) {
        mBundle.putLongArray(key, value);
        return this;
    }


    public PostCard withDoubleArray(@Nullable String key, double[] value) {
        mBundle.putDoubleArray(key, value);
        return this;
    }


    public PostCard withByteArray(@Nullable String key, byte[] value) {
        mBundle.putByteArray(key, value);
        return this;
    }


    public PostCard withCharArray(@Nullable String key, char[] value) {
        mBundle.putCharArray(key, value);
        return this;
    }


    public PostCard withFloatArray(@Nullable String key, float[] value) {
        mBundle.putFloatArray(key, value);
        return this;
    }


    public PostCard withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return this;
    }

    public PostCard withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends
            Parcelable> value) {
        mBundle.putParcelableArrayList(key, value);
        return this;
    }

    public PostCard withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public PostCard withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }
    
    public Bundle getOptionCompat() {
        return optionCompat;
    }

    public Object navigation() {
        return ViomiRouter.getInstance().navigation(null, this, -1, null);
    }

    public Object navigation(Context context) {
        return ViomiRouter.getInstance().navigation(context, this, -1,null);
    }

    public Object navigation(Context context, NavigationCallback callback) {
        return ViomiRouter.getInstance().navigation(context, this, -1, callback);
    }

    public Object navigation(Context context, int requestCode) {
        return ViomiRouter.getInstance().navigation(context, this, requestCode, null);
    }

    public Object navigation(Context context, int requestCode, NavigationCallback callback) {
        return ViomiRouter.getInstance().navigation(context, this, requestCode, callback);
    }

    public Pair<Class<?>, Bundle> getProviderPage() {
        return ViomiRouter.getInstance().getProvidePage(this);
    }
    
}
