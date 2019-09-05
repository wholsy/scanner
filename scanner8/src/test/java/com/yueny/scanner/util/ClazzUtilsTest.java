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

    @Test
    public void testIsAnonymousInnerClass(){
        String innerClassName = "com.yueny.superclub.util.strategy.scan.ContainerProxy$1";
        Assert.assertTrue(ClazzUtils.isAnonymousInnerClass(innerClassName));

        innerClassName = "com.yueny.superclub.util.strategy.scan.ContainerProxy$1.class";
        Assert.assertTrue(ClazzUtils.isAnonymousInnerClass(innerClassName));

        innerClassName = "com.yueny.superclub.util.strategy.scan.ContainerProxy";
        Assert.assertFalse(ClazzUtils.isAnonymousInnerClass(innerClassName));
    }
}
