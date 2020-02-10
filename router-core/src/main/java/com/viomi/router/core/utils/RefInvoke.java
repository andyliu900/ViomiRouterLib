package com.viomi.router.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiAndroidPluginSDK
 * @Package: com.viomiplugin.host.utils
 * @ClassName: RefInvoke
 * @Description: 反射工具类
 * @Author: randysu
 * @CreateDate: 2019-07-29 17:20
 * @UpdateUser:
 * @UpdateDate: 2019-07-29 17:20
 * @UpdateRemark:
 * @Version: 1.0
 */
public class RefInvoke {

    /********************    创建对象    **********************/

    /**
     * 无参
     *
     * @param className
     * @return
     */
    public static Object createObject(String className) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};

        try {
            Class r = Class.forName(className);
            return createObject(r, pareTypes, pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 无参
     *
     * @param clazz
     * @return
     */
    public static Object createObject(Class clazz) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};

        return createObject(clazz, pareTypes, pareValues);
    }

    /**
     * 一个参数
     *
     * @param className
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object createObject(String className, Class pareType, Object pareValue) {
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};

        try {
            Class r = Class.forName(className);
            return createObject(r, pareTypes, pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 一个参数
     *
     * @param clazz
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object createObject(Class clazz, Class pareType, Object pareValue) {
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};

        return createObject(clazz, pareTypes, pareValues);
    }

    /**
     * 多个参数
     *
     * @param className
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object createObject(String className, Class[] pareTypes, Object[] pareValues) {
        try {
            Class r = Class.forName(className);
            return createObject(r, pareTypes, pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 多个参数
     *
     * @param clazz
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object createObject(Class clazz, Class[] pareTypes, Object[] pareValues) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor(pareTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /********************    执行方法    **********************/

    /**
     * 多个参数
     *
     * @param obj
     * @param methodName
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class[] pareTypes, Object[] pareValues) {
        if (obj == null) {
            return null;
        }

        try {
            Method method = obj.getClass().getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(obj, pareValues);  // 实例方法需要传递实例变量
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 一个参数
     *
     * @param obj
     * @param methodName
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class pareType, Object pareValue) {
        Class[] pareTypes = {pareType};
        Object[] pareValues = {pareValue};

        return invokeInstanceMethod(obj, methodName, pareTypes, pareValues);
    }

    /**
     * 无参
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};

        return invokeInstanceMethod(obj, methodName, pareTypes, pareValues);
    }

    /**
     * 执行静态方法   多参数
     *
     * @param clazz
     * @param methodName
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String methodName, Class[] pareTypes, Object[] pareValues) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(null, pareValues); // 静态方法不用传递实例对象
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 一个参数
     *
     * @param clazz
     * @param methodName
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String methodName, Class pareType, Object pareValue) {
        Class[] pareTypes = {pareType};
        Object[] pareValues = {pareValue};

        return invokeStaticMethod(clazz, methodName, pareTypes, pareValues);
    }

    /**
     * 无参
     *
     * @param clazz
     * @param methodName
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String methodName) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};

        return invokeStaticMethod(clazz, methodName, pareTypes, pareValues);
    }

    /**
     * 多参数
     *
     * @param className
     * @param methodName
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName, Class[] pareTypes, Object[] pareValues) {
        try {
            Class clazz = Class.forName(className);
            return invokeStaticMethod(clazz, methodName, pareTypes, pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 一个参数
     *
     * @param className
     * @param methodName
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName, Class pareType, Object pareValue) {
        Class[] pareTypes = {pareType};
        Object[] pareValues = {pareValue};

        return invokeStaticMethod(className, methodName, pareTypes, pareValues);
    }

    /**
     * 无参
     *
     * @param className
     * @param methodName
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};

        return invokeStaticMethod(className, methodName, pareTypes, pareValues);
    }

    /********************    获取实例变量    **********************/

    public static Object getFieldObject(Object obj, String fieldName) {
        return getFieldObject(obj.getClass(), obj, fieldName);
    }

    public static Object getFieldObject(String className, Object obj, String fieldName) {
        try {
            Class clazz = Class.forName(className);
            return getFieldObject(clazz, obj, fieldName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getFieldObject(Class clazz, Object obj, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /********************    设置实例变量    **********************/

    public static void setFieldObject(Object obj, String fieldName, Object fieldValue) {
        setFieldObject(obj.getClass(), obj, fieldName, fieldValue);
    }

    public static void setFieldObject(String className, Object obj, String fieldName, Object fieldValue) {
        try {
            Class clazz = Class.forName(className);
            setFieldObject(clazz, obj, fieldName, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置对象内部的变量
     *
     * @param clazz      目标对象class
     * @param obj        目标对象实例
     * @param fieldName  变量名称
     * @param fieldValue 新的变量值
     */
    public static void setFieldObject(Class clazz, Object obj, String fieldName, Object fieldValue) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setObjectField(Object object, String fieldName, Object value) {
        try {
            Field field = findField(object.getClass(), fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Object getObjectField(Object object, String fieldName) {
        try {
            Field field = findField(object.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Field findField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            for (Class<?> cls = clazz; cls != null; cls = cls.getSuperclass()) {
                try {
                    return cls.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e1) {
                    // Ignore
                }
            }
            throw e;
        }
    }

    /********************    获取静态变量    **********************/

    public static Object getStaticFieldObject(String className, String fieldName) {
        return getFieldObject(className, null, fieldName);
    }

    public static Object getStaticFieldObject(Class clazz, String fieldName) {
        return getFieldObject(clazz, null, fieldName);
    }

    /********************    设置静态变量    **********************/

    public static void setStaticFieldObject(String className, String fieldName, Object fieldValue) {
        setFieldObject(className, null, fieldName, fieldValue);
    }

    public static void setStaticFieldObject(Class clazz, String fieldName, Object fieldValue) {
        setFieldObject(clazz, null, fieldName, fieldValue);
    }
}
