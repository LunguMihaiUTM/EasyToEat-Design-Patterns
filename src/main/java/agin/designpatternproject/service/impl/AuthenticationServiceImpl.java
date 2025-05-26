package agin.designpatternproject.service.impl;

import agin.designpatternproject.dto.UserDTO;
import agin.designpatternproject.entity.User;
import agin.designpatternproject.enums.Role;
import agin.designpatternproject.exception.UserException;
import agin.designpatternproject.repository.UserRepository;
import agin.designpatternproject.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String signup(UserDTO input) {
        var user = userRepository.findByUsername(input.getUsername());
        if (user.isPresent()) {
            throw new UserException("Username is already in use");
        }

        User newUser = User.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .email(input.getEmail())
                .phone(input.getPhone())
                .role(input.getRole() != null ? input.getRole() : Role.USER)
                .build();

        userRepository.save(newUser);

        return "Successfully registered";
    }

    public User authenticate(UserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}