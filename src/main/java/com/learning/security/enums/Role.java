package com.learning.security.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ADMIN:
 *   permissions:
 *     admin:*
 *     manager:*
 *   role-authority:
 *     ROLE_ADMIN
 *   final exported authority-list (immutable):
 *     admin:*
 *     manager:*
 *     ROLE_ADMIN
 *
 *
 *     So ADMIN ultimately becomes:
 *           admin:create -> Authority
 *           admin:delete
 *           admin:update
 *           admin:read
 *           manager:create
 *           manager:delete
 *           manager:update
 *           manager:read
 *           ROLE_ADMIN -> ROLE
 */

public enum Role {
    USER(
            Set.of(
                    Permission.USER_READ,
                    Permission.USER_UPDATE,
                    Permission.USER_DELETE,
                    Permission.USER_CREATE
            )
    ),

    ADMIN(
            Set.of(
                    Permission.ADMIN_READ,
                    Permission.ADMIN_UPDATE,
                    Permission.ADMIN_DELETE,
                    Permission.ADMIN_CREATE,
                    Permission.MANAGER_READ,
                    Permission.MANAGER_UPDATE,
                    Permission.MANAGER_DELETE,
                    Permission.MANAGER_CREATE
            )
    ),

    MANAGER(
            Set.of(
                    Permission.MANAGER_READ,
                    Permission.MANAGER_UPDATE,
                    Permission.MANAGER_DELETE,
                    Permission.MANAGER_CREATE
            )
    );

    private final List<SimpleGrantedAuthority> authorities;

    Role(Set<Permission> permissions) {
        // maps permissions to -> authorities of type SimpleGrantedAuthority
        //AUTHORITIES
        List<SimpleGrantedAuthority> tempList = permissions
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getPermission()))
                .collect(Collectors.toList());

        //adds role to authorities with ROLE_
        //ROLES_
        tempList.add(new SimpleGrantedAuthority("ROLE_"+ this.name()));

        this.authorities = Collections.unmodifiableList(tempList);

    }

    public List<SimpleGrantedAuthority> getAuthorities(){
        return this.authorities;
    }

}
