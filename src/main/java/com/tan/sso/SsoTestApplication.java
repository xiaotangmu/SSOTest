package com.tan.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.tan.sso.mapper")
@SpringBootApplication
public class SsoTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsoTestApplication.class, args);
	}

}
