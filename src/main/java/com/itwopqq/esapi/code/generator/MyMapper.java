package com.itwopqq.esapi.code.generator;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author fanzhen
 * @desx
 * @date 2020-04-11
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
