package com.cukurova.api;

import com.cukurova.security.AuthunticationFilter;
import java.rmi.Naming;
import org.glassfish.jersey.server.ResourceConfig;

public class ServerInitiator extends ResourceConfig {

    public ServerInitiator() {

        packages("com.cukurova.api");
        register(AuthunticationFilter.class);
    }

}
