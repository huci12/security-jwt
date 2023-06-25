package com.example.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.dto.Account;
import com.example.dto.TokenInfo;
import com.example.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		log.info("Login Success");
		Account account = (Account) authentication.getPrincipal();
		
		log.info(account.toString());
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		
		
		//로그인 성공시 Token 발행
		TokenInfo token = jwtTokenUtil.generateToken(authentication);
		log.info(token.getAccessToken());
		log.info(token.getRefreshToken());
		//objectMapper.writeValue(response.getWriter(), token.getAccessToken());
		//response.setHeader("Authorization", "Bearer " + token.getAccessToken());
		
		
		//Refresh 토큰은 DB나 Redis 에 저장 한다.
		
		objectMapper.writeValue(response.getWriter(), token.toString());
		response.setHeader("Authorization", "Bearer " + token.getAccessToken());
		response.setHeader("RefreshToken", token.getRefreshToken());
		
		
        
		
		
		
	}

}
