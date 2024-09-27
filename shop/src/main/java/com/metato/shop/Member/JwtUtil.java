package com.metato.shop.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;


@Component
public class JwtUtil {

    @Value("${myapp.security.jwt-secret}")
    private String jwtSecret;  // 인스턴스 필드로 jwtSecret 주입

    private static SecretKey key;

    // Spring이 인스턴스를 생성하고 의존성을 주입한 후 실행됨
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));  // static 필드에 값 할당
    }

    // JWT 만들어주는 함수
    public static String createToken(Authentication auth) {
        var user = (CustomUser) auth.getPrincipal();

        var authorities = auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));

        // .claim(이름, 값) 으로 JWT에 데이터 추가 가능
        String jwt = Jwts.builder()
                .claim("username", user.getUsername())
                .claim("displayName", user.displayName)
                .claim("authorities",  authorities)
                .claim("id", user.id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000000)) //유효기간 1000초
                .signWith(key)
                .compact();

        return jwt;
    }

    // JWT 까주는 함수
    public static Claims extractToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }

}
