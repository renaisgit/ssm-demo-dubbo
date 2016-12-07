package com.jmmall.demo.common.service;

import java.util.List;

import com.jmmall.demo.common.query.BaseQuery;

/**
 * @描述：公共服务
 * @作者：renais
 * @创建时间：2016-12-6,下午4:59:45
 * @版本： 1.0
 */
public interface BaseService<E, PK> {

	public List<E> findByQuery(BaseQuery query);
}
