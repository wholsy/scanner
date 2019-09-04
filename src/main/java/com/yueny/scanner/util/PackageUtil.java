package com.yueny.scanner.util;

import com.yueny.scanner.consts.Consts;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 20:46
 */
public class PackageUtil {
    /**
     * 去除重复包
     *
     * @param packages
     * @return
     */
    public static List<String> distinct(List<String> packages) {
        //去除完全重复包
        List<String> minPackages = packages.stream()
                .distinct()
                .collect(Collectors.toList());

        List<String> distinctPackages = new LinkedList<>();
        //去除父子包
        minPackages.forEach(srcPkg -> {
            long count = minPackages.stream()
                    .filter(comparePkg -> !comparePkg.equals(srcPkg) && srcPkg.startsWith(comparePkg))
                    .count();
            if (count == 0) {
                distinctPackages.add(srcPkg);
            }
        });

        return distinctPackages;
    }

    /**
     * 包名转换为路径名
     *
     * @param packageName
     * @return
     */
    public static String package2Path(String packageName) {
        return packageName.replace(Consts.PACKAGE_SEPARATOR, Consts.PATH_SEPARATOR);
    }

    /**
     * 路径名转换为包名
     *
     * @param pathName
     * @return
     */
    public static String path2Package(String pathName) {
        return pathName.replaceAll(Consts.PATH_SEPARATOR, Consts.PACKAGE_SEPARATOR);
    }

}
