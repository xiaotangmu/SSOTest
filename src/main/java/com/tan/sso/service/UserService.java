package com.tan.sso.service;

import com.tan.sso.bean.UmsMember;

public interface UserService {

	public UmsMember getUser(Integer id);
	
	public int setUser(UmsMember user);
	
	public UmsMember login(UmsMember user);
	
	public void addUserToken(String token, String userId);
}
