package com.example.demo;

import com.example.demo.entity.SiteUser;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
	}

	@Test
	void createUserTest(){
		SiteUser siteUser = userService.create("qweqwe","123","1@1");

	}


}
