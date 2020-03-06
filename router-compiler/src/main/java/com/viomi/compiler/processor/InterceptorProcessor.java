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
import com.viomi.router.annotation.Interceptor;

import java.io.IOException;
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
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterApplication
 * @Package: com.viomi.compiler.processor
 * @ClassName: InterceptorProcessor
 * @Description:
 * @Author: randysu
 * @CreateDate: 2019-11-26 16:11
 * @UpdateUser:
 * @UpdateDate: 2019-11-26 16:11
 * @UpdateRemark:
 * @Version: 1.0
 */

@AutoService(Processor.class)
@SupportedOptions(Constants.ARGUMENTS_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(Constants.ANNOTATION_TYPE_INTERCEPTOR)
public class InterceptorProcessor extends AbstractProcessor {

    private Map<Integer, Element> interceptors = new HashMap();

    /**
     * 节点工具类(类、函数、属性都是节点)
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

    private TypeMirror iInterceptor;
    private LogUtils logUtils;
    private String moduleName = "";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        logUtils = LogUtils.newLog(processingEnvironment.getMessager());
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filerUtils = processingEnvironment.getFiler();
        iInterceptor = elementUtils.getTypeElement(Constants.IINTERCEPTOR).asType();

        Map<String, String> options = processingEnvironment.getOptions();
        if (!Tools.isEmpty(options)) {
            moduleName = options.get(Constants.ARGUMENTS_NAME);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (!Tools.isEmpty(annotations)) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Interceptor.class);
            try {
                parseInterceptor(elements);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void parseInterceptor(Set<? extends Element> elements) throws IOException {
        if (!Tools.isEmpty(elements)) {
            for (Element element : elements) {
                if (verify(element)) {
                    Interceptor interceptor = element.getAnnotation(Interceptor.class);
                    interceptors.put(interceptor.priority(), element);
                }
            }

            TypeElement iInterceptor = elementUtils.getTypeElement(Constants.IINTERCEPTOR);
            TypeElement iInterceptorGroup = elementUtils.getTypeElement(Constants.IINTERCEPTOR_GROUP);

            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                    ClassName.get(Map.class),
                    ClassName.get(Integer.class),
                    ParameterizedTypeName.get(
                            ClassName.get(Class.class),
                            WildcardTypeName.subtypeOf(ClassName.get(iInterceptor))
                    )
            );

            /**
             * 参数+变量名
             * Map<String, Class<? extends IInterceptor>> interceptors
             */
            ParameterSpec parameterSpec = ParameterSpec.builder(parameterizedTypeName, "interceptors").build();

            /**
             * 构建方法
             * public void loadInto(Map<String, Class<? extends IInterceptor>> interceptors){}
             */
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.METHOD_LOAD_INTO)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(parameterSpec);
            if (!interceptors.isEmpty() && interceptors.size() > 0) {
                /**
                 * 构建方法体重的语句
                 */
                for (Map.Entry<Integer, Element> entry : interceptors.entrySet()) {
                    methodBuilder.addStatement("interceptors.put(" + entry.getKey() + ", $T.class)",
                    ClassName.get((TypeElement)entry.getValue()));
                }
            }

            /**
             * 将文件写入到磁盘
             * app/build/source/api/debug/PACKAGE_OF_GENERATE_FILE下面
             */
            JavaFile.builder(Constants.PACKAGE_OF_GENERATE_FILE,
                    TypeSpec.classBuilder(Constants.NAME_OF_INTERCEPTOR + moduleName)
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(methodBuilder.build())
                            .addSuperinterface(ClassName.get(iInterceptorGroup)).build())
                    .build().writeTo(filerUtils);
        }
    }

    private boolean verify(Element element) {
        Interceptor interceptor = element.getAnnotation(Interceptor.class);
        return interceptor != null && ((TypeElement)element).getInterfaces().contains(iInterceptor);
    }
}
