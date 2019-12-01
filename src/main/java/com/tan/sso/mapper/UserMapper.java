package com.tan.sso.mapper;

import com.tan.sso.bean.UmsMember;

import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<UmsMember> {
	
	public int insertUser(UmsMember user);

}
