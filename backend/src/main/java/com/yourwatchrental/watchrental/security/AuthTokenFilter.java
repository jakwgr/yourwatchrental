package com.yourwatchrental.watchrental.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor

public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwUtil util;
    private final  CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try{
            String jwt = parseJwt(request);
            if (jwt != null && util.validateJwtToken(jwt))
            {
                final String id = util.getUserFromToken(jwt);
                final UserDetails userDetail = userDetailsService.loadUserByUsername(id);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetail,
                        null,
                        userDetail.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);
            }
        } catch (Exception e)
        {
            log.error("cannot set user auth", e);
        }
        filterChain.doFilter(request,response);


    }
    private String parseJwt(HttpServletRequest request)
    {
        String headerAuth = request.getHeader("Authorization");
        if(headerAuth != null && headerAuth.startsWith("Bearer "))
        {
            return headerAuth.substring(7);
        }
        return null;
    }
}
