# scanner
### 介绍
scanner 为一个脱离spring环境的包路径(package)下的类扫描工具。
用于获取指定包下的Class类，同时可根据指定Annotation或父类接口进行过滤。

### 使用说明
项目没有上传Maven中央仓库，目前在私有maven仓库中心。需要的=自行下载并deploy到私服。

#### 1.注意事项
- 项目JDK 版本 1.8+
- 基线 yueny-parent 1.1.6。 请提前部署。
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

		<!-- 自有线程池工具 -->
		<dependency>
			<groupId>com.yueny.rapid</groupId>
			<artifactId>rapid-lang-thread</artifactId>
			<version>1.0.1-SNAPSHOT</version>
		</dependency>
```

#### 2.依赖
- 添加依赖
在项目添加以下依赖包：
```xml
<dependency>
    <groupId>com.yueny</groupId>
    <artifactId>scanner8</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

#### 3. 使用手册
- 示例代码
```java
package com.yueny.scanner;

import com.google.common.collect.Sets;
import com.yueny.scanner.api.IScanner;
import com.yueny.scanner.config.ScanConfig;
import com.yueny.scanner.test.st.ISt;
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
public class ScannerTest {
    private final List<String> scanPkgs = Arrays.asList(
            "org", "lombok", "com.sun", "javax",
            "com.yueny");
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
                "com.aaa",  "com.yueny")), ISt.class);
        Assert.assertTrue(classList.size() == 7);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.yueny")), ISt.class, Arrays.asList(ScanConfig.ClazzType.INTERFACE, ScanConfig.ClazzType.CLASS, ScanConfig.ClazzType.ABSTRACT));
        Assert.assertTrue(classList.size() == 8);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.yueny")), ISt.class, Arrays.asList(ScanConfig.ClazzType.INTERFACE, ScanConfig.ClazzType.CLASS));
        Assert.assertTrue(classList.size() == 7);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.yueny")), ISt.class, Arrays.asList(ScanConfig.ClazzType.INTERFACE));
        Assert.assertTrue(classList.size() == 2);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.yueny")), ISt.class, Arrays.asList(ScanConfig.ClazzType.CLASS));
        Assert.assertTrue(classList.size() == 5);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

        classList = scanner.scan(Sets.newHashSet(Arrays.asList(
                "com.aaa",  "com.yueny")), ISt.class, Arrays.asList(ScanConfig.ClazzType.ABSTRACT));
        Assert.assertTrue(classList.size() == 1);
        log.info("共扫描到{}个类:{}.", classList.size(), classList);

    }

}

```


### 版本历史变更记录
* 1.0.0
    + 获取多个包下所有的Class类。
    + 获取多个包下带有指定注解的所有的Class类。
    + 获取多个包下带有指定注解或指定父接口的所有子孙Class类。
    + 允许自定义过滤接口、抽象类或实体类。
* 1.1.0
    +  spring支持(scanner-spring)   
    + 匿名内部类的表达式判断
    + 扫描多个包下带有指定注解和接口的所有子类

### 项目结构
scanner
- scanner8：jdk 1.8下的核心包
- scanner-spring：Spring Boot扩展包，支持注解配置和生效


### 联系方式
- Email：deep_blue_yang@126.com