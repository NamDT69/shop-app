package com.project.shopapp.service;

import com.project.shopapp.dto.UserDto;
import com.project.shopapp.dto.UserLoginDto;
import com.project.shopapp.model.User;

public interface IUserService {
    User createUser(UserDto userDto);
    String login(UserLoginDto userLoginDto) throws Exception;
}
