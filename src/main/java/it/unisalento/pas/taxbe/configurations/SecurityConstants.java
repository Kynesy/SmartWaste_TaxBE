package it.unisalento.pas.taxbe.configurations;

/**
 * Class containing security-related constants.
 */
public class SecurityConstants {
    /**
     * Array of OAuth2 issuer URLs used for JWT (JSON Web Token) validation.
     */
    public static final String[] ISSUER_LIST = {"https://smart-city-waste-management.eu.auth0.com/"};

    /**
     * Role identifier for operator users.
     */
    public static final String OPERATOR_ROLE_ID = "OPERATOR";

    /**
     * Role identifier for regular user accounts.
     */
    public static final String USER_ROLE_ID = "USER";

    /**
     * Role identifier for administrative users with full access.
     */
    public static final String ADMIN_ROLE_ID = "ADMIN";
}
