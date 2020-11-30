package com.whosly.scanner.config;

import lombok.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * 扫描条件项
 *
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 22:59
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ScanConfig {
    /**
     * 扫描路径
     */
    @Singular
    @NonNull
    private List<String> basePackages;

    /**
     * 扫描多个包下带有指定注解的Class. 如 @Getter 等。
     * 该值存在的时候，扫描的是被该注解标注的所有类
     */
    private Class<? extends Annotation> annotation;

    /**
     * 类或接口类型
     * 该值存在的时候，扫描的是该接口或类的所有子接口/子类，但不包含自身
     */
    private Class<?> clazz;

    /**
     * clazz对应允许的类型，如接口，类实例， 抽象类
     *
     * 默认为普通类、接口、内部类接口和内部类
     */
    @Builder.Default
    private List<ClazzType> clazzTypes = Arrays.asList(ClazzType.CLASS, ClazzType.INTERFACE);

    public enum ClazzType{
        /**
         * 接口， 包含在接口/普通类中声明的内部接口
         */
        INTERFACE,
        /**
         * 普通类， 包含在普通类中声明的内部类
         */
        CLASS,
        /**
         * 抽象类
         */
        ABSTRACT;
    }
}
