package com.learning.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller")
//open api annotation to enable bearer auth token
//@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Operation(
            summary = "Fetch user resource",
            description = "Returns a simple test response to validate user read permissions."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful fetch",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    //@PreAuthorize("hasAuthority('user:read')")
    @GetMapping
    public String get() {
        return "GET:: User Controller";
    }


    @Operation(
            summary = "Create user resource",
            description = "Used to test and validate that the caller has user create privileges."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Resource created",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    //@PreAuthorize("hasAuthority('user:create')")
    @PostMapping
    public String post() {
        return "POST:: User Controller";
    }


    @Operation(
            summary = "Update user resource",
            description = "Verifies user update capability for the current authenticated token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Update succeeded",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    //@PreAuthorize("hasAuthority('user:update')")
    @PutMapping
    public String put() {
        return "PUT:: User Controller";
    }


    @Operation(
            summary = "Delete user resource",
            description = "Checks deletion privileges of the authenticated useristrator."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted"
            ),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    //@PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping
    public String delete() {
        return "DELETE:: User Controller";

    }
}