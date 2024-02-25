package com.example.demo.status;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");


    String value;
    UserRole(String value){
        this.value = value;
    }
}
