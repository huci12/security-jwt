package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import com.example.filter.JwtFilter;
import com.example.filter.JwtLoginProcessingFilter;
import com.example.handler.JwtAccessDeniedHandler;
import com.example.handler.JwtAuthenticationFailureHandler;
import com.example.handler.JwtAuthenticationSuccessHandler;
import com.example.handler.JwtLoginAuthenticationEntryPoint;
import com.example.provider.JwtAuthenticationProvider;
import com.example.util.JwtTokenUtil;



@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	/*
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/public" , "/token/issuAccessToken");
	}
	*/
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/public" , "/token/issuAccessToken");
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.formLogin().disable()
		.httpBasic().disable();
		
		http
		.antMatcher("**")
		.authorizeRequests()
		.anyRequest()
		.authenticated()
		.and()
		.addFilterBefore(new JwtFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(jwtLoginProcessingFilter() ,UsernamePasswordAuthenticationFilter.class )
		;
		
		http.exceptionHandling().authenticationEntryPoint(new JwtLoginAuthenticationEntryPoint())
		.accessDeniedHandler(jwtAccessDeniedHandler());
		
		http.csrf().disable();
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(jwtAuthenticationProvider());
	}
	
	@Bean
	AuthenticationProvider jwtAuthenticationProvider(){
		return new JwtAuthenticationProvider();
	}
	
	@Bean
	AuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
		return new JwtAuthenticationSuccessHandler();
	}
	
	@Bean
	AuthenticationFailureHandler jwtAuthenticationFailureHandler() {
		return new JwtAuthenticationFailureHandler();
	}
	
	
	@Bean
	JwtLoginProcessingFilter jwtLoginProcessingFilter() throws Exception {
	
		JwtLoginProcessingFilter jwtLoginProcessingFilter = new JwtLoginProcessingFilter();
		
		jwtLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
		jwtLoginProcessingFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler());
		jwtLoginProcessingFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler());
		return jwtLoginProcessingFilter;
		
	}
	
	@Bean 
	JwtAccessDeniedHandler jwtAccessDeniedHandler() {
		return new JwtAccessDeniedHandler();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	

}
