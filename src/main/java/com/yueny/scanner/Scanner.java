package com.yueny.scanner;

import com.yueny.scanner.api.IScanner;
import com.yueny.scanner.factory.ExecutorsFactory;
import com.yueny.scanner.util.PackageUtil;
import com.yueny.scanner.util.ScannerUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/**
 * 类扫描器实现类
 *
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 20:39
 */
public class Scanner implements IScanner {
    @Override
    public List<Class<?>> scan(List<String> basePackages) {
        //没有需要扫描的包，返回空列表
        if (CollectionUtils.isEmpty(basePackages)) {
            return Collections.emptyList();
        }

        List<Class<?>> classList = new LinkedList<>();
        //去除重复包和父子包
        List<String> realScanBasePackages = PackageUtil.distinct(basePackages);

        //创建异步线程
        List<FutureTask<List<Class<?>>>> tasks = new LinkedList<>();
        realScanBasePackages
                .forEach(pkg -> {
                    ScannerCallable call = new ScannerCallable(pkg);

                    FutureTask<List<Class<?>>> task = new FutureTask(call);
                    ExecutorsFactory.submit(new Thread(task));

                    tasks.add(task);
                });

        //等待返回结果
        tasks.parallelStream().forEach(task -> {
            try {
                classList.addAll(task.get());
            } catch (InterruptedException | ExecutionException e) {
//                log.error(e.getMessage(), e);
            }
        });

        return classList;
    }

    @Override
    public List<Class<?>> scan(List<String> basePackages, Class<? extends Annotation> annotation) {
        List<Class<?>> classList = this.scan(basePackages);

        //根据 Annotation 过滤并返回
        return classList.parallelStream()
                .filter(clz -> {
                    try {
                        if (clz.getAnnotation(annotation) == null) {
                            return false;
                        }
                    } catch (Throwable e) {
//                        log.debug(e.getMessage());
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Class<?>> scan(Set<String> basePackages, Class<?> clazz) {
        return null;
    }

    /**
     * 扫描器线程类
     */
    @AllArgsConstructor
    class ScannerCallable implements Callable<List<Class<?>>> {
        /**
         * 扫描的包名称
         */
        private String pkg;

        @Override
        public List<Class<?>> call() {
            return ScannerUtils.scan(pkg);
        }
    }
}
