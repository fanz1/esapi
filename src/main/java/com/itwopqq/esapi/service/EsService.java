package com.itwopqq.esapi.service;

import com.itwopqq.esapi.model.Person;

import java.io.IOException;
import java.util.List;

public interface EsService {

    void createData(Person person) throws IOException;


    /***
     * @desc 批量添加数据
     * @author fanzhen
     * @date 5/15/22 11:56 AM
     * @param
     * @param persons
     * @return void
     */
    void createDatas(List<Person> persons) throws Exception;

    /***
     * @desc 通过wildcard的方式实现模糊查询的功能
     * @author fanzhen
     * @date 5/8/22 10:11 PM
     * @param
     * @param keyword
     * @return java.util.List<com.itwopqq.esapi.model.Person>
     */
    List<Person> searchByWildcard(String keyword, String index, String type) throws Exception;

    /***
     * @desc 通过倒排索引+match_phrase方式实现模糊检索的功能
     * @author fanzhen
     * @date 5/8/22 10:11 PM
     * @param
     * @param keyword
     * @return java.util.List<com.itwopqq.esapi.model.Person>
     */
    List<Person> searchByNgramSearch(String keyword, String index, String type) throws Exception;

    void updateData(Person person) throws Exception;
}
