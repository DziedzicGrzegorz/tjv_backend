package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.authConfig.TOKEN_TYPE;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.access_expiration}")
    private long jwtAccessExpiration;

    @Value("${application.security.jwt.refresh_expiration}")
    private long jwtRefreshExpiration;
    private final String TOKEN_TYPE_NAME = "tokenType";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtAccessExpiration, TOKEN_TYPE.ACCESS);
    }

    public String generateAccessToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtAccessExpiration, TOKEN_TYPE.ACCESS);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtRefreshExpiration, TOKEN_TYPE.REFRESH);
    }

    public String generateRefreshToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtRefreshExpiration, TOKEN_TYPE.REFRESH);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration,
            TOKEN_TYPE tokenType
    ) {
        extraClaims.put("tokenType", tokenType.name());

        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();

    }

    public Claims validateAndExtractClaims(String token, TOKEN_TYPE expectedTokenType) throws JwtException {
        Claims claims = extractAllClaims(token);

        String tokenTypeString = claims.get(TOKEN_TYPE_NAME, String.class);
        TOKEN_TYPE tokenType;
        try {
            tokenType = TOKEN_TYPE.valueOf(tokenTypeString);
        } catch (IllegalArgumentException e) {
            throw new JwtException("Invalid token type", e);
        }

        if (tokenType != expectedTokenType) {
            throw new JwtException("Invalid token type");
        }

        if (isTokenExpired(claims)) {
            throw new ExpiredJwtException(null, claims, "Token has expired");
        }

        return claims;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}