package agin.designpatternproject.service;

import agin.designpatternproject.dto.UserDTO;
import agin.designpatternproject.entity.User;

public interface AuthenticationService {
    String signup(UserDTO input);
    User authenticate(UserDTO input);
}
