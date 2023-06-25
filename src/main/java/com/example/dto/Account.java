package com.example.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;





@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable{

    private Long id;
    private String username;
    private String email;
    private int age;
    private String password;
    public Set<Role> userRoles = new HashSet<>();
    
}
