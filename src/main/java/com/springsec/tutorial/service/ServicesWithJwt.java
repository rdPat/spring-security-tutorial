package com.springsec.tutorial.service;
import com.springsec.tutorial.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServicesWithJwt {

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("mysupersecretkeymysupersecretkey".getBytes());

    // ------------------ TOKEN GENERATION --------------------
    public static String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", "USER");

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutes
                .signWith(SECRET_KEY)
                .compact();
    }


    // ------------------ TOKEN EXTRACTION --------------------
    public static String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    public static Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }


    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    // ------------------ VALIDATE TOKEN ----------------------
    public static boolean validateToken(String token, UserDetails userDetails) {

        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    // ------------------ YOUR VERIFY METHOD ------------------
    public String verifyByJWT(Users user) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    ));

            if (authentication.isAuthenticated()) {
                String token = generateToken(user.getUsername());
                return "SUCCESS AWWWWW " + token;
            }
        } catch (Exception e) {
            return "Fail EWWWWW";
        }
        return "";
    }
}
