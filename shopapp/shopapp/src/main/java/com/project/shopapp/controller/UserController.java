package com.project.shopapp.controller;

import com.project.shopapp.dto.UserDto;
import com.project.shopapp.dto.UserLoginDto;
import com.project.shopapp.model.User;
import com.project.shopapp.response.BaseResponse;
import com.project.shopapp.response.LoginResponse;
import com.project.shopapp.service.IUserService;
import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.utils.BindingResultUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult){
        ResponseEntity<BaseResponse> errorMsg = BindingResultUtils.getResponseEntity(localizationUtils, bindingResult);
        if (errorMsg != null) return errorMsg;
        if (!userDto.getPassword().equals(userDto.getRetypePassword())){
            return ResponseEntity.badRequest().body("password not match");
        }
        try {
            User user = userService.createUser(userDto);
            return ResponseEntity.ok().body(user);

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody UserLoginDto userLoginDto, BindingResult bindingResult){

        ResponseEntity<BaseResponse> errorMsg = BindingResultUtils.getResponseEntity(localizationUtils, bindingResult);
        if (errorMsg != null) return errorMsg;
        try {
            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                    .data(userService.login(userLoginDto))
                    .build());

        }catch (Exception e){
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED,e.getMessage()))
                    .build());

        }
    }

}
