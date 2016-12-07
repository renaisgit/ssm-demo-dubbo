package com.jmmall.demo.service.api;

import com.jmmall.demo.common.page.PageBean;
import com.jmmall.demo.common.query.BaseQuery;
import com.jmmall.demo.common.service.BaseService;
import com.jmmall.demo.model.SysUser;

/**
 * @描述：用户服务
 * @作者：renais
 * @创建时间：2016-12-6,下午2:00:16
 * @版本： 1.0
 */
public interface UserService extends BaseService<SysUser, String>{

	public PageBean findPageByQuery(BaseQuery query);
}
