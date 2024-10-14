package com.rajan.foodDeliveryApp.controllers;

import com.rajan.foodDeliveryApp.domain.dto.GenericResponse;
import com.rajan.foodDeliveryApp.domain.dto.PasswordResetDto;
import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import com.rajan.foodDeliveryApp.services.UserService;
import com.rajan.foodDeliveryApp.services.mail.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/password-reset")
public class PasswordResetController {

    private final UserService userService;
    private final EmailService emailService;

    public PasswordResetController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/request")
    public GenericResponse resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
        UserEntity user = userService.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String resetUrl = getAppUrl(request) + "/api/password-reset/changePassword?token=" + token;

        emailService.sendResetTokenEmail(user.getEmail(), resetUrl);

        return new GenericResponse("Reset password email sent to " + userEmail);
    }

    @PostMapping("/changePassword")
    public GenericResponse changePassword(@RequestBody PasswordResetDto passwordResetDTO) {
        boolean isValid = userService.validatePasswordResetToken(passwordResetDTO.getToken());

        if (!isValid) {
            throw new RuntimeException("Invalid token.");
        }

        // fetch user from the token
        UserEntity user = userService.findByPasswordResetToken(passwordResetDTO.getToken())
                .orElseThrow(() -> new RuntimeException("User not found"));
        userService.updatePassword(user, passwordResetDTO.getNewPassword());
        return new GenericResponse("Password successfully reset.");
    }

    private String getAppUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
