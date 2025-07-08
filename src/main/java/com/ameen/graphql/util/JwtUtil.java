package com.ameen.graphql.util;

import com.ameen.graphql.constant.Constant;
import com.ameen.graphql.dto.LoginDto;
import com.ameen.graphql.exception.CustomGraphQLException;
import com.ameen.graphql.model.Role;
import com.ameen.graphql.model.User;
import com.ameen.graphql.model.UserRoleMapping;
import com.ameen.graphql.repository.UserRoleMappingRepository;
import com.ameen.graphql.serviceImpl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private final UserServiceImpl userService;
    private final UserRoleMappingRepository userRoleMappingRepository;

    public JwtUtil(UserServiceImpl userService, UserRoleMappingRepository userRoleMappingRepository) {
        this.userService = userService;
        this.userRoleMappingRepository = userRoleMappingRepository;
    }
    private static final String SECRET_KEY = "coherent";
    private Key getSigningKey() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        } catch (Exception e) {
            throw new CustomGraphQLException("Error generating signing key");
        }
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new CustomGraphQLException("Token is invalid or expired");
        }
    }

    public String generateToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Map<String, Object> validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public Map<String, String> login(LoginDto loginDto) {
        User user = userService.login(loginDto);
        if (user == null) {
            throw new CustomGraphQLException("Invalid credentials");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("userName", user.getName());
        claims.put("email", user.getEmail());
        List<UserRoleMapping> userRoleMappings = userRoleMappingRepository.findByUser(user.getId());
        Role role = null;
        if (!userRoleMappings.isEmpty()) {
            role = userRoleMappings.get(0).getRole();
            if (role != null) {
                claims.put("role", role.getRoleName());
            }
        }
        String accessToken = generateToken(claims,user.getEmail() , Constant.ACCESS_TOKEN_EXPIRATION);
        String refreshToken = generateToken(claims,user.getEmail() , Constant.REFRESH_TOKEN_EXPIRATION);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("role", role.getRoleName()) ;
        tokens.put(Constant.ACCESS_TOKEN, accessToken);
        tokens.put(Constant.REFRESH_TOKEN, refreshToken);
        return tokens;
    }

    public Map<String, String> refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CustomGraphQLException("Refresh token is required");
        }
        Map<String, Object> claims = validateToken(refreshToken);
        if (claims == null) {
            throw new CustomGraphQLException("Invalid or expired refresh token");
        }
        String newAccessToken = generateToken(claims, (String) claims.get("email"), Constant.ACCESS_TOKEN_EXPIRATION);
        String newRefreshToken = generateToken(claims, (String) claims.get("email"), Constant.REFRESH_TOKEN_EXPIRATION);
        return Map.of(Constant.ACCESS_TOKEN, newAccessToken,Constant.REFRESH_TOKEN,newRefreshToken);
    }

    public boolean validateTokenId(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new CustomGraphQLException("Error extracting claims from token");
        }
    }

}
