package ru.danilov.Spring_security_JWT.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.danilov.Spring_security_JWT.security.JwtUtil;
import ru.danilov.Spring_security_JWT.service.PersonDetailsService;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Логика аутентификации с использованием токена
        String token = (String) authentication.getCredentials();

        try {
            String username = jwtUtil.validateToken(token);
            UserDetails userDetails = personDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        } catch (JWTVerificationException e) {
            throw new BadCredentialsException("Invalid JWT Token");
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("User not found");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
