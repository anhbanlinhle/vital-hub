package com.main.server.service.implement;

import com.main.server.entity.User;
import com.main.server.repository.UserRepository;
import com.main.server.security.JwtService;
import com.main.server.security.TokenResponse;
import com.main.server.service.AuthService;
import com.main.server.utils.dto.OauthResponse;
import com.main.server.utils.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    final JwtService jwtService;

    @Autowired
    final UserRepository userRepository;

    @Override
    public ResponseEntity<?> signInResponse(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
        OauthResponse object;
        try {
            object = restTemplate.getForObject(uri, OauthResponse.class);
            if (object == null) {
                throw new HttpClientErrorException(HttpStatusCode.valueOf(400));
            }
        } catch (HttpClientErrorException exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().body(new Response("Invalid token", 400));
        }
        User user = userRepository.findByGmail(object.getEmail()).orElse(null);
        return ResponseEntity.ok().body(new TokenResponse(jwtService.generateToken(object.getEmail()), user == null));
    }
}
