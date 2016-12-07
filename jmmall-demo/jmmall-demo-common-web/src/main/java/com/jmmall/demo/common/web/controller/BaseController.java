package com.jmmall.demo.common.web.controller;

import org.springframework.ui.ModelMap;

/**
 * @描述：基础控制器
 * @作者：renais
 * @创建时间：2016-12-6,下午5:04:09
 * @版本： 1.0
 */
public abstract class BaseController {
	protected void createResult(ModelMap model, Object result) {
		model.addAttribute("result", result);
	}
}
