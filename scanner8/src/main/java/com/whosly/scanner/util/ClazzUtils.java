package com.whosly.scanner.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 类工具
 *
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 21:11
 */
public class ClazzUtils {
    /**
     * 匿名内部类匹配表达式
     */
    private static final Pattern ANONYMOUS_INNER_CLASS_PATTERN = Pattern.compile("^[\\s\\S]*\\${1}\\d+\\.class$");

    /**
     * 根据文件后缀名判断是否Class文件
     *
     * @param fileName
     * @return
     */
    public static boolean isClass(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return false;
        }
        return fileName.endsWith(".class");
    }

    /**
     * Class文件名转为类名（即去除后缀名）
     *
     * @param classFileName
     * @return
     */
    public static String classFile2SimpleClass(String classFileName) {
        return StringUtils.substringBeforeLast(classFileName, ".class");
    }

    /**
     * 根据类名判断是否匿名内部类
     *
     * @param className
     * @return
     */
    public static boolean isAnonymousInnerClass(String className) {
        return ANONYMOUS_INNER_CLASS_PATTERN.matcher(className).matches();
    }

}
