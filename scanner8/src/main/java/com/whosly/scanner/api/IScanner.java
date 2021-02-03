package com.whosly.scanner.api;

import com.whosly.scanner.config.ScanConfig;

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
     * 扫描多个包下带有 指定注解的Class
     *
     * @param basePackages 包路径
     * @param annotation  指定泛型修饰的
     * @return
     */
    List<Class<?>> scanAnno(List<String> basePackages, Class<? extends Annotation> annotation);

    /**
     * 扫描多个包下类/接口clazz的所有子类
     *
     * @param basePackages 包路径
     * @param annotation  指定泛型修饰的
     * @param clazzTypes 扫描类类型
     * @return
     */
    List<Class<?>> scanAnno(List<String> basePackages, Class<? extends Annotation> annotation, List<ScanConfig.ClazzType> clazzTypes);

    /**
     * 扫描多个包下的Class
     *
     * @param basePackages 包路径
     * @return
     */
    List<Class<?>> scan(List<String> basePackages);

    /**
     * 扫描多个包下类/接口clazz的所有子类
     *
     * @param basePackages 包路径
     * @param clazz 指定的类或接口类型
     * @return
     */
    List<Class<?>> scan(Set<String> basePackages, Class<?> clazz);

    /**
     * 扫描多个包下类/接口clazz的所有子类
     *
     * @param basePackages 包路径
     * @param clazzTypes 扫描类类型
     * @return
     */
    List<Class<?>> scan(Set<String> basePackages, List<ScanConfig.ClazzType> clazzTypes);

    /**
     * 扫描多个包下类/接口clazz的所有子类
     *
     * @param basePackages 包路径
     * @param clazz 指定的类或接口类型
     * @param clazzTypes 扫描类类型
     * @return
     */
    List<Class<?>> scan(Set<String> basePackages, Class<?> clazz, List<ScanConfig.ClazzType> clazzTypes);
}
