package com.project.shopapp.service;

import com.project.shopapp.component.JwtProvider;
import com.project.shopapp.dto.UserDto;
import com.project.shopapp.dto.UserLoginDto;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.model.Role;
import com.project.shopapp.model.RoleType;
import com.project.shopapp.model.User;
import com.project.shopapp.model.UserRole;
import com.project.shopapp.repository.RoleRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationProvider authenticationProvider;
    private final RoleRepository roleRepository;
    @Override
    @Transactional
    public User createUser(UserDto userDto) throws DataIntegrityViolationException{
        String phoneNumber = userDto.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exited");
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new DataIntegrityViolationException("Email already exited");
        }
        User user = mapper.map(userDto, User.class);
        if (userDto.getFacebookAccountId() == 0 && userDto.getGoogleAccountId() == 0){
            String password = userDto.getPassword();
            String passwordEncode = passwordEncoder.encode(password);
            user.setPassword(passwordEncode);
        }
        Role role = roleRepository.findByName(RoleType.CUSTOMER).orElseThrow(
                     ()-> new DataNotFoundException("Create not successfully")
        );
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public String login(UserLoginDto userLoginDto) throws Exception {
        User existingUser = userRepository.findByEmail(userLoginDto.getEmail()).orElseThrow(
                ()-> new UsernameNotFoundException("User or password incorrect")
        );
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0){
            if (!passwordEncoder.matches(userLoginDto.getPassword(), existingUser.getPassword())){
                throw new BadCredentialsException("User or password incorrect");
            }
        }
        if (!existingUser.getRole().getName().equals(RoleType.CUSTOMER) ){
            throw new BadCredentialsException("User or password incorrect");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userLoginDto.getEmail(), userLoginDto.getPassword(),
                existingUser.getAuthorities()
        );
        authenticationProvider.authenticate(authenticationToken);
        return jwtProvider.generateAccessToken(existingUser);
    }
}
