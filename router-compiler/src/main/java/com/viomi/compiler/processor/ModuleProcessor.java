package com.viomi.compiler.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.viomi.compiler.Constants;
import com.viomi.compiler.utils.LogUtils;
import com.viomi.compiler.utils.Tools;
import com.viomi.router.annotation.Module;

import java.io.IOException;
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
 * @ProjectName: ViomiModuleFramework
 * @Package: com.viomi.module_conpiler.processor
 * @ClassName: ModuleProcessor
 * @Description:  Module注解解析器
 * @Author: randysu
 * @CreateDate: 2020/3/30 10:47 AM
 * @UpdateUser:
 * @UpdateDate: 2020/3/30 10:47 AM
 * @UpdateRemark:
 * @Version: 1.0
 */

@AutoService(Processor.class)
@SupportedOptions(Constants.ARGUMENTS_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(Constants.ANNOTATION_TYPE_MODULE)
public class ModuleProcessor extends AbstractProcessor {

    /**
     * 节点工具类（类、函数、属性都是节点）
     */
    private Elements elementUtils;

    /**
     * type(类信息)工具类
     */
    private Types typeUtils;

    /**
     * 文件生成器
     */
    private Filer filerUtils;

    private String moduleName;
    private LogUtils logUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        logUtils = LogUtils.newLog(processingEnvironment.getMessager());

        logUtils.i("ModuleProcessor init");

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
        logUtils.i("init ModuleProcessor " + moduleName + " success !");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!Tools.isEmpty(set)) {
            Set<? extends Element> rootElements = roundEnvironment.getElementsAnnotatedWith(Module.class);
            if (!Tools.isEmpty(rootElements)) {
                processorModule(rootElements);
            }
            return true;
        }

        return false;
    }

    private void processorModule(Set<? extends Element> rootElements) {
        for (Element element : rootElements) {
            TypeMirror typeMirror = element.asType();
            logUtils.i("Module class:" + typeMirror.toString());

            generatedModuleEntryFile(typeMirror);
        }
    }

    /**
     * 生成Module入口java文件
     */
    private void generatedModuleEntryFile(TypeMirror typeMirror) {
        logUtils.i("generatedModuleEntryFile start");

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.METHOD_INIT_MODULE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        String fullJavaClassName = typeMirror.toString();
        int lastIndexOfDot = fullJavaClassName.lastIndexOf(".");
        String packageName = fullJavaClassName.substring(0, lastIndexOfDot);
        String className = fullJavaClassName.substring(lastIndexOfDot + 1);

        logUtils.i("packageName: " + packageName + "  className: " + className);

        ClassName moduleEntrance = ClassName.get(packageName, className);

        methodBuilder.addStatement("$T moduleEntrance = new $T()", moduleEntrance, moduleEntrance);
        methodBuilder.addStatement("moduleEntrance.initModule()");

        // 生成java文件
        TypeElement iModuleEntrance = elementUtils.getTypeElement(Constants.IMODULEENTRANCE);

        String newModuleEntranceClassName = Constants.NAME_OF_MODULEENTRANCE + moduleName;
        TypeSpec typeSpec = TypeSpec.classBuilder(newModuleEntranceClassName)
                .addSuperinterface(ClassName.get(iModuleEntrance))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();

        JavaFile javaFile = JavaFile.builder(Constants.PACKAGE_OF_GENERATE_MODULE_FILE, typeSpec).build();

        try {
            javaFile.writeTo(filerUtils);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logUtils.i("generatedModuleEntryFile end");
    }

}
