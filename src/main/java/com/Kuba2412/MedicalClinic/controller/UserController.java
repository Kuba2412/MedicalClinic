package com.Kuba2412.MedicalClinic.controller;

import com.Kuba2412.MedicalClinic.model.User;
import com.Kuba2412.MedicalClinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new user", description = "Create a new user with a username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public User addUser(@RequestBody User user) {
        return userService.addUser(user.getUsername(), user.getPassword());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public User getUserId(@PathVariable Long id) {
        return userService.getUserId(id);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users returned successfully")
    })
    public List<User> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @PutMapping("/{username}/password")
    @Operation(summary = "Update user password", description = "Update the password of an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public User updatePassword(@PathVariable String username, @RequestBody String newPassword) {
        return userService.updatePassword(username, newPassword);
    }
}