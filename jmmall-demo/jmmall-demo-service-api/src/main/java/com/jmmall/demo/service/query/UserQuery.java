package com.jmmall.demo.service.query;


import com.jmmall.demo.common.query.BaseQuery;

import lombok.Data;

/**
 * 用户查询
 * @author CHENSHUAI
 *
 */
@Data
public class UserQuery extends BaseQuery {
	private Integer userId;
}
