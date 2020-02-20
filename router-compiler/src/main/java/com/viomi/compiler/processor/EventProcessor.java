package com.viomi.compiler.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.viomi.compiler.Constants;
import com.viomi.compiler.utils.LogUtils;
import com.viomi.compiler.utils.Tools;
import com.viomi.router.annotation.Event;
import com.viomi.router.annotation.modle.EventMeta;
import com.viomi.router.annotation.modle.RouteMeta;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.compiler.processor
 * @ClassName: EventProcessor
 * @Description:  Event注解处理
 * @Author: randysu
 * @CreateDate: 2020-02-19 11:33
 * @UpdateUser:
 * @UpdateDate: 2020-02-19 11:33
 * @UpdateRemark:
 * @Version: 1.0
 *
 * 1、每一个module生成一个类，类名是moduleName
 * 2、这个类中通过loadInto方法，在内部构建EventMeta对象，并将对象填充至传入的Map参数
 * 3、每一个类只维护module内部自己的Event集合，内部遍历这个集合来构建EventMeta对象
 */

@AutoService(Processor.class)
@SupportedOptions(Constants.ARGUMENTS_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(Constants.ANNOTATION_TYPE_EVENT)
public class EventProcessor extends AbstractProcessor {

    /**
     * 节点工具类（类、函数、属性都是节点）
     */
    private Elements elementUtils;

    /**
     * type（类信息）工具类
     */
    private Types typeUtils;

    /**
     * 文件生成器  类/资源
     */
    private Filer filerUtils;

    private String moduleName;
    private LogUtils logUtils;

    private HashMap<String, EventMeta> eventCollection = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        logUtils = LogUtils.newLog(processingEnvironment.getMessager());
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filerUtils = processingEnvironment.getFiler();

        Map<String, String> options = processingEnvironment.getOptions();
        logUtils.i("options size:" + options.size());
        if (!Tools.isEmpty(options)) {
            moduleName = options.get(Constants.ARGUMENTS_NAME);
        }
        if (Tools.isEmpty(moduleName)) {
            throw new RuntimeException("Not set processor moudleName option !");
        }
        logUtils.i("init EventProcessor " + moduleName + " success !");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!Tools.isEmpty(set)) {
            Set<? extends Element> rootElements = roundEnvironment.getElementsAnnotatedWith(Event.class);
            if (!Tools.isEmpty(rootElements)) {
                processorEvent(rootElements);
            }
            return true;
        }
        return false;
    }

    private void processorEvent(Set<? extends Element> rootElements) {
        for (Element element : rootElements) {
            EventMeta eventMeta;

            // 类信息
            TypeMirror typeMirror = element.asType();
            logUtils.i("Event class:" + typeMirror.toString());

            Event event = element.getAnnotation(Event.class);
            String fileName = element.getSimpleName().toString();
            eventMeta = new EventMeta(element, fileName, typeMirror.toString(), event, moduleName);
            categories(eventMeta);
        }

        generatedEventFile();
    }

    /**
     * 根据EventMeta生成事件集合
     *
     * @param eventMeta
     */
    private void categories(EventMeta eventMeta) {
        if (eventMeta == null) {
            return;
        }

        if (Tools.isEmpty(moduleName)) {
            return;
        }

        eventCollection.put(eventMeta.getKey(), eventMeta);

        logUtils.i("categories success");
    }

    /**
     * 生成Event的java文件
     */
    private void generatedEventFile() {
        logUtils.i("generatedEventFile start");

        TypeName mapOfEventMeta = ParameterizedTypeName.get(
                ClassName.get(HashMap.class),
                ClassName.get(String.class),
                ClassName.get(EventMeta.class));

        // 创建参数类型  HashMap<String, HashMap<String, EventMeta>>
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(HashMap.class),
                ClassName.get(String.class),
                mapOfEventMeta);
        ParameterSpec eventMetaMap = ParameterSpec.builder(parameterizedTypeName, "eventMetaMap").build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.METHOD_LOAD_INTO)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(eventMetaMap);


        methodBuilder.addStatement("$T eventCollection = new $T()", mapOfEventMeta, ClassName.get(HashMap.class));

        for (String key : eventCollection.keySet()) {
            EventMeta eventMetaObj = eventCollection.get(key);
            methodBuilder.addStatement("eventCollection.put($S, $T.build($S, $S, $S, $S))",
                    key,
                    ClassName.get(EventMeta.class),
                    eventMetaObj.getFileName(),
                    eventMetaObj.getFullJavaClassName(),
                    key,
                    moduleName);
        }

        methodBuilder.addStatement("eventMetaMap.put($S, eventCollection)",
                moduleName);

        // 生成java文件
        TypeElement iEvent = elementUtils.getTypeElement(Constants.IEVENT);

        String newEventClassName = Constants.NAME_OF_EVENT + moduleName;
        TypeSpec typeSpec = TypeSpec.classBuilder(newEventClassName)
                .addSuperinterface(ClassName.get(iEvent))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();
        JavaFile javaFile = JavaFile.builder(Constants.PACKAGE_OF_GENERATE_EVENT_FILE, typeSpec)
                .build();
        try {
            javaFile.writeTo(filerUtils);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logUtils.i("generatedEventFile end");
    }

}
