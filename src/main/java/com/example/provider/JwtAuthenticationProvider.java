package com.example.provider;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.dto.AccountContext;
import com.example.token.JwtAuthenticationToken;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		log.info("로그인 시도");
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);
		
		log.debug(accountContext.toString());
		
		//String jwtAccessToken = JwtTokenUtil.generateAccessToken(username);
		//String jwtRefreshToken = JwtTokenUtil.generateRefreshToken(username);
		
		
		
		return new JwtAuthenticationToken(accountContext.getAccount() , null , accountContext.getAuthorities());
	}

	

	
	
	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(JwtAuthenticationToken.class);
	}
	

}
