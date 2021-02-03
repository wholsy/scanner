# jdk 1.8下的扫描工具

# 基础数据源定义
### **Maven依赖管理**
```
<dependency>
    <groupId>com.whosly</groupId>
    <artifactId>scanner8</artifactId>
    <version>version</version>
</dependency>
```
   
  
# 版本历史变更记录
### 1.0.0
    + 获取多个包下所有的Class类。
    + 获取多个包下带有指定注解的所有的Class类。
    + 获取多个包下带有指定注解或指定父接口的所有子孙Class类。
    + 允许自定义过滤接口、抽象类或实体类。

### 1.1.0
依赖调整为 release

###  1.2.0-RELEASE
    + jar上传至中央仓库。groupId： com.whosly。
    + 部署至maven中央仓库 https://search.maven.org/search?q=whosly  。

###  1.3.0-SNAPSHOT
    + 优化扫描jar下类的扫描精确度



