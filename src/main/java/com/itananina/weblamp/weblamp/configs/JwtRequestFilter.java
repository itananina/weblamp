package com.itananina.weblamp.weblamp.configs;

import com.itananina.weblamp.weblamp.services.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import io.jsonwebtoken.ExpiredJwtException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization"); //всегда должен быть иначе чет кривое шлют

        String username = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwt); //валидируем и достаем из токена
            } catch (ExpiredJwtException e) {
                log.debug("The token is expired");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //вручную заполняем секьюрити контекст
            //достаем инфу из токена, другой вариант: каждый раз перепроверять есть ли этот юзер все еше в бд
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, jwtTokenUtil.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
