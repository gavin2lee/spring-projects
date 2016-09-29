package com.gl.anything.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gl.anything.vo.GreetingVo;

@RequestMapping("/helloservice")
public class HelloWorldController {
	
	@RequestMapping("/greeting")
	public GreetingVo simplyGreeting(@RequestParam String username){
		GreetingVo vo = new GreetingVo();
		vo.setId(100);
		vo.setGreetAt(new Date());
		vo.setWords("Hi "+username);
		
		return vo;
	}
}
