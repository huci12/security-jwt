package com.example.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Role{
	private Long id;
	private String roleName;
	private String roleDesc;
}
