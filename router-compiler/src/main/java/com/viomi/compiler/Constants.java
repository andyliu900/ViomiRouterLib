package com.viomi.compiler;

import com.squareup.javapoet.ClassName;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.compiler
 * @ClassName: Constants
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-25 16:50
 * @UpdateUser:
 * @UpdateDate: 2019-11-25 16:50
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Constants {

    public static final ClassName ROUTER = ClassName.get("com.viomi.router.core.ViomiRouter", "ViomiRouter");

    public static final String ACTIVITY = "android.app.Activity";
    public static final String FRAGMENT = "androidx.fragment.app.Fragment";
    public static final String ISERVICE = "com.viomi.router.core.template.IService";

    public static final String ARGUMENTS_NAME = "moduleName";
    public static final String ANNOTATION_TYPE_ROUTE = "com.viomi.router.annotation.Route";
    public static final String ANNOTATION_TYPE_EXTRA = "com.viomi.router.annotation.Extra";
    public static final String ANNOTATION_TYPE_INTERCEPTOR = "com.viomi.router.annotation.Interceptor";
    public static final String ANNOTATION_TYPE_EVENT = "com.viomi.router.annotation.Event";
    public static final String ANNOTATION_TYPE_MODULE = "com.viomi.router.annotation.Module";

    public static final String IROUTE_GROUP = "com.viomi.router.core.template.IRouteGroup";
    public static final String IROUTE_ROOT = "com.viomi.router.core.template.IRouteRoot";
    public static final String IEXTRA = "com.viomi.router.core.template.IExtra";
    public static final String IINTERCEPTOR_GROUP = "com.viomi.router.core.template.IInterceptorGroup";
    public static final String IINTERCEPTOR = "com.viomi.router.core.template.IInterceptor";
    public static final String IEVENT = "com.viomi.router.core.template.IEvent";
    public static final String IMODULEENTRANCE = "com.viomi.router.core.template.IModuleEntrance";

    public static final String SEPARATOR = "_";
    public static final String PROJECT = "ViomiRouter";
    public static final String NAME_OF_GROUP = PROJECT + SEPARATOR + "Group" + SEPARATOR;
    public static final String NAME_OF_ROOT = PROJECT + SEPARATOR + "Root" + SEPARATOR;
    public static final String NAME_OF_EVENT = PROJECT + SEPARATOR + "Event" + SEPARATOR;
    public static final String NAME_OF_MODULEENTRANCE = PROJECT + SEPARATOR + "Entrance" + SEPARATOR;
    public static final String PACKAGE_OF_GENERATE_FILE = "com.viomi.router.routes";
    public static final String PACKAGE_OF_GENERATE_EVENT_FILE = "com.viomi.router.events";
    public static final String PACKAGE_OF_GENERATE_MODULE_FILE = "com.viomi.module.entrances";

    public static final String METHOD_LOAD_INTO = "loadInto";
    public static final String METHOD_LOAD_EXTRA = "loadExtra";
    public static final String METHOD_INIT_MODULE = "initModule";

    public static final String PARCELABLE = "android.os.Parcelable";

    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INTEGER = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBEL = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String STRING = LANG + ".String";

    public static final String ARRAYLIST = "java.util.ArrayList";
    public static final String LIST = "java.util.List";

    public static final String BYTEARRAY = "byte[]";
    public static final String SHORTARRAY = "short[]";
    public static final String BOOLEANARRAY = "boolean[]";
    public static final String CHARARRAY = "char[]";
    public static final String DOUBLEARRAY = "double[]";
    public static final String FLOATARRAY = "float[]";
    public static final String INTARRAY = "int[]";
    public static final String LONGARRAY = "long[]";
    public static final String STRINGARRAY = "java.lang.String[]";

    public static final String NAME_OF_EXTRA = SEPARATOR + "Extra";
    public static final String NAME_OF_INTERCEPTOR = PROJECT + SEPARATOR + "Interceptor" + SEPARATOR;

}
