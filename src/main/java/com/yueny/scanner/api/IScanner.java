package com.yueny.scanner.api;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * 扫描器
 *
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 20:41
 */
public interface IScanner {
    /**
     * 扫描多个包下的Class
     *
     * @param basePackages 包路径
     * @return
     */
    List<Class<?>> scan(List<String> basePackages);

    /**
     * 扫描多个包下带有注解的Class
     *
     * @param basePackages 包路径
     * @param annotation
     * @return
     */
    List<Class<?>> scan(List<String> basePackages, Class<? extends Annotation> annotation);

    /**
     * 扫描多个包下类clazz的所有子类
     *
     * @param basePackages 包路径
     * @param clazz
     * @return
     */
    List<Class<?>> scan(Set<String> basePackages, Class<?> clazz);
}
