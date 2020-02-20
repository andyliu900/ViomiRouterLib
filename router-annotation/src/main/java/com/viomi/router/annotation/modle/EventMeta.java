package com.viomi.router.annotation.modle;

import com.viomi.router.annotation.Event;

import javax.lang.model.element.Element;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiRouterLib
 * @Package: com.viomi.router.annotation.modle
 * @ClassName: EventMeta
 * @Description:
 * @Author: randysu
 * @CreateDate: 2020-02-18 17:51
 * @UpdateUser:
 * @UpdateDate: 2020-02-18 17:51
 * @UpdateRemark:
 * @Version: 1.0
 */
public class EventMeta {

    /**
     * 节点
     */
    private Element element;

    /**
     * 生成的文件名称
     */
    private String fileName;

    /**
     * 带包名的类名全称
     */
    private String fullJavaClassName;

    /**
     * 注解使用的类对象
     */
    private Class<?> destination;

    /**
     * 事件的key
     */
    private String key;

    /**
     * 事件所属的module
     */
    private String moduleName;

    public EventMeta() {

    }

    public static EventMeta build(String fileName, String fullJavaClassName, String key, String moduleName) {
        return new EventMeta(null, fileName, fullJavaClassName, null, key, moduleName);
    }

    public EventMeta(Element element, String fileName, String fullJavaClassName, Class<?> destination, String key, String moduleName) {
        this.element = element;
        this.fileName = fileName;
        this.fullJavaClassName = fullJavaClassName;
        this.destination = destination;
        this.key = key;
        this.moduleName = moduleName;
    }

    public EventMeta(Element element, String fileName, String fullJavaClassName, Class<?> destination, Event event, String moduleName) {
        this(element, fileName, fullJavaClassName, destination, event.key(), moduleName);
    }

    public EventMeta(Element element, String fileName, String fullJavaClassName, Event event, String moduleName) {
        this(element, fileName, fullJavaClassName, null, event.key(), moduleName);
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFullJavaClassName() {
        return fullJavaClassName;
    }

    public void setFullJavaClassName(String fullJavaClassName) {
        this.fullJavaClassName = fullJavaClassName;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
