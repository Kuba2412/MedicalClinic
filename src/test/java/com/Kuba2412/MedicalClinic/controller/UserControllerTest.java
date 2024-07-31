//package com.Kuba2412.MedicalClinic.controller;
//
//import com.Kuba2412.MedicalClinic.handler.exception.UserNotFoundException;
//import com.Kuba2412.MedicalClinic.model.User;
//import com.Kuba2412.MedicalClinic.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private UserService userService;
//
//    private User user;
//
//    @BeforeEach
//    void setup() {
//        user = new User();
//        user.setUsername("kp123");
//        user.setPassword("password");
//    }
//
//    @Test
//    void addUser_ValidInput_UserCreated() throws Exception {
//        when(userService.addUser(anyString(), anyString())).thenReturn(user);
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username").value(user.getUsername()));
//    }
//
//    @Test
//    void getUserId_UserExists_UserReturned() throws Exception {
//        when(userService.getUserId(anyLong())).thenReturn(user);
//
//        mockMvc.perform(get("/users/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value(user.getUsername()));
//    }
//
//    @Test
//    void getUserId_UserNotFound_ThrowException() throws Exception {
//        when(userService.getUserId(anyLong())).thenThrow(new UserNotFoundException("User not found."));
//
//        mockMvc.perform(get("/users/12345")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("User not found."));
//    }
//
//    @Test
//    void getAllUsers_UsersExist_UsersReturned() throws Exception {
//        List<User> users = Arrays.asList(user);
//        when(userService.getAllUsers(any(Pageable.class))).thenReturn(users);
//
//        mockMvc.perform(get("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(users.size()));
//    }
//
//    @Test
//    void updatePassword_UserExists_PasswordUpdated() throws Exception {
//        when(userService.updatePassword(anyString(), anyString())).thenReturn(user);
//
//        mockMvc.perform(put("/users/testUser/password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("newPassword")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value(user.getUsername()));
//    }
//
//    @Test
//    void updatePassword_UserNotFound_ThrowException() throws Exception {
//        when(userService.updatePassword(anyString(), anyString())).thenThrow(new UserNotFoundException("User not found."));
//
//        mockMvc.perform(put("/users/nonExistentUser/password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("newPassword")))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("User not found."));
//    }
//}