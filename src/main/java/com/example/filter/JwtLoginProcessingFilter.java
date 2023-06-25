package com.example.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.example.dto.AccountDto;
import com.example.token.JwtAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JwtLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private ObjectMapper objectMapper = new ObjectMapper();
	

	public JwtLoginProcessingFilter() {
		super(new AntPathRequestMatcher("/login" , "POST"));
	}
	

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		
		logger.debug("JwtLoginProcessingFilter");
		
		
		if (!isAjax(request)) {
			throw new IllegalStateException("Authentication is not supported");
		}

		
		AccountDto accountDto = objectMapper.readValue(request.getReader(),AccountDto.class);
		  
		if(StringUtils.isEmpty(accountDto.getUsername()) || StringUtils.isEmpty(accountDto.getPassword())){ throw new
			IllegalArgumentException("Username or Password is empty"); 
		}
		  
		JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(accountDto.getUsername() , accountDto.getPassword());
		
		
		
		return getAuthenticationManager().authenticate(jwtAuthenticationToken);
		
		
		
	}

	private boolean isAjax(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			return true;
		}
		return false;
	}
	
	
	
	
	

}
