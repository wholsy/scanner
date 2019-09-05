package com.yueny.scanner.config;

import lombok.*;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 扫描条件项
 *
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 22:59
 */
@Builder
//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@ToString
//@Accessors(chain=true)
public class ScanConfig {
    /**
     * 扫描路径
     */
    @Singular
    @NonNull
    private List<String> basePackages;

    /**
     * 扫描多个包下带有指定注解的Class. 如 @Getter 等
     */
    private Class<? extends Annotation> annotation;
}
