package com.tan.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tan.sso.bean.UmsMember;
import com.tan.sso.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping("get")
	public Object get(Integer id) {
		System.out.println("id： " + id);
		UmsMember user = userService.getUser(id);
		System.out.println(user);
		return user;
	}
	
	@GetMapping("insert")
	public Object get(String userName) {
		System.out.println("userName： " + userName);
		UmsMember user = new UmsMember(null, userName, null);
		return userService.setUser(user);
	}
}
