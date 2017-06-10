package com.cukurova.api;

import com.cukurova.model.CoordinatesModel;
import com.cukurova.model.UserModel;
import com.cukurova.tasks.CoordinateTasks;
import com.cukurova.tasks.NotificationTasks;
import com.cukurova.tasks.UserTasks;
import com.cukurova.utils.DateOps;
import com.google.gson.Gson;
import java.math.BigDecimal;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/")
public class WruApi {

    private String getUserName() {
        return context.getUserPrincipal().getName();
    }

    @HeaderParam("accesstoken")
    String userToken;

    @Context
    SecurityContext context;

    @GET
    @Path("/user-info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfoByUsername() throws SQLException {

        return Response.ok(new Gson().toJson(new UserTasks().getUserInfoByToken(userToken))).build();
    }

    @POST
    @Path("/login")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response doLoginResponse(UserModel user) throws Exception {
        return new UserTasks().doLoginResponse(user.getUsername(), user.getPassword());
    }

    @POST
    @PermitAll
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response doSignUpResponse(UserModel user) throws Exception {
        return new UserTasks().doSignUpResponse(user);

    }

    @POST
    @RolesAllowed("USER")
    @Path("/coords")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response doPushCoordinatesToDataBaseResponse(CoordinatesModel coords) throws Exception {
        return new CoordinateTasks()
                .pushCoordinatesToDataBaseResponse(getUserName(), coords.getLng(), coords.getLat());

    }

    @GET
    @Path("notification/get")
    @RolesAllowed("USER")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getUserNotificationsResponse() throws SQLException {
        return Response.ok(new Gson().toJson(new NotificationTasks().pullUserNotifications(getUserName(), 5))).build();
    }

    @GET
    @Path("notification/get-live")
    @RolesAllowed("USER")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getLiveNotificationsResponse(@QueryParam("lastUpdateDate") String lastUpdateDate) throws SQLException, InterruptedException, ParseException {

        return Response
                .ok(new Gson().toJson(new NotificationTasks().liveNotifications(getUserName(), DateOps.stringToDate(lastUpdateDate)))).build();
    }

    @GET
    @Path("notification/set-seen")
    @RolesAllowed("USER")
    public Response setNotificationsSeenResponse() throws SQLException {
        new NotificationTasks().setUserNotificationsSeen(getUserName());
        return Response.ok().build();
    }

    @GET
    @Path("contact-list")
    @RolesAllowed("USER")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getFollowedList() throws SQLException {

        return Response.ok(new UserTasks().getFullFollowerUserList(getUserName())).build();
    }

}
