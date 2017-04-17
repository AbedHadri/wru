package com.cukurova.webapis;

import com.cukurova.operations.Conn;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("server-status")//  192.168.1.25/webapi/server-status
public class MyResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {

        Conn conn = new Conn();

        return "Got it! " + conn.startConnectionDebugger();
    }
}
