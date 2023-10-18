package com.main.server.security;

import com.main.server.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Autowired
    UserRepository userRepository;
    public static final String USER_SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public String generateToken(String username) {
        HashMap<String, Object> map = new HashMap<>();
        return newToken(map, username);
    }

    public boolean tokenIsValid(String token, UserDetails userDetails) {
        return (extractUsername(token).equals(userDetails.getUsername()) &&!tokenIsExpired(token));
    }

    public boolean tokenIsValid(String token) {
        return !tokenIsExpired(token);
    }


    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(USER_SECRET);
        return Keys.hmacShaKeyFor(bytes);
    }

    private String newToken(Map<String, ? extends Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    private Claims allClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    private<T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = allClaims(token);
        return claimResolver.apply(claims);
    }

    private boolean tokenIsExpired(String token) {
        Date expiredDate = extractExpiration(token);
        Date now = new Date();
        return expiredDate.before(now);
    }
}
