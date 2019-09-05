package com.yueny.scanner.factory;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.yueny.rapid.lang.thread.factory.NamedThreadFactory;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 20:58
 */
public class ExecutorsFactory {
    /**
     * 线程池
     */
    private static ListeningExecutorService executor = MoreExecutors.listeningDecorator(
            MoreExecutors.getExitingExecutorService(
                    new ThreadPoolExecutor(
                            Runtime.getRuntime().availableProcessors()*2,
                            Integer.MAX_VALUE,
                            5,
                            TimeUnit.SECONDS,
                            new SynchronousQueue<>(),
                            NamedThreadFactory.create("class-scanner-thread-"))
            ));

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
