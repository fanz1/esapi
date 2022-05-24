package com.itwopqq.esapi.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

@Document(indexName = "ngram_esapi", type = "ngram_person")
@Data
public class Person {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 年龄
     */
    @Column(name = "AGE")
    private Integer age;

    /**
     * 地址
     */
    @Column(name = "ADDRESS")
    private String address;

    /**
     * 不分词的地址字段,用来测试模糊搜索的效果
     */
    private String addressNoAnalysis;
}