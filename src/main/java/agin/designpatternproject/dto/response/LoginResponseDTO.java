package agin.designpatternproject.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private Long expiresIn;
    private String role;

    private LoginResponseDTO(Builder builder) {
        this.token = builder.token;
        this.expiresIn = builder.expiresIn;
        this.role = builder.role;
    }

    public static class Builder {
        private String token;
        private Long expiresIn;
        private String role;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public LoginResponseDTO build() {
            return new LoginResponseDTO(this);
        }
    }
}
