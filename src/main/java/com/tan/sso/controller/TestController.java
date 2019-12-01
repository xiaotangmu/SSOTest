package com.tan.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tan.sso.annotations.LoginRequired;
import com.tan.sso.bean.UmsMember;
import com.tan.sso.service.UserService;

@Controller
public class TestController {

	@Autowired
	UserService userService;

	@GetMapping("success")
	@LoginRequired(loginSuccess=true)
	public String success() {
		return "success";
	}
	
	@GetMapping("testLogin")
	@ResponseBody
	public Object testLogin(UmsMember user) {
		System.out.println(user);
		UmsMember login = userService.login(user);
		System.out.println(login);
		return login;
	}
}
