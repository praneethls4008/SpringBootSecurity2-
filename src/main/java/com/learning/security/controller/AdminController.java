package com.learning.security.controller;

import com.learning.security.enums.Permission;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    //@PreAuthorize("hasAuthority('admin:read')")
    @GetMapping
    public String get(){
        return "GET:: admin controller";
    }

    //@PreAuthorize("hasAuthority('admin:create')")
    @PostMapping
    public String post(){
        return "POST:: admin controller";
    }

    //@PreAuthorize("hasAuthority('admin:update')")
    @PutMapping
    public String put(){
        return "PUT:: admin controller";
    }

    //@PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping
    public String delete(){
        return "DELETE:: admin controller";

    }
}
