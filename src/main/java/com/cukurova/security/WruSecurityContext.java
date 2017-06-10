package com.cukurova.security;

import com.cukurova.model.UserModel;
import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Abed
 */
public class WruSecurityContext implements SecurityContext {

    private UserModel user;
    private String scheme;

    public WruSecurityContext(UserModel user, String scheme) {
        this.user = user;
        this.scheme = scheme;
    }

    @Override
    public Principal getUserPrincipal() {
        return this.user;
    }

    public UserModel getRequestUser() {
        return this.user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return user.getUsername() != null;
    }

    @Override
    public boolean isSecure() {
        return "https".equals(this.scheme);
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }

}
