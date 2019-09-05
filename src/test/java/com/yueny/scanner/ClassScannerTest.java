package com.yueny.scanner;

import com.yueny.scanner.api.IScanner;
import com.yueny.scanner.config.ScanConfig;
import com.yueny.scanner.test.st.anno.St;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 21:18
 */
@Slf4j
public class ClassScannerTest {
    private final List<String> scanPkgs = Arrays.asList(
            "org", "lombok", "com.sun", "javax",
            "com.yueny");
    private IScanner classScanner = new Scanner();

    /**
     * 测试用例：扫描多个包
     */
    @Test
    public void testScan() {
        List<Class<?>> classList = classScanner.scan(scanPkgs);
        Assert.assertTrue(classList.size() > 0);
        log.info("共扫描到{}个类", classList.size());
    }

    /**
     * 测试用例：扫描多个包下带有Scannable注解的类
     */
    @Test
    public void testScanByAnnotation() {
        List<Class<?>> classList = classScanner.scan(scanPkgs, Deprecated.class);
        Assert.assertTrue(classList.size() > 0);
        log.info("共扫描到{}个类", classList.size());

        classList = classScanner.scan(scanPkgs, St.class);
        Assert.assertTrue(classList.size() == 2);
        log.info("共扫描到{}个类", classList.size());

        ScanConfig scanConfig = ScanConfig.builder()
                .basePackages(scanPkgs)
                .annotation(St.class)
                .build();
        log.info("扫描配置: {}.", scanConfig);
    }

}
