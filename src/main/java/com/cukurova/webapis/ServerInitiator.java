
package com.cukurova.webapis;

import org.glassfish.jersey.server.ResourceConfig;
 

public class ServerInitiator extends ResourceConfig {
    
    public ServerInitiator() {
        
        packages("com.keysoft.api");
        //register(LoggingFilter.class);
        register(com.cukurova.security.AuthunticationFilter.class);
    }

} 