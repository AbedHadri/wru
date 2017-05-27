package com.cukurova.api;

import com.cukurova.model.UserModel;
import com.cukurova.tasks.NotificationTasks;
import com.cukurova.tasks.UserTasks;
import com.cukurova.utils.DateOps;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.text.ParseException;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class WruApi {
//
//    @GET
//    @Path("my-info")
//    @Produces(MediaType.APPLICATION_JSON)
//    public UserModel getUserInfoByUsername();

    @POST
    @Path("/login")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doLoginResponse(UserModel user) throws Exception {
        return new UserTasks().doLoginResponse(user.getUsername(), user.getPassword());
    }

    @GET
    @Path("/getlogin")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doLoginResponse(@QueryParam("username") String username, @QueryParam("password") String password) throws Exception {
        return new UserTasks().doLoginResponse(username, password);
    }

    @POST
    @PermitAll
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response doSignUpResponse(UserModel user) throws Exception {
        return new UserTasks().doSignUpResponse(user);

    }

    @GET
    @Path("get")
    @RolesAllowed("USER")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getUserNotificationsResponse(@HeaderParam("accesstoken") String userToken) throws SQLException {
        return Response.ok(new Gson().toJson(new NotificationTasks().pullUserNotifications(userToken, 5))).build();
    }

    @GET
    @Path("get-live")
    @RolesAllowed("USER")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getLiveNotificationsResponse(@QueryParam("lastUpdateDate") String lastUpdateDate, @HeaderParam("accesstoken") String userToken) throws SQLException, InterruptedException, ParseException {

        return Response.ok(new Gson().toJson(new NotificationTasks().liveNotifications(userToken, DateOps.stringToDate(lastUpdateDate)))).build();
    }

    @GET
    @Path("set-seen")
    @RolesAllowed("USER")
    public Response setNotificationsSeenResponse(@HeaderParam("accesstoken") String userToken) throws SQLException {
        new NotificationTasks().setUserNotificationsSeen(userToken);
        return Response.ok().build();
    }

}
