package com.yueny.scanner.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-05 12:35
 */
public class ClazzUtilsTest {
    @Test
    public void testClassFile2SimpleClass(){
        String packagePath = "org.springframework.instrument.classloading.ResourceOverridingShadowingClassLoader.class";
        String className = ClazzUtils.classFile2SimpleClass(packagePath);

        Assert.assertEquals(className, "org.springframework.instrument.classloading.ResourceOverridingShadowingClassLoader");
    }
}
