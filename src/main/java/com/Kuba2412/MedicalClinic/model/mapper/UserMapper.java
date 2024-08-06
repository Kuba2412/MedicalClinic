package com.Kuba2412.MedicalClinic.model.mapper;

import com.Kuba2412.MedicalClinic.model.User;
import com.Kuba2412.MedicalClinic.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {


    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}
