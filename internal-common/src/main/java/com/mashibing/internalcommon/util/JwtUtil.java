package com.mashibing.internalcommon.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    /**
     * 密钥，仅服务器端存储
     */
    private static String secret = "ko346134h_we]rg3in_yip1!";

    /**
     * @param subject
     * @param issueDate 签发时间
     * @return
     */
    public static String createToken(String subject, Date issueDate){
        String compactJws = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issueDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return compactJws;
    }

    /**
     * 解密 jwt
     * @param token
     * @return
     */
    public static JwtInfo parseToken(String token){
        try{
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            if (claims != null){
                JwtInfo jwtInfo = new JwtInfo();
                jwtInfo.setSubject(claims.getSubject());
                jwtInfo.setIssueDate(claims.getIssuedAt().getTime());
                return jwtInfo;
            }
        }catch (ExpiredJwtException e){
            e.printStackTrace();
            System.out.println("jwt过期了");
        }
        return null;
    }

}
