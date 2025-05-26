package agin.designpatternproject.dto;

import agin.designpatternproject.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private Role role;
}
