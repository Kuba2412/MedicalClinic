package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.handler.exception.UserNotFoundException;
import com.Kuba2412.MedicalClinic.model.User;
import com.Kuba2412.MedicalClinic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void addUser_UniqueData_UserAdded() {
        // given
        String username = "uniqueUser";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.addUser(username, password);

        // then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void getUserId_UserExists_UserReturned() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userService.getUserId(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void getUserId_UserDoesNotExist_ExceptionThrown() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when and then
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getUserId(userId));
        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void getAllUsers_PaginatedList_Returned() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Arrays.asList(new User(), new User());
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // when
        List<User> result = userService.getAllUsers(pageable);

        // then
        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }

    @Test
    void updatePassword_UserExists_PasswordUpdated() {
        // given
        String username = "existingUser";
        String newPassword = "newPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword("oldPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.updatePassword(username, newPassword);

        // then
        assertNotNull(result);
        assertEquals(newPassword, result.getPassword());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void updatePassword_UserNotFound_ThrowsException() {
        // given
        String username = "nonExistingUser";
        String newPassword = "newPassword";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updatePassword(username, newPassword);
        });

        // then
        assertEquals("User not found.", exception.getMessage());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verify(userRepository, Mockito.times(0)).save(any(User.class));
    }
}