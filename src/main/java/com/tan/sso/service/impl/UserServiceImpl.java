package com.tan.sso.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tan.sso.bean.UmsMember;
import com.tan.sso.mapper.UserMapper;
import com.tan.sso.service.UserService;
import com.tan.sso.util.RedisUtil;

import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;
	@Autowired
	RedisUtil redisUtil;

	@Override
	public UmsMember getUser(Integer id) {
		UmsMember user = userMapper.selectByPrimaryKey(id);
		return user;
	}

	@Override
	public int setUser(UmsMember user) {
		return userMapper.insertUser(user);
	}

	@Override
	public UmsMember login(UmsMember user) {

		// 先查询redis
		Jedis jedis = null;
		try {
			jedis = redisUtil.getJedis();

			if (jedis != null) {
				// userName + passwork 作为中间唯一标记
				String umsMemberStr = jedis.get("user:" + user.getUserName() + user.getPassword() + ":info");

				if (StringUtils.isNotBlank(umsMemberStr)) {
					// 密码正确
					UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
					return umsMemberFromCache;
				}
			}
			// 链接redis失败，查找数据库
			UmsMember umsMemberFromDb = getUserByUserNameAndPwd(user);

			if (umsMemberFromDb != null) {
				jedis.setex("user:" + umsMemberFromDb.getUserName() + umsMemberFromDb.getPassword() + ":info",
						60 * 60 * 24, JSON.toJSONString(umsMemberFromDb));
			}
			return umsMemberFromDb;
		} finally {
			jedis.close();
		}

	}

	@Override
	public void addUserToken(String token, String userId) {
		Jedis jedis = redisUtil.getJedis();
		try {
			jedis.setex("user:" + userId + ":token", 60 * 60 * 2, token);//有效时间为2 个钟
		} finally {
			jedis.close();
		}
	}
	
	public UmsMember getUserByUserNameAndPwd(UmsMember user){
		
		List<UmsMember> selectByExample = userMapper.select(user);// 以非空字段作为查询条件
		if (selectByExample != null && selectByExample.size() > 0) {//没有不是null，是[]
			return selectByExample.get(0);
		}
		return null;
		
	}
}
