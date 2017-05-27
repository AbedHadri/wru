package com.cukurova.api;

import com.cukurova.utils.SystemConstants;
import com.cukurova.utils.Conn;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("server-status")//  192.168.1.xx/webapi/server-status
public class ServerStatusTest {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() throws SQLException {

        Conn conn = new Conn(); 
        return "Server is Online! " + conn.startConnectionDebugger();
    }
}
