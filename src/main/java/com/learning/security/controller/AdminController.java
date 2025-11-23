package com.learning.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin Controller")
//open api annotation to enable bearer auth token
//@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Operation(
            summary = "Fetch admin resource",
            description = "Returns a simple test response to validate admin read permissions."
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
    //@PreAuthorize("hasAuthority('admin:read')")
    @GetMapping
    public String get() {
        return "GET:: admin controller";
    }


    @Operation(
            summary = "Create admin resource",
            description = "Used to test and validate that the caller has admin create privileges."
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
    //@PreAuthorize("hasAuthority('admin:create')")
    @PostMapping
    public String post() {
        return "POST:: admin controller";
    }


    @Operation(
            summary = "Update admin resource",
            description = "Verifies admin update capability for the current authenticated token."
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
    //@PreAuthorize("hasAuthority('admin:update')")
    @PutMapping
    public String put() {
        return "PUT:: admin controller";
    }


    @Operation(
            summary = "Delete admin resource",
            description = "Checks deletion privileges of the authenticated administrator."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted"
            ),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    //@PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping
    public String delete() {
        return "DELETE:: admin controller";

    }
}
