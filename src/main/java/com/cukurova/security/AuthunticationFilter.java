package com.cukurova.security;

import com.cukurova.model.UserModel;
import com.cukurova.tasks.UserTasks;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthunticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private Method getRequestedMethod() {
        return resourceInfo.getResourceMethod();//getting the requested method's information and annotations
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (getRequestedMethod().isAnnotationPresent(PermitAll.class)) {//checking the type of the permission annotation
            return;
        } else if (getRequestedMethod().isAnnotationPresent(DenyAll.class)) {//deny if resource has the annotation denyall
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }

        String token = requestContext.getHeaderString("accesstoken");//getting the authorization token from the incoming request
        UserTasks user = new UserTasks();
        UserModel userData = new UserModel();
         

        try {
            userData = user.getUserInfoByToken(token);//check users permission and validity
            if (userData != null && userData.getUsername() != null && !userData.getUsername().equals("")) {
                SecurityContext context = new WruSecurityContext(userData, token);
                requestContext.setSecurityContext(context);//settiong security context 
            } else {//in case of failed authentication 403 error will be returned.
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            }
        } catch (SQLException ex) {
            Logger.getLogger(AuthunticationFilter.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("the user inside is : ");
        }

    }

}
