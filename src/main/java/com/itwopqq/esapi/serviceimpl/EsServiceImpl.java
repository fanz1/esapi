package com.itwopqq.esapi.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.itwopqq.esapi.model.Person;
import com.itwopqq.esapi.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author fanzhen
 * @desx
 * @date 5/4/22
 */
@Service
@Slf4j
public class EsServiceImpl implements EsService {


    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void createData(Person person) throws IOException {

        IndexQueryBuilder indexQueryBuilder = new IndexQueryBuilder().withId(person.getId().toString()).withObject(person);
        elasticsearchTemplate.index(indexQueryBuilder.build());
    }

    @Override
    public void createDatas(List<Person> persons) throws Exception {
        if (CollectionUtils.isEmpty(persons)) {
            return;
        }
        // 服务器配置不行，不使用bulk的方式添加数据
//        List<IndexQuery> iqs = new ArrayList<>();
//        persons.forEach(person -> {
//            IndexQueryBuilder indexQueryBuilder = new IndexQueryBuilder().withId(person.getId().toString()).withObject(person);
//            iqs.add(indexQueryBuilder.build());
//        });
//        elasticsearchTemplate.bulkIndex(iqs);


        for (Person person : persons) {
            createData(person);
        }
    }

    @Override
    public List<Person> searchByWildcard(String keyword, String index, String type) throws Exception {

        SearchQuery searchQuery = new NativeSearchQuery(QueryBuilders.wildcardQuery("addressNoAnalysis", String.format("*%s*", keyword)));
        return elasticsearchTemplate.queryForList(searchQuery, Person.class);
    }

    @Override
    public List<Person> searchByNgramSearch(String keyword, String index, String type) throws Exception {

        SearchQuery searchQuery = new NativeSearchQuery(new MatchPhraseQueryBuilder("address", keyword));
        List<Person> personList = elasticsearchTemplate.queryForList(searchQuery, Person.class);
        return personList;
    }

    @Override
    public void updateData(Person person) throws Exception {

        try {
            UpdateRequest updateRequest = new UpdateRequest();
            String personStr = JSON.toJSONString(person);
            Map map = JSON.parseObject(personStr, Map.class);
            updateRequest.doc(map);
            UpdateQueryBuilder uqb = new UpdateQueryBuilder();
            uqb.withId(person.getId().toString());
            uqb.withUpdateRequest(updateRequest);
            uqb.withClass(Person.class);
            elasticsearchTemplate.update(uqb.build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


}
