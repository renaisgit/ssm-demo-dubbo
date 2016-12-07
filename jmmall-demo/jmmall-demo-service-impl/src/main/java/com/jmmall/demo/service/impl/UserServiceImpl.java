package com.jmmall.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmmall.demo.common.page.PageBean;
import com.jmmall.demo.common.query.BaseQuery;
import com.jmmall.demo.mapper.SysUserMapper;
import com.jmmall.demo.model.SysUser;
import com.jmmall.demo.service.api.UserService;
import com.jmmall.demo.common.service.impl.BaseServiceImpl;

/**
 * 用户服务实现
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<SysUser, String> implements UserService {

	@Autowired
	private SysUserMapper sysUserMapper;

	public List<SysUser> findByQuery(BaseQuery query) {
		// TODO Auto-generated method stub
		return sysUserMapper.selectByQuery(query);
	}

	public PageBean findPageByQuery(BaseQuery query) {
		// TODO Auto-generated method stub
		query.setRecordList(sysUserMapper.getSysUserListPage(query));
		 return query;
	}

}
