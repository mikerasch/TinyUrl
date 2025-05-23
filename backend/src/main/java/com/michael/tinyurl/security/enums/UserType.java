package com.michael.tinyurl.security.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserType {
    ANONYMOUS((GrantedAuthority) () -> "ANONYMOUS"),
    LOGGED_IN((GrantedAuthority) () -> "LOGGED_IN");

    private final GrantedAuthority grantedAuthority;

    UserType(GrantedAuthority grantedAuthority) {
        this.grantedAuthority = grantedAuthority;
    }

    public GrantedAuthority getGrantedAuthority() {
        return grantedAuthority;
    }
}
