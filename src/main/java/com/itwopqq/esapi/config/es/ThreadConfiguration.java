package com.itwopqq.esapi.config.es;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fanzhen
 * @desx
 * @date 5/4/22
 */
@Configuration
public class ThreadConfiguration {

    private ThreadPoolExecutor threadPoolExecutor;


    @Bean
    public ThreadPoolExecutor getThreadPool(){
        //给线程命名
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat( "test-thread-%d").build();
        //设置线程池参数
        threadPoolExecutor = new ThreadPoolExecutor(100, 100, 100,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024), namedThreadFactory);
        //设置拒绝策略
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolExecutor;
    }
}
