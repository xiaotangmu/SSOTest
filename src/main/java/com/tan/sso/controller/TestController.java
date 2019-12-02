package com.tan.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public String success(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("userId: " + request.getParameter("userId") +
				", userName: " + (String) request.getAttribute("userName"));
		//注意因为使setAttr放置的参数，所以用getAttr获取 -- 数据能够在服务器间传递，不要用getParameter -- 获取路径上的哪些参数
		
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
