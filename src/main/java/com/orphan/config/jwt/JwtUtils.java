package com.orphan.config.jwt;

import com.orphan.config.security.UserPrincipal;
import com.orphan.enums.TokenEnum;
import com.orphan.utils.constants.Constants;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    //Thời gian có hiệu lực của chuỗi jwt
    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Creates access token.
     *
     * @param authentication {@link Authentication}
     * @param rememberMe if application is remember me
     * @return {@link AccessToken}
     */
    public AccessToken createAccessToken(Authentication authentication, boolean rememberMe) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String name = String.valueOf(principal.getUserId());
        long now = (new Date()).getTime();
        long dateToMilliseconds = 60*60*1000;
        Date validity;
        Date refreshTokenExpiration = new Date(now + TokenEnum.REFRESH_TOKEN_EXPIRED.getValue() * dateToMilliseconds);
        if (rememberMe) {
            validity = new Date(now + TokenEnum.TOKEN_REMEMBER_ME_EXPIRED.getValue() * dateToMilliseconds);
        } else {
            validity = new Date(now + TokenEnum.TOKEN_JWT_EXPIRED.getValue() * dateToMilliseconds);
        }
        //Build access token
        String jwt = Jwts.builder().setSubject(name)
                .setIssuedAt(new Date())
                .setAudience(String.valueOf(refreshTokenExpiration.getTime()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        //Build refresh token
        String refreshToken = Jwts.builder().setSubject(name)
                .setExpiration(refreshTokenExpiration)
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
                .compact();

        AccessToken accessToken = new AccessToken();
        accessToken.setToken(jwt);
        accessToken.setExpried(validity);
        accessToken.setRefreshToken(refreshToken);
        accessToken.setUserId(principal.getUserId());
        accessToken.setTokenType(Constants.JWT_TOKEN_TYPE);
        return accessToken;
    }

    /**
     * Create token register
     *
     * @param email
     * @return token
     */

    public String createTokenRegister(String email) {
        //Build access token
        long now = (new Date()).getTime();
        long dateToMilliseconds = 86400000 ;
        Date validity = new Date(now + dateToMilliseconds);
        String jwt = Jwts.builder().setSubject(email)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret).compact();
        return jwt;
    }

    /**
     * Get subject from input token
     *
     * @param token access token
     * @return subject
     */
    public Integer getSubjectFromToken(String token) {
        return Integer.valueOf(Jwts.parser()
                .setSigningKey(this.jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
    /**
     * Get subject from input token
     *
     * @param token access token
     * @return subject
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(token).getBody().getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    //kiem tra jwttoken
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}
