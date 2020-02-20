package com.viomi.compiler.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.viomi.compiler.Constants;
import com.viomi.compiler.utils.LogUtils;
import com.viomi.compiler.utils.Tools;
import com.viomi.router.annotation.Route;
import com.viomi.router.annotation.modle.RouteMeta;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.compiler.processor
 * @ClassName: RouterProcessor
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-26 17:41
 * @UpdateUser:
 * @UpdateDate: 2019-11-26 17:41
 * @UpdateRemark:
 * @Version: 1.0
 */

@AutoService(Processor.class)
@SupportedOptions(Constants.ARGUMENTS_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(Constants.ANNOTATION_TYPE_ROUTE)
public class RouterProcessor extends AbstractProcessor {

    /**
     * ket: 组名  value: 类名
     */
    private Map<String, String> rootMap = new TreeMap();

    /**
     * ket: 组名  value:
     */
    private Map<String, List<RouteMeta>> groupMap = new HashMap();

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementUtils;

    /**
     * type(类信息)工具类
     */
    private Types typeUtils;

    /**
     * 文件生成器 类/资源
     */
    private Filer filerUtils;

    private String moduleName;

    private LogUtils logUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        logUtils = LogUtils.newLog(processingEnvironment.getMessager());
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filerUtils = processingEnvironment.getFiler();

        //参数是模块名 为了防止多模块/组件化开发的时候 生成相同的 xx$$ROOT$$文件
        Map<String, String> options = processingEnvironment.getOptions();
        logUtils.i("options size " + options.size());
        if (!Tools.isEmpty(options)) {
            moduleName = options.get(Constants.ARGUMENTS_NAME);
        }
        if (Tools.isEmpty(moduleName)) {
            throw new RuntimeException("Not set processor moudleName option !");
        }
        logUtils.i("init RouterProcessor " + moduleName + " success !");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!Tools.isEmpty(set)) {
            Set<? extends Element> rootElements = roundEnvironment.getElementsAnnotatedWith(Route.class);
            if (!Tools.isEmpty(rootElements)) {
                processorRoute(rootElements);
            }
            return true;
        }

        return false;
    }

    private void processorRoute(Set<? extends Element> rootElements) {
        TypeElement activity = elementUtils.getTypeElement(Constants.ACTIVITY);
        TypeElement fragment = elementUtils.getTypeElement(Constants.FRAGMENT);
        TypeElement service = elementUtils.getTypeElement(Constants.ISERVICE);
        for (Element element : rootElements) {
            RouteMeta routeMeta;
            // 类信息
            TypeMirror typeMirror = element.asType();
            logUtils.i("Route class:" + typeMirror.toString());
            Route route = element.getAnnotation(Route.class);
            if (typeUtils.isSubtype(typeMirror, activity.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.ACTIVITY, route, element);
            } else if (typeUtils.isSubtype(typeMirror, fragment.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.FRAGMENT, route, element);
            } else if (typeUtils.isSubtype(typeMirror, service.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.ISERVICE, route, element);
            } else {
                throw new RuntimeException("Just support Activity or Fragment or IService Route: " + element);
            }
            categories(routeMeta);
        }
        TypeElement iRouteGroup = elementUtils.getTypeElement(Constants.IROUTE_GROUP);
        TypeElement iRouteRoot = elementUtils.getTypeElement(Constants.IROUTE_ROOT);

        // 生成Group记录分组表
        generatedGroup(iRouteGroup);

        // 生成Root类 作用：记录<分组， 对应的Group类>
        generatedRoot(iRouteRoot, iRouteGroup);
    }

    private void categories(RouteMeta routeMeta) {
        if (routeVerify(routeMeta)) {
            logUtils.i("Group : " + routeMeta.getGroup() + " path = " + routeMeta.getPath());
            // 分组与组中的路由信息
            List<RouteMeta> routeMetas = groupMap.get(routeMeta.getGroup());
            if (Tools.isEmpty(routeMetas)) {
                routeMetas = new ArrayList<>();
                routeMetas.add(routeMeta);
                groupMap.put(routeMeta.getGroup(), routeMetas);
            } else {
                routeMetas.add(routeMeta);
            }
        } else {
            logUtils.i("Group info error:" + routeMeta.getPath());
        }
    }

    /**
     * 生成Group类
     *
     * @param iRouteGroup
     */
    private void generatedGroup(TypeElement iRouteGroup) {
        // 创建参数类型  Map<String, RouteMeta>
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class));
        ParameterSpec altas = ParameterSpec.builder(parameterizedTypeName, "altas").build();

        for (Map.Entry<String, List<RouteMeta>> entry : groupMap.entrySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.METHOD_LOAD_INTO)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(altas);

            String groupName = entry.getKey();
            List<RouteMeta> groupData = entry.getValue();
            for (RouteMeta routeMeta : groupData) {
                // 函数体的添加
                methodBuilder.addStatement("altas.put($S,$T.build($T.$L,$T.class,$S,$S))",
                        routeMeta.getPath(),
                        ClassName.get(RouteMeta.class),
                        ClassName.get(RouteMeta.Type.class),
                        routeMeta.getType(),
                        ClassName.get((TypeElement)routeMeta.getElement()),
                        routeMeta.getPath(),
                        routeMeta.getGroup());
            }

            String groupClassName = Constants.NAME_OF_GROUP + groupName;
            TypeSpec typeSpec = TypeSpec.classBuilder(groupClassName)
                    .addSuperinterface(ClassName.get(iRouteGroup))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())
                    .build();
            JavaFile javaFile = JavaFile.builder(Constants.PACKAGE_OF_GENERATE_FILE, typeSpec).build();
            try {
                javaFile.writeTo(filerUtils);
            } catch (IOException e) {
                e.printStackTrace();
            }
            rootMap.put(groupName, groupClassName);
        }

    }

    /**
     * 生成Root类
     *
     * @param iRouteRoot
     * @param iRouteGroup
     */
    private void generatedRoot(TypeElement iRouteRoot, TypeElement iRouteGroup) {
        // 创建参数类型   Map<String,Class<? extends IRouteGroup>> routes
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(iRouteGroup))
                ));

        // 参数   Map<String,Class<? extends IRouteGroup>> routes> routes
        ParameterSpec parameterSpec = ParameterSpec.builder(parameterizedTypeName, "routes").build();

        // 函数  public void loadInfo(Map<String,Class<? extends IRouteGroup>> routes> routes)
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.METHOD_LOAD_INTO)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(parameterSpec);

        // 函数体
        for (Map.Entry<String, String> entry : rootMap.entrySet()) {
            methodBuilder.addStatement("routes.put($S, $T.class)", entry.getKey(), ClassName.get(Constants.PACKAGE_OF_GENERATE_FILE, entry.getValue()));
        }

        // 生成$Root$类
        String className = Constants.NAME_OF_ROOT + moduleName;
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(iRouteRoot))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();

        try {
            JavaFile.builder(Constants.PACKAGE_OF_GENERATE_FILE, typeSpec).build().writeTo(filerUtils);
            logUtils.i("Generated RouteRoot：" + Constants.PACKAGE_OF_GENERATE_FILE + "." + className);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证path路由地址合法性
     *
     * @param routeMeta
     * @return
     */
    private boolean routeVerify(RouteMeta routeMeta) {
        String path = routeMeta.getPath();
        String group = routeMeta.getGroup();

        if (!path.startsWith("/")) {
            return false;
        }

        if (Tools.isEmpty(group)) {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (Tools.isEmpty(defaultGroup)) {
                return false;
            }
            routeMeta.setGroup(defaultGroup);
        }

        return true;
    }

}
