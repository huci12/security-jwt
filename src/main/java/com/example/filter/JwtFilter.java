package com.example.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.code.JWTCode;
import com.example.dto.Account;
import com.example.dto.AccountContext;
import com.example.token.JwtAuthenticationToken;
import com.example.util.JwtTokenUtil;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	
	// 인증에서 제외할 url
	private static final List<String> EXCLUDE_URL =Collections.unmodifiableList(Arrays.asList("/login" , "/token/issuAccessToken"));
		
	
    private JwtTokenUtil jwtTokenUtil;
	
    @Autowired
	private UserDetailsService userDetailsService;
	
    
	
	public JwtFilter(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}
	
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		log.info("do Filter");
		
		String token = request.getHeader("Authorization");
		log.info(token);
		if (token != null && token.startsWith("Bearer ")) {
			String jwtToken = extractAccessToken(request);
			log.info(jwtToken);
			
			if(jwtTokenUtil.validateToken(jwtToken) == JWTCode.ACCESS) {
				Authentication authentication = jwtTokenUtil.getAuthentication(jwtToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}else if(jwtTokenUtil.validateToken(jwtToken) == JWTCode.EXPIRED) {
				
				
				//String username = "testUser";
				
				//AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);
				//UserDetails principal = new User(username, "", accountContext.getAuthorities());
				
				//String nAccessToken = jwtTokenUtil.generateAccessToken( new UsernamePasswordAuthenticationToken(principal, "", accountContext.getAuthorities()));
				
				
				//Access Token이 만료되었다. Refresh 토큰을 통한 새로운 Access Token 발급이 필요함
				//클라이언트에 너의 토큰은 만료 되었으니 가지고 있는 Refresh 토큰을 보내라고 응답을 준다.
				
				
				//이후 다시 토큰을 보내면 DB나 레디스에 있는것과 비교 하여 
				//Access 토큰을 재 발급 해준다.
				
				
				
				
				
			}else{
				throw new IllegalArgumentException("Invalid JWT token");
			}
			filterChain.doFilter(request, response);
			
			
			
		}
	}
	
	private String extractAccessToken(HttpServletRequest request) {
	    String bearerToken = request.getHeader("Authorization");

	    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	        String token = bearerToken.substring(7);
	        token = token.replaceAll("\\s", ""); // 공백 제거
	    	return  token;// "Bearer " 다음 문자열부터 AccessToken이므로 추출합니다.
	    }

	    return null;
	}
	

}
