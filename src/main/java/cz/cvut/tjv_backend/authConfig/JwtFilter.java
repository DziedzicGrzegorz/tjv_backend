package cz.cvut.tjv_backend.authConfig;

import cz.cvut.tjv_backend.exception.Exceptions.AccessTokenExpiredException;
import cz.cvut.tjv_backend.exception.Exceptions.InvalidAccessTokenException;
import cz.cvut.tjv_backend.exception.Exceptions.MissingAccessToken;
import cz.cvut.tjv_backend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if (jwt == null) {
                throw new MissingAccessToken("Missing or invalid Authorization header");
            }

            // Validate token and extract claims
            Claims claims = jwtService.validateAndExtractClaims(jwt, TOKEN_TYPE.ACCESS);

            String username = claims.getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            handlerExceptionResolver.resolveException(
                    request, response, null, new AccessTokenExpiredException("Access token expired")
            );
            return;

        } catch (MissingAccessToken e) {
            handlerExceptionResolver.resolveException(
                    request, response, null, new MissingAccessToken("Missing Authorization header")
            );
            return;

        } catch (JwtException e) {
            handlerExceptionResolver.resolveException(
                    request, response, null, new InvalidAccessTokenException("Invalid token")
            );
            return;
        }

        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        for (String pattern : SecurityConstants.PUBLIC_ENDPOINTS) {
            if (pathMatcher.match(pattern, path)) {
                return true; // Skip filtering for public endpoints
            }
        }
        return false; // Apply filter for other endpoints
    }
}