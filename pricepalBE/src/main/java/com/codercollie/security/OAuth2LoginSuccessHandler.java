package com.codercollie.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final String redirectUri;
    private final JwtService jwtService;

    public OAuth2LoginSuccessHandler(JwtService jwtService, @Value("${app.frontend.redirect-uri}") String redirectUri){
        this.jwtService = jwtService;
        this.redirectUri = redirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        final OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email");
        if(email == null){
            email = user.getName();
        }

        String name = user.getAttribute("name");
        if(name == null || name.isBlank()){
            name = user.getName();
        }

        final String token = jwtService.createToken(email, Map.of(
                "name", name,
                "provider", "google"
        ));
        response.sendRedirect(redirectUri + "#token=" + URLEncoder.encode(token, StandardCharsets.UTF_8));
    }
}
