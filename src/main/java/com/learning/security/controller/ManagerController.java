package com.learning.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/manager")
@Tag(name = "Manager Controller")
//open api annotation to enable bearer auth token
//@SecurityRequirement(name = "bearerAuth")
public class ManagerController {

    @Operation(
            summary = "Fetch manager resource",
            description = "Returns a simple test response to validate manager read permissions."
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
    //@PreAuthorize("hasAuthority('manager:read')")
    @GetMapping
    public String get() {
        return "GET:: Manager Controller";
    }


    @Operation(
            summary = "Create manager resource",
            description = "Used to test and validate that the caller has manager create privileges."
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
    //@PreAuthorize("hasAuthority('manager:create')")
    @PostMapping
    public String post() {
        return "POST:: Manager Controller";
    }


    @Operation(
            summary = "Update manager resource",
            description = "Verifies manager update capability for the current authenticated token."
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
    //@PreAuthorize("hasAuthority('manager:update')")
    @PutMapping
    public String put() {
        return "PUT:: Manager Controller";
    }


    @Operation(
            summary = "Delete manager resource",
            description = "Checks deletion privileges of the authenticated manageristrator."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted"
            ),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    //@PreAuthorize("hasAuthority('manager:delete')")
    @DeleteMapping
    public String delete() {
        return "DELETE:: Manager Controller";

    }
}