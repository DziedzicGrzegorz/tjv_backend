package cz.cvut.tjv_backend.authConfig;

public class SecurityConstants {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/healthz",
            "/auth/**",
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
}