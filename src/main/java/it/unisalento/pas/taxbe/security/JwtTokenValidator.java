/**
 * A custom JWT (JSON Web Token) validator responsible for validating JWT tokens used for authentication.
 * This validator checks token expiration and issuer validity.
 */
package it.unisalento.pas.taxbe.security;

import it.unisalento.pas.taxbe.configurations.SecurityConstants;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

/**
 * Validates JWT tokens for authentication and authorization.
 */
public class JwtTokenValidator implements OAuth2TokenValidator<Jwt> {

    /**
     * Validates the given JWT token.
     *
     * @param jwt The JWT token to be validated.
     * @return An OAuth2TokenValidatorResult indicating whether the token is valid or not.
     */
    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (isTokenExpired(jwt)) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Token expired", null));
        }

        if (!isValidIssuer(jwt)) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid issuer", null));
        }

        return OAuth2TokenValidatorResult.success();
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param jwt The JWT token to check for expiration.
     * @return true if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(Jwt jwt) {
        Instant tokenExpiration = jwt.getExpiresAt();
        return tokenExpiration != null && tokenExpiration.isBefore(Instant.now());
    }

    /**
     * Validates the issuer of the JWT token against the expected issuers.
     *
     * @param jwt The JWT token to validate.
     * @return true if the issuer is valid, false otherwise.
     */
    private boolean isValidIssuer(Jwt jwt) {
        String tokenIssuer = String.valueOf(jwt.getIssuer());
        String[] expectedIssuer = SecurityConstants.ISSUER_LIST;
        for (String issuer : expectedIssuer) {
            if (issuer.equals(tokenIssuer)) {
                return true; // Found a matching issuer in the list
            }
        }

        return false; // No matching issuer found
    }
}
