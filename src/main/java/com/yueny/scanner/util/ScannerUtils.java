package com.yueny.scanner.util;

import com.yueny.scanner.config.ScanConfig;
import com.yueny.scanner.config.ScanTaskId;
import com.yueny.scanner.consts.Consts;
import com.yueny.scanner.consts.ProtocolTypes;
import com.yueny.scanner.factory.ExecutorsFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static com.yueny.scanner.consts.Consts.PACKAGE_SEPARATOR;
import static com.yueny.scanner.consts.Consts.PATH_SEPARATOR;

/**
 * 扫描工具类，实现文件扫描
 *
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 20:55
 */
@Slf4j
public class ScannerUtils {
    /**
     * 扫描某个包下所有Class类
     *
     * @param pkg 扫描报名
     * @param scanConfig 扫描条件项
     * @param scanTaskId 扫描任务对象
     * @return Class列表
     */
    public static List<Class<?>> scan(String pkg, ScanConfig scanConfig, ScanTaskId scanTaskId) {
        List<Class<?>> classList = new LinkedList<>();
        try {
            //包名转化为路径名
            String pathName = PackageUtil.package2Path(pkg);
            //获取路径下URL
            Enumeration<URL> urls = getClassLoader().getResources(pathName);
            //循环扫描路径
            classList = scanUrls(pkg, urls, scanConfig, scanTaskId);
        } catch (Exception e) {
            log.error("扫描「" + scanTaskId.getTaskId() + "」包路径" + pkg + "出错：", e);
        }

        return classList;
    }

