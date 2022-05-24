package com.itwopqq.esapi.mapper;

import com.itwopqq.esapi.code.generator.MyMapper;
import com.itwopqq.esapi.model.Person;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonMapper extends MyMapper<Person> {

    List<Person> pageByThanId(@Param(value = "id") Long id, @Param(value = "size") Integer size);
}