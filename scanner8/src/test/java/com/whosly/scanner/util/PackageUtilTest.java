package com.whosly.scanner.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-05 12:37
 */
public class PackageUtilTest {
    @Test
    public void testPath2Package(){
        String name = "org/springframework/instrument/classloading/ResourceOverridingShadowingClassLoader.class";
        String packagePath = PackageUtil.path2Package(name);

        Assert.assertEquals(packagePath, "org.springframework.instrument.classloading.ResourceOverridingShadowingClassLoader.class");
    }
}
