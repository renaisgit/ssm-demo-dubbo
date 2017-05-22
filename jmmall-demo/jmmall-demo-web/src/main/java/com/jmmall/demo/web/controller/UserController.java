package com.jmmall.demo.web.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.rpc.RpcContext;
import com.jmmall.demo.common.page.PageBean;
import com.jmmall.demo.common.query.BaseQuery;
import com.jmmall.demo.common.web.controller.BaseController;
import com.jmmall.demo.model.SysUser;
import com.jmmall.demo.service.api.UserService;
import com.jmmall.demo.service.query.UserQuery;

/**
 * UserController
 * 
 * @author CHEN SHUAI
 * 
 */
@Controller
@RequestMapping(value = { "/user" })
public class UserController extends BaseController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/userLogin" })
	public void login(ModelMap model, HttpServletRequest request) {
	}

	@RequestMapping(value = { "/index" })
	public void index(ModelMap model, HttpServletRequest request) {
		System.out.println();
	}

	@RequestMapping(value = { "/{id}" })
	public void show(ModelMap model, HttpServletRequest request, @RequestParam("id") String id) {
		System.out.println(id);
	}

	@RequestMapping(value = { "/userList" })
	public void list(ModelMap model, HttpServletRequest request) throws InterruptedException, ExecutionException {
		BaseQuery query = new UserQuery();
		userService.findByQuery(query);
		Future<List<SysUser>> fooFuture = RpcContext.getContext().getFuture(); 
		List<SysUser> userList= fooFuture.get();
		createResult(model,userList);
		model.addAttribute("chenshuai", "chenshuai");
		model.addAttribute("aaa", "aaa");
	}

	@RequestMapping(value = { "/userPage" })
	public void userPage(ModelMap model, HttpServletRequest request){
		createResult(model,userService.findPageByQuery(new UserQuery()));
//		return new ModelAndView("user/userList");
	}

}
