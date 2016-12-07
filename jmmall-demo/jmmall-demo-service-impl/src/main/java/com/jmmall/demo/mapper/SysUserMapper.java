package com.jmmall.demo.mapper;

import java.util.List;

import com.jmmall.demo.common.core.mapper.BaseMapper;
import com.jmmall.demo.common.query.BaseQuery;
import com.jmmall.demo.model.SysUser;

/**
 * 用户mapper
 * @author CHEN SHUAI
 *
 */
public interface SysUserMapper extends BaseMapper<SysUser, String>{
	
	List<SysUser> getSysUserListPage(BaseQuery query);
	
}