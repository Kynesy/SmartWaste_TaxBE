/**
 * A converter class responsible for converting a JWT (JSON Web Token) into an AbstractAuthenticationToken
 * used for authentication and authorization purposes.
 */
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

/**
 * Converts a JWT into an AbstractAuthenticationToken by extracting the user's roles from the JWT claims.
 */
public class JwtTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final String CLAIM_ROLE = "role"; // The key for the role claim in the JWT

    /**
     * Converts a JWT into an AbstractAuthenticationToken.
     *
     * @param jwt The JWT to be converted.
     * @return An AbstractAuthenticationToken containing the user's authorities.
     */
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractRole(jwt.getClaims());

        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * Extracts the user's role from the JWT claims.
     *
     * @param claims The claims extracted from the JWT.
     * @return A collection of GrantedAuthority representing the user's role.
     */
    private Collection<GrantedAuthority> extractRole(Map<String, Object> claims) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (claims.containsKey(CLAIM_ROLE)) {
            String role = (String) claims.get(CLAIM_ROLE);
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
