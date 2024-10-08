package com.cetin.hospital.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${jwt.key}")
    private String SECRET;
    @Value("${expires.in}")
    private Long EXPIRES_IN;
    @Value("${refresh.expires.in}")
    private Long REFRESH_EXPIRES_IN;

    public String generateToken(String TC){
        Map<String, Object> claims = new HashMap<>();
        Date expireDate = new Date(System.currentTimeMillis() + (EXPIRES_IN * 1000L));
        return buildToken(claims, TC, expireDate);
    }

    public String generateRefreshToken(String TC){
        Map<String, Object> claims = new HashMap<>();
        Date expireDate = new Date(System.currentTimeMillis() + (REFRESH_EXPIRES_IN * 1000L));
        return buildToken(claims, TC, expireDate);
    }

    private String buildToken(Map<String, Object> claims, String TC,
                              Date expireDate){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(TC)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expireDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token, JwtUserDetails jwtUserDetails){
        try {
            Claims claims = getAllClaims(token);
            String TC = claims.getSubject();
            Date expiration = claims.getExpiration();
            return TC.equals(jwtUserDetails.getUsername()) && expiration.after(new Date());
        }catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