    /**
     * 扫描多个Url路径，找出符合包名的Class类
     *
     * @param pkg
     * @param urls
     * @param scanConfig 扫描条件项
     * @param scanTaskId 扫描任务对象
     *
     * @return Class列表
     * @throws IOException
     */
    private static List<Class<?>> scanUrls(String pkg, Enumeration<URL> urls, ScanConfig scanConfig, ScanTaskId scanTaskId) {
        List<Class<?>> classList = new LinkedList<>();

        //Enumeration转list
        List<URL> urlList = new LinkedList<>();
        while (urls.hasMoreElements()) {
            urlList.add(urls.nextElement());
        }

        //创建异步任务
        List<FutureTask<List<Class<?>>>> tasks = new LinkedList<>();
        urlList.forEach(url -> {
            UrlScanCallable call = new UrlScanCallable(pkg, url, scanConfig, scanTaskId);

            FutureTask<List<Class<?>>> task = new FutureTask<>(call);
            ExecutorsFactory.submit(new Thread(task));

            tasks.add(task);
        });

        //等待并处理返回结果
        tasks.parallelStream().forEach(task -> {
            try {
                classList.addAll(task.get());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        return classList;
    }

    /**
     * 递归扫描指定文件路径下的Class文件
     *
     * @param pkg
     * @param filePath
     * @return Class列表
     */
    private static List<Class<?>> recursiveScan4Path(String pkg, String filePath, ScanConfig scanConfig, ScanTaskId scanTaskId) {
        List<Class<?>> classList = new LinkedList<>();

        //读取文件
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            return classList;
        }

        //处理类文件
        File[] classes = file.listFiles(child -> ClazzUtils.isClass(child.getName()));
        Arrays.asList(classes).forEach(child -> {
            // child eg. /Users/xiaobai/workspace/yueny/github/scanner/target/test-classes/com/yueny/scanner/ClassScannerTest.class
            // className eg. com.yueny.scanner.ClassScannerTest
            String className = ClazzUtils.classFile2SimpleClass(
                    new StringBuilder().append(pkg).append(PACKAGE_SEPARATOR).append(child.getName()).toString()
            );

            //加载类文件
            Class clz = getClazz(className, scanConfig, scanTaskId);
            if(clz != null){
                classList.add(clz);
            }
        });

        //处理目录
        File[] dirs = file.listFiles(child -> child.isDirectory());
        Arrays.asList(dirs).forEach(child -> {
            String childPackageName = new StringBuilder()
                    .append(pkg)
                    .append(PACKAGE_SEPARATOR)
                    .append(child.getName())
                    .toString();
            String childPath = new StringBuilder()
                    .append(filePath)
                    .append(PATH_SEPARATOR)
                    .append(child.getName())
                    .toString();
            classList.addAll(
                    recursiveScan4Path(childPackageName, childPath, scanConfig, scanTaskId)
            );
        });

        return classList;
    }

    /**
     * 递归扫描Jar文件内的Class类
     *
     * @param pkg
     * @param jarPath
     * @return Class列表
     * @throws IOException
     */
    private static List<Class<?>> recursiveScan4Jar(String pkg, String jarPath, ScanConfig scanConfig, ScanTaskId scanTaskId) throws IOException {
        List<Class<?>> classList = new LinkedList<>();

        //读取Jar文件
        JarInputStream jin = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jin.getNextJarEntry();
        while (entry != null) {
            // eg. com/sun/java/swing/action/AboutAction.class
            String name = entry.getName();
            entry = jin.getNextJarEntry();

            //包名不匹配，跳过
            if (!name.contains(PackageUtil.package2Path(pkg))) {
                continue;
            }

            //判断是否类文件
            if (ClazzUtils.isClass(name)) {
                if (ClazzUtils.isAnonymousInnerClass(name)) {
                    //是匿名内部类，跳过
                    continue;
                }

                //文件名转类名. eg. com.sun.java.swing.action.AboutAction
                String className = ClazzUtils.classFile2SimpleClass(PackageUtil.path2Package(name));

                // test
                if(StringUtils.startsWith(className, "org.springframework.instrumentloading")){
                    log.info("className {} .",className);
                }

                //加载类文件
                Class clz = getClazz(className, scanConfig, scanTaskId);
                if(clz != null){
                    classList.add(clz);
                }
            }
        }

        return classList;
    }

    /**
     * 通过类名得到Class类
     *
     * @param className 类名
     * @return Class类
     */
    private static Class<?> getClazz(String className, ScanConfig scanConfig, ScanTaskId scanTaskId) {
        try {
            //加载类文件
            Class clz = getClassLoader().loadClass(className);

            // 基本数据类型
            if(clz.isPrimitive()){
                return null;
            }

            // 判断是否是指定注解的 Class. 扫描的是被该注解标注的所有类
            if(scanConfig.getAnnotation() != null){
                if (clz.getAnnotation(scanConfig.getAnnotation()) == null) {
                    // 该类不存在注解， 则不添加
                    return null;
                }

                // 不是注解自身
                if(StringUtils.equals(clz.getCanonicalName(), scanConfig.getAnnotation().getCanonicalName())){
                    return null;
                }
            }

            // 扫描的是该接口或类的所有子接口/子类，但不包含自身
            if (scanConfig.getClazz() != null) {
                if (!scanConfig.getClazz().isAssignableFrom(clz)) {
                  // clz 不是 scanConfig.getClazz() 的子类
                  return null;
                }

                // 不是自身
                if (StringUtils.equals(clz.getCanonicalName(), scanConfig.getClazz().getCanonicalName())) {
                  return null;
                }

                // 到这的，都为子孙类了
                // 匿名内部类或者注解，直接抛弃
                if(clz.isAnonymousClass() || clz.isAnnotation()){
                    return null;
                }else {
                    if(clz.isInterface()){
                        if (!scanConfig.getClazzTypes().contains(ScanConfig.ClazzType.INTERFACE)) {
                            // 不允许包含接口
                            return null;
                        }
                    }else{
                        /* 普通类，抽象类 */
                        // 抽象类
                        if(Modifier.isAbstract(clz.getModifiers())){
                            if (!scanConfig.getClazzTypes().contains(ScanConfig.ClazzType.ABSTRACT)) {
                                // 不允许包含抽象类
                                return null;
                            }
                        }else{
                            // 普通类
                            if (!scanConfig.getClazzTypes().contains(ScanConfig.ClazzType.CLASS)) {
                                // 不允许包含普通类
                                return null;
                            }
                        }
                    }
                }
            }

            return clz;
        } catch (ClassNotFoundException | LinkageError e) {
            // getClassLoader().loadClass(className) 可能会报找不到类的异常，此处忽略
            log.debug("「" + scanTaskId.getTaskId() + "」cannot load className: {}, mesaage: {}.", className, e.getMessage());
        }

        return null;
    }

    /**
     * 获取 ClassLoader
     */
    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * URL扫描线程类, 根据 URL 得到 Class
     */
    @AllArgsConstructor
    static class UrlScanCallable implements Callable<List<Class<?>>> {
        /**
         * 需匹配的包名
         */
        private String pkg;
        /**
         * 需扫描的URL
         */
        private URL url;

        /**
         * 扫描条件项
         */
        private ScanConfig scanConfig;

        /**
         * 扫描任务对象
         */
        private ScanTaskId scanTaskId;

        @Override
        public List<Class<?>> call() {
            return scan(pkg, url, scanConfig, scanTaskId);
        }
    }

    private static List<Class<?>> scan(String pkg, URL url, ScanConfig scanConfig, ScanTaskId scanTaskId) {
        //获取协议
        String protocol = url.getProtocol();

        List<Class<?>> classList = new LinkedList<>();
        try {
            if (ProtocolTypes.file.name().equals(protocol)) {
                //文件
                String path = URLDecoder.decode(url.getFile(), Consts.DEFAULT_CHARSET);
                classList.addAll(recursiveScan4Path(pkg, path, scanConfig, scanTaskId));
            } else if (ProtocolTypes.jar.name().equals(protocol)) {
                //jar包
                String jarPath = URLUtils.getJarPathFormUrl(url);
                classList.addAll(recursiveScan4Jar(pkg, jarPath, scanConfig, scanTaskId));
            }
        } catch (Exception e) {
            log.error("扫描「" + scanTaskId.getTaskId() + "」出错:", e);
        }

        return classList;
    }

}
