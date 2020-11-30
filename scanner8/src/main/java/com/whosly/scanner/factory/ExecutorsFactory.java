package com.whosly.scanner.factory;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 20:58
 */
public class ExecutorsFactory {
    /**
     * 创建线程池配置
     */
    private static final ListeningExecutorService executor;
    static {
        BasicThreadFactory.Builder builder = new BasicThreadFactory.Builder();
        builder.namingPattern("class-scanner-thread-");

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors()*2,
                Integer.MAX_VALUE,
                5,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                builder.build());

        executor = MoreExecutors.listeningDecorator(
                MoreExecutors.getExitingExecutorService(poolExecutor));
    }

    /**
     * 在线程池执行线程
     *
     * @param thread
     */
    public static void submit(Thread thread) {
        executor.execute(thread);
    }

    /**
     * 在线程池执行线程
     *
     * @param runnable
     */
    public static void submit(Runnable runnable) {
        executor.execute(runnable);
    }

}
