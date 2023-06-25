package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SampleController {

	@GetMapping("/public")
	public String getPublic() {
		return "this is public";
	}
	
	@GetMapping("/private")
	public String getPrivate() {
		log.debug("log!!");
		return "this is private";
	}
}
