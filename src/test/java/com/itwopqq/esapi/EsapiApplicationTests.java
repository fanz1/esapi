package com.itwopqq.esapi;

import cn.binarywang.tools.generator.ChineseAddressGenerator;
import cn.binarywang.tools.generator.ChineseNameGenerator;
import com.alibaba.fastjson.JSONObject;
import com.itwopqq.esapi.model.Person;
import com.itwopqq.esapi.service.EsService;
import com.itwopqq.esapi.service.PersonService;
import org.apache.shiro.util.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(classes = EsapiApplication.class)
@RunWith(SpringRunner.class)
public class EsapiApplicationTests {

    @Autowired
    EsService esService;

    @Autowired
    PersonService personService;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Test
    public void contextLoads() {

        System.out.println(esService);
    }

    @Test
    public void test1() throws IOException {

        Person person = new Person();
        person.setName("潘隆龙2");
        person.setAge(19);

        personService.createData(person);
        esService.createData(person);
    }

    @Test
    public void test2() {


        for (int i = 0; i < 100000; i++) {
            executorService.execute(() -> {
                Person person = new Person();
                person.setName(ChineseNameGenerator.getInstance().generate());
                person.setAge(ThreadLocalRandom.current().nextInt(100));
                person.setAddress(ChineseAddressGenerator.getInstance().generate());
                personService.createData(person);
                try {
                    esService.createData(person);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Test
    public void test3() throws Exception {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Person person = new Person();
            person.setName(ChineseNameGenerator.getInstance().generate());
            person.setAge(ThreadLocalRandom.current().nextInt(100));
            person.setAddress(ChineseAddressGenerator.getInstance().generate());
            persons.add(person);
        }
        personService.createDatas(persons);
        esService.createDatas(persons);
        System.out.println(JSONObject.toJSONString(persons));

    }

    @Test
    public void test4() throws Exception {

        List<Person> personList = personService.pageByThanId(6266574L, 1);
        if(CollectionUtils.isEmpty(personList)){
            return;
        }
        Person person = personList.get(0);
        person.setAddressNoAnalysis(person.getAddress());
        esService.updateData(person);
    }

}
