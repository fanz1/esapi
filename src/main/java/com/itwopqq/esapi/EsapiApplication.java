package com.itwopqq.esapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.itwopqq.esapi.mapper")
public class EsapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsapiApplication.class, args);
    }

}
