package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.JWTCode;
import com.example.dto.AccountContext;
import com.example.dto.TokenInfo;
import com.example.util.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@Slf4j
public class TokenController {

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@PostMapping("/token/issuAccessToken")
	public TokenInfo getAccessToken(TokenInfo tokenInfo) {
		log.info("issuAccessToken");
		String refreshToken = tokenInfo.getRefreshToken();
		String accessToken = tokenInfo.getAccessToken();
		if(jwtTokenUtil.validateToken(refreshToken) == JWTCode.ACCESS) {
			Authentication authentication = jwtTokenUtil.getAuthentication(accessToken);
			String username = authentication.getName();
			AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);
			UserDetails principal = new User(username, "", accountContext.getAuthorities());
			String nAccessToken = jwtTokenUtil.generateAccessToken( new UsernamePasswordAuthenticationToken(principal, "", accountContext.getAuthorities()));
			tokenInfo.setAccessToken(nAccessToken);
		}
		
		return tokenInfo;
	}
}
