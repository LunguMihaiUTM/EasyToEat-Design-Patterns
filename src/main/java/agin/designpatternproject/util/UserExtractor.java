package agin.designpatternproject.util;

import agin.designpatternproject.entity.User;
import agin.designpatternproject.repository.UserRepository;
import agin.designpatternproject.service.impl.JwtServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserExtractor {

    private JwtServiceImpl jwtService;
    private UserRepository userRepository;

    public User getUser(String jwtToken) {
        String username = jwtService.extractUsername(jwtToken);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("No user found"));
    }
}
