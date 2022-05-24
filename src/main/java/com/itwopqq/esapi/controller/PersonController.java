package com.itwopqq.esapi.controller;

import cn.binarywang.tools.generator.ChineseAddressGenerator;
import cn.binarywang.tools.generator.ChineseNameGenerator;
import com.itwopqq.esapi.model.Person;
import com.itwopqq.esapi.service.EsService;
import com.itwopqq.esapi.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 通用 Controller 代码生成器
 *
 * @author business.generator
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private EsService esService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/createDatas")
    public String createDatas() {

        for (int i = 0; i < 50000; i++) {
            threadPoolExecutor.execute(() -> {
                List<Person> persons = new ArrayList<>();
                for (int j = 0; j < 100; j++) {
                    Person person = new Person();
                    person.setName(ChineseNameGenerator.getInstance().generate());
                    person.setAge(ThreadLocalRandom.current().nextInt(100));
                    person.setAddress(ChineseAddressGenerator.getInstance().generate());
                    persons.add(person);
                }
                personService.createDatas(persons);
                try {
                    esService.createDatas(persons);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        return "success";
    }

    /***
     * @desc 通过wildcard实现模糊检索的功能
     * @author fanzhen
     * @date 5/8/22 10:19 PM
     * @param
     * @param keyword
     * @return java.util.List<com.itwopqq.esapi.model.Person>
     */
    @GetMapping("/wildcard")
    public List<Person> wildcard(String keyword) throws Exception {

        List<Person> personList = esService.searchByWildcard(keyword, "ngram_esapi", "ngram_person");
        return personList;
    }

    /***
     * @desc 通过自建analysis+match_pharse实现模糊检索的功能
     * @author fanzhen
     * @date 5/8/22 10:18 PM
     * @param
     * @param keyword
     * @return java.util.List<com.itwopqq.esapi.model.Person>
     */
    @GetMapping("/ngramSearch")
    public List<Person> ngramSearch(String keyword) throws Exception {
        List<Person> personList = esService.searchByNgramSearch(keyword, "ngram_esapi", "ngram_person");
        return personList;
    }

    @GetMapping("/updatePerson")
    public String updatePerson() {
//        5667500

        List<Person> personList = personService.pageByThanId(5667500L, 1000);
        while (personList.size() > 0){
            CountDownLatch cdl = new CountDownLatch(personList.size());
            for (Person person : personList) {
                threadPoolExecutor.execute(() -> {
                    try {
                        esService.updateData(person);
                    } catch (Exception e) {

                    } finally {
                        cdl.countDown();
                    }
                });
            }
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            personList = personService.pageByThanId(personList.get(personList.size() - 1).getId(), 1000);
        }
        return "success";
    }
}
