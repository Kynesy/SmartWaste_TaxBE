package it.unisalento.pas.taxbe.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JwtTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final String CLAIM_ROLE = "role"; // The key for the role claim in the JWT

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractRole(jwt.getClaims());

        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractRole(Map<String, Object> claims) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (claims.containsKey(CLAIM_ROLE)) {
            String role = (String) claims.get(CLAIM_ROLE);
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
