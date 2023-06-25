package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.dto.Account;
import com.example.dto.AccountContext;
import com.example.dto.Role;

import lombok.extern.slf4j.Slf4j;



@Service("userDetailsService")
@Slf4j
public class CustomUserDetails implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		Account account = new Account();
		
		account.setId(Long.parseLong("1"));
		account.setUsername("testUser");
		account.setEmail("testEmail@Email.com");
		account.setAge(20);
		account.setPassword("1234");
		
		
		Role role = Role.builder().id(Long.parseLong("1")).roleName("ROLE_USER").roleDesc("USER").build();
		account.userRoles.add(role);
		
		
		
		Set<String> userRoles = account.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRoleName())
                .collect(Collectors.toSet());
		
		log.info(userRoles.toString());
		
		List<GrantedAuthority> collect = userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		
		
		return new AccountContext(account , collect);
	}

}
