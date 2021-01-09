package com.udacity.jwdnd.course4.ecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationVerificationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationVerificationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    private final JWTVerifier jwtVerifier = JWT.require(HMAC512(SecurityConstants.SECRET.getBytes()))
            .withClaimPresence(SecurityConstants.USERNAME_CLAIM).acceptExpiresAt(0).build();

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String token = req.getHeader(SecurityConstants.HEADER_STRING);
        if (token == null || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        token = token.substring(SecurityConstants.TOKEN_PREFIX.length());

        try {
            DecodedJWT jwt = jwtVerifier.verify(token);

            UsernamePasswordAuthenticationToken authenticationToken = null;
            String username = jwt.getClaim(SecurityConstants.USERNAME_CLAIM).asString();
            if (username != null) {
                authenticationToken = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            }

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        catch (JWTVerificationException jwtve) {
            // ignore invalid verification
        }

        chain.doFilter(req, res);
    }
}
