package com.rajan.foodDeliveryApp.services.authentication;


import com.rajan.foodDeliveryApp.domain.Role;
import com.rajan.foodDeliveryApp.domain.dto.AuthenticationResponse;
import com.rajan.foodDeliveryApp.domain.dto.LoginRequest;
import com.rajan.foodDeliveryApp.domain.dto.RegisterRequest;
import com.rajan.foodDeliveryApp.domain.dto.UserDto;
import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import com.rajan.foodDeliveryApp.mappers.Mapper;
import com.rajan.foodDeliveryApp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        UserEntity u = UserEntity.builder()
                .email(registerRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role(Role.USER)
                .build();
        userRepository.save(u);
        String token = jwtService.generateToken(u);
        Date issuedAt = jwtService.getIssuedDate(token);
        Date expirationDate = jwtService.getExpirationDate(token);

        return AuthenticationResponse.builder()
                .token(token)
                .issuedDate(issuedAt)
                .expirationDate(expirationDate)
                .build();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        UserEntity u = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserDto userDto = userMapper.mapTo(u);
        String token = jwtService.generateToken(u);
        Date issuedAt = jwtService.getIssuedDate(token);
        Date expirationDate = jwtService.getExpirationDate(token);

        return AuthenticationResponse.builder()
                .user(userDto)
                .token(token)
                .issuedDate(issuedAt)
                .expirationDate(expirationDate)
                .build();
    }
}
