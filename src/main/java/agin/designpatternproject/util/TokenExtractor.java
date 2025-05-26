package agin.designpatternproject.util;

import org.springframework.stereotype.Service;

@Service
public class TokenExtractor {

    public String getToken(String token){
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}