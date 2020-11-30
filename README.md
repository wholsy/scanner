# scanner
### 介绍
scanner 为一个脱离 spring 环境的包路径(package)下的类扫描工具。
用于获取指定包下的Class类，同时可根据指定Annotation或父类接口进行过滤。

### 使用说明
项目已上传Maven中央仓库。
> https://search.maven.org/search?q=whosly

#### 1.注意事项
- 项目JDK 版本 1.8+
- 基线 yueny-parent 1.3.0。 请提前部署。
> https://github.com/yueny/base/blob/master/yueny/pom.xml
- 组件依赖明细
```xml
        <dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
		</dependency>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-collections4</artifactId>
		</dependency>
		<dependency>
	        <groupId>org.projectlombok</groupId> 
	        <artifactId>lombok</artifactId>
	    </dependency>
		<dependency>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
		</dependency>
```

#### 2.依赖
- 添加依赖
在项目添加以下依赖包：
```
<dependency>
    <groupId>com.whosly</groupId>
    <artifactId>scanner8</artifactId>
    <version>version</version>
</dependency>
```

#### 3. 使用手册
- 示例代码
```java
package com.whosly.scanner.util;

import com.google.common.collect.Sets;
import com.whosly.scanner.Scanner;
import com.whosly.scanner.api.IScanner;
import com.whosly.scanner.config.ScanConfig;
import com.whosly.scanner.util.st.ISt;
import com.whosly.scanner.util.st.anno.St;
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
public class ScannerTest {
    private final List<String> scanPkgs = Arrays.asList(
            "org", "lombok", "com.sun", "javax",
            "com.whosly");
    private IScanner scanner = new Scanner();

    /**
     * 测试用例：扫描多个包
     */
    @Test
    public void testScan() {
        List<Class<?>> classList = scanner.scan(scanPkgs);
        Assert.assertTrue(classList.size() > 0);
        log.info("共扫描到{}个类", classList.size());
    }

    /**
     *
     */
    @Test
    public void testScanByAnnotation() {
        List<Class<?>> classList = scanner.scan(scanPkgs, Deprecated.class);
        Assert.assertTrue(classList.size() > 0);
        log.info("共扫描到{}个类", classList.size());

        classList = scanner.scan(scanPkgs, St.class);
        Assert.assertTrue(classList.size() == 3);
        log.info("共扫描到{}个类", classList.size());

        ScanConfig scanConfig = ScanConfig.builder()
                .basePackages(scanPkgs)
                .annotation(St.class)
                .build();
        log.info("扫描配置: {}.", scanConfig);
    }


    /**
     */
    @Test
    public void testScanByClass() {
        List<Class<?>> classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.whosly")), ISt.class);
        Assert.assertTrue(classList.size() == 7);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.whosly")), ISt.class, Arrays.asList(ScanConfig.ClazzType.INTERFACE, ScanConfig.ClazzType.CLASS, ScanConfig.ClazzType.ABSTRACT));
        Assert.assertTrue(classList.size() == 8);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.whosly")), ISt.class, Arrays.asList(ScanConfig.ClazzType.INTERFACE, ScanConfig.ClazzType.CLASS));
        Assert.assertTrue(classList.size() == 7);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.whosly")), ISt.class, Arrays.asList(ScanConfig.ClazzType.INTERFACE));
        Assert.assertTrue(classList.size() == 2);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.whosly")), ISt.class, Arrays.asList(ScanConfig.ClazzType.CLASS));
        Assert.assertTrue(classList.size() == 5);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.whosly")), ISt.class, Arrays.asList(ScanConfig.ClazzType.ABSTRACT));
        Assert.assertTrue(classList.size() == 1);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

    }

}


```


### 联系方式
- Email：deep_blue_yang@126.com