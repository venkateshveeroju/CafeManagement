package com.restaurant.tablebookingapp.JWT;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    private final JwtDecoder jwtDecoder;

    private final JwtToPrincipleConvertor jwtToPrincipleConvertor;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().matches("/auth/login|/user/forgotPassword|/user/test")) {
            filterChain.doFilter(request, response);
        } else {

            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            extractTokenFromRequest(request)
                    .map(jwtDecoder::decode)
                    .map(jwtToPrincipleConvertor::convert)
                    .map(UserPrincipleAuthenticationToken::new)
                    .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
            filterChain.doFilter(request, response);
        }
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }


}