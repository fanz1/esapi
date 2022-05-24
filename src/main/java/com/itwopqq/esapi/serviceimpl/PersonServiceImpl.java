package com.itwopqq.esapi.serviceimpl;

import com.itwopqq.esapi.mapper.PersonMapper;
import com.itwopqq.esapi.model.Person;
import com.itwopqq.esapi.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通用 serviceImpl 代码生成器
 *
 * @author business.generator
 * @description
 */
@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonMapper personMapper;

    @Override
    public void createData(Person person) {

        personMapper.insertUseGeneratedKeys(person);
    }

    @Override
    public void createDatas(List<Person> personList) {
        if (CollectionUtils.isEmpty(personList)) {
            return;
        }
        personMapper.insertList(personList);
    }

    @Override
    public List<Person> pageByThanId(Long id, Integer size) {

        if (null == id) {
            id = 0L;
        }
        return personMapper.pageByThanId(id, size);
    }
}




