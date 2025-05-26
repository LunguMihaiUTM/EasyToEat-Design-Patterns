package agin.designpatternproject.controller;

import agin.designpatternproject.dto.UserDTO;
import agin.designpatternproject.dto.response.LoginResponseDTO;
import agin.designpatternproject.entity.User;
import agin.designpatternproject.service.AuthenticationService;
import agin.designpatternproject.service.impl.JwtServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final JwtServiceImpl jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO input) {
        return ResponseEntity.ok(authenticationService.signup(input));
    }


    //Builder
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserDTO input) {
        User authenticatedUser = authenticationService.authenticate(input);
        String token = jwtService.generateToken(authenticatedUser);
        LoginResponseDTO loginResponse = new LoginResponseDTO.Builder()
                .token(token)
                .role(authenticatedUser.getRole().toString())
                .expiresIn(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }
}
