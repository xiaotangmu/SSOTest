package com.tan.sso.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.tan.sso.annotations.LoginRequired;
import com.tan.sso.bean.UmsMember;
import com.tan.sso.service.UserService;
import com.tan.sso.util.JwtUtil;

@Controller
public class PassportController {

	@Autowired
	UserService userService;

//	@RequestMapping("vlogin")
//    public String vlogin(String code,HttpServletRequest request){
//
//        // 授权码换取access_token
//        // 换取access_token
//        // client_secret=f043fe09dcab7e9b90cdd7491e282a8f
//        // client_id=2173054083
//        String s3 = "https://api.weibo.com/oauth2/access_token?";
//        Map<String,String> paramMap = new HashMap<>();
//        paramMap.put("client_id","2173054083");
//        paramMap.put("client_secret","f043fe09dcab7e9b90cdd7491e282a8f");
//        paramMap.put("grant_type","authorization_code");
//        paramMap.put("redirect_uri","http://passport.gmall.com:8085/vlogin");
//        paramMap.put("code",code);// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
//        String access_token_json = HttpclientUtil.doPost(s3, paramMap);
//
//        Map<String,Object> access_map = JSON.parseObject(access_token_json,Map.class);
//
//        // access_token换取用户信息
//        String uid = (String)access_map.get("uid");
//        String access_token = (String)access_map.get("access_token");
//        String show_user_url = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
//        String user_json = HttpclientUtil.doGet(show_user_url);
//        Map<String,Object> user_map = JSON.parseObject(user_json,Map.class);
//
//        // 将用户信息保存数据库，用户类型设置为微博用户
//        UmsMember umsMember = new UmsMember();
//        umsMember.setSourceType("2");
//        umsMember.setAccessCode(code);
//        umsMember.setAccessToken(access_token);
//        umsMember.setSourceUid((String)user_map.get("idstr"));
//        umsMember.setCity((String)user_map.get("location"));
//        umsMember.setNickname((String)user_map.get("screen_name"));
//        String g = "0";
//        String gender = (String)user_map.get("gender");
//        if(gender.equals("m")){
//            g = "1";
//        }
//        umsMember.setGender(g);
//
//        UmsMember umsCheck = new UmsMember();
//        umsCheck.setSourceUid(umsMember.getSourceUid());
//        UmsMember umsMemberCheck = userService.checkOauthUser(umsCheck);
//
//        if(umsMemberCheck==null){
//            userService.addOauthUser(umsMember);
//        }else{
//            umsMember = umsMemberCheck;
//        }
//
//        // 生成jwt的token，并且重定向到首页，携带该token
//        String token = null;
//        String memberId = umsMember.getId();
//        String nickname = umsMember.getNickname();
//        Map<String,Object> userMap = new HashMap<>();
//        userMap.put("memberId",memberId);
//        userMap.put("nickname",nickname);
//
//
//        String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
//        if(StringUtils.isBlank(ip)){
//            ip = request.getRemoteAddr();// 从request中获取ip
//            if(StringUtils.isBlank(ip)){
//                ip = "127.0.0.1";
//            }
//        }
//
//        // 按照设计的算法对参数进行加密后，生成token
//        token = JwtUtil.encode("2019gmall0105", userMap, ip);
//
//        // 将token存入redis一份
//        userService.addUserToken(token,memberId);
//
//
//        return "redirect:http://search.gmall.com:8083/index?token="+token;
//    }

	@RequestMapping("verify")
	@ResponseBody
	public String verify(String token, String currentIp, HttpServletRequest request) {

		// 通过jwt校验token真假
		Map<String, String> map = new HashMap<>();

		Map<String, Object> decode = JwtUtil.decode(token, "tanSso2019", currentIp);// key 和 ip 在实际生产要再加密

		if (decode != null) {
			map.put("status", "success");
			map.put("userId", (String) decode.get("userId"));
			map.put("userName", (String) decode.get("userName"));
		} else {
			map.put("status", "fail");
		}

		return JSON.toJSONString(map);
	}

	@PostMapping("login")
	@ResponseBody
	public String login(UmsMember umsMember, HttpServletRequest request) {

		String token = "";

		// 调用用户服务验证用户名和密码
		UmsMember umsMemberLogin = userService.login(umsMember);
		System.out.println(umsMemberLogin);

		if (umsMemberLogin != null) {
			// 登录成功

			// 用jwt制作token
			String userId = umsMemberLogin.getId() + "";
			String userName = umsMemberLogin.getUserName();
			Map<String, Object> userMap = new HashMap<>();
			userMap.put("userId", userId);
			userMap.put("userName", userName);

			String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
			if (StringUtils.isBlank(ip)) {
				ip = request.getRemoteAddr();// 从request中获取ip
				if (StringUtils.isBlank(ip)) {
					ip = "127.0.0.1";//都没有，出错，这里直接给了
				}
			}

			// 按照设计的算法对参数进行加密后，生成token
			token = JwtUtil.encode("tanSso2019", userMap, ip);

			//将token存入redis一份
			userService.addUserToken(token, userId);

		} else {
			// 登录失败
			token = "fail";
		}

		return token;
	}

	@RequestMapping("index")
	@LoginRequired(loginSuccess = false)
	public String index(String ReturnUrl, ModelMap map) {

		map.put("ReturnUrl", ReturnUrl);
		return "login";
	}
}
