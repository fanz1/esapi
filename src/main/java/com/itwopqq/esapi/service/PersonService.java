package com.itwopqq.esapi.service;

import com.itwopqq.esapi.model.Person;

import java.util.List;

/**
 * 通用 service 代码生成器
 *
 * @author business.generator
 * @description
 */
public interface PersonService {


    void createData(Person person);

    void createDatas(List<Person> personList);

    List<Person> pageByThanId(Long id, Integer size);
}




