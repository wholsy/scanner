package com.yueny.scanner.util;

import java.net.URL;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 21:13
 */
public class URLUtils {
    /**
     * url中文件类型前缀
     */
    public static final String JAR_URL_FILE_PREFIX = "file:";

    /**
     * 从url中获取jar文件真实路径
     * <p>
     * jar文件url示例如下：<p>
     * jar:file:/Users/caches/mod/org.projectlombok/lombok/1.18.4/7f96/lombok-1.18.4.jar!/org
     *
     * @param url
     * @return
     */
    public static String getJarPathFormUrl(URL url) {
        String file = url.getFile();
        String jarRealPath = file.substring(0, file.lastIndexOf("!"))
                .replaceFirst(JAR_URL_FILE_PREFIX, "");
        return jarRealPath;
    }

}
