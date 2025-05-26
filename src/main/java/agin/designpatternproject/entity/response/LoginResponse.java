package agin.designpatternproject.entity.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;

    private Long expiresIn;
}
