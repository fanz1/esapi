package com.itwopqq.esapi.config.es;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fanzhen
 * @desx
 * @date 5/4/22
 */

@Configuration
public class RestConfig {

    @Bean
    public RestClient getClient() {

        RestClientBuilder clientBuilder = RestClient.builder(new HttpHost("121.4.110.238", 9200, "http"));
        // 1.设置请求头
        Header[] defaultHeaders = {new BasicHeader("charset", "utf-8"), new BasicHeader("content-type", "application/json")};
        clientBuilder.setDefaultHeaders(defaultHeaders);

        //2. 设置超时时间，多次尝试同一请求时应该遵守的超时。默认值为30秒，与默认套接字超时相同。若自定义套接字超时，则应相应地调整最大重试超时
        clientBuilder.setMaxRetryTimeoutMillis(60000);

        /**
         *3.设置失败监听器，
         *每次节点失败都可以监听到，可以作额外处理
         */
        clientBuilder.setFailureListener(new RestClient.FailureListener() {

            @Override
            public void onFailure(HttpHost host) {
                super.onFailure(host);
                System.out.println(host.getHostName() + "=== 节点失败了");
            }
        });

        /** 4.配置节点选择器，客户端以循环方式将每个请求发送到每一个配置的节点上，
         *发送请求的节点，用于过滤客户端，将请求发送到这些客户端节点，默认向每个配置节点发送，
         *这个配置通常是用户在启用嗅探时向专用主节点发送请求（即只有专用的主节点应该被HTTP请求命中）
         */
//        clientBuilder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);

        /*
         *5. 配置异步请求的线程数量，Apache Http Async Client默认启动一个调度程序线程，以及由连接管理器使用的许多工作线程
         *（与本地检测到的处理器数量一样多，取决于Runtime.getRuntime().availableProcessors()返回的数量）。线程数可以修改如下,
         *这里是修改为1个线程，即默认情况
         */
        clientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultIOReactorConfig(
                IOReactorConfig.custom().setIoThreadCount(10).build()
        ));

        /**
         *6. 配置连接超时和套接字超时
         *配置请求超时，将连接超时（默认为1秒）和套接字超时（默认为30秒）增加，
         *这里配置完应该相应地调整最大重试超时（默认为30秒），即上面的setMaxRetryTimeoutMillis，一般于最大的那个值一致即60000
         */
        clientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            // 连接5秒超时，套接字连接60s超时
            return requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000);
        });

        return clientBuilder.build();
    }


    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost("121.4.110.238", 9200, "http")
        ));
    }
}
