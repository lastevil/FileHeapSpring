package com.mytest.MyApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenUtil {

    @Value("${jwt.lifetime}")
    private Integer jwtLifetime;
    @Value("${jwt.secret}")
    private String secret;

    SecretKey secretKey;

    public String generateToken(UserDetails userDetails) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        secretKey =  new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith( secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClamFromToken(token, Claims::getSubject);
    }

    public List<String> getRoles(String token) {
        return getClamFromToken(token, (Function<Claims, List<String>>) claims -> claims.get("roles", List.class));
    }

    private <T> T getClamFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
