package com.houyaozu.knowledge.common.utils;


import com.houyaozu.knowledge.common.exception.KnowledgeException;
import com.houyaozu.knowledge.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
public class JwtUtil {
    private static long tokenExpiration = 60 * 60 * 1000L;
    private static SecretKey secretKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    public static String createToken(Integer userId, String username) {
        String jwt = Jwts.builder().
                setSubject("LOGIN_USER").
                setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)).
                claim("userId", userId).
                claim("username", username).
                signWith(secretKey, SignatureAlgorithm.HS256).
                compact();
        return jwt;
    }
    public static Claims parseToken(String token) {
        if (token == null) {
            throw new KnowledgeException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (ExpiredJwtException e){
            throw new KnowledgeException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException e){
            throw new KnowledgeException(ResultCodeEnum.TOKEN_INVALID);
        }
    }
}
