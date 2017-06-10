package com.cukurova.tasks;

import com.cukurova.model.TokenModel;
import com.cukurova.model.UserModel;
import com.cukurova.utils.Conn;
import com.cukurova.security.Secure;
import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

public class UserTasks {

    public UserTasks() {
    }

    public UserModel getUserInfoByUsername(String username) {

        return new UserModel(username);
    }

    public UserModel getUserInfoByToken(String accessToken) throws SQLException {
        ResultSet rs = new Conn().sqlExecuteSelect("SELECT USERNAME FROM tokens WHERE TOKEN = ?", accessToken);
        if (rs.next()) {
            String username = rs.getString(1);
            return new UserModel().getDataByUserName(username);
        } else {
            return null;
        }
    }

    public void doSignUp(UserModel user) throws SQLException, Exception {

        new Conn().sqlExecuteInsert("INSERT INTO user_info (EMAIL, FIRST_NAME, LAST_NAME , USERNAME, PASSWORD) VALUES(?,?,?,?,?)",
                user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), new Secure().hash(user.getPassword(), ""));

    }

    public Response doSignUpResponse(UserModel user) throws Exception {

        if (!user.hasConstraintViolationFree()) {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("Constraint Violation please check input and try again").build();
        }

        doSignUp(user);
        return Response.ok().entity("user created").build();
    }

    public Response doLoginResponse(String username, String password) throws Exception {

        TokenModel token = new TokenModel("", username);
        UserModel user = new UserModel(username, password);

        if (!user.credentialsMatch()) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Username or password is invalid.")
                    .build();
        } else {

            return Response
                    .ok(new Gson().toJson(token))
                    .header("acesstoken", token.getClientToken())
                    .build();
        }
    }

    public List<String> getFullFollowerNameList(String username) throws SQLException {
        List<String> rsltList = new ArrayList<>();
        ResultSet rs = new Conn().sqlExecuteSelect("SELECT CONCAT(user_info.FIRST_NAME , user_info.LAST_NAME) NAME , relations.FOLLOWER FROM relations "
                + " INNER JOIN user_info ON relations.FOLLOWER = user_info.USERNAME WHERE relations.FOLLOWED = ?", username);

        while (rs.next()) {
            rsltList.add(rs.getString("NAME"));
        }

        return rsltList;
    }

    public List<UserModel> getFullFollowerUserList(String username) throws SQLException {
        List<UserModel> rsltList = new ArrayList<>();
        UserModel user = new UserModel();

        ResultSet rs = new Conn().sqlExecuteSelect("SELECT CONCAT(user_info.FIRST_NAME ,' ', user_info.LAST_NAME) NAME , relations.FOLLOWER FROM relations "
                + " INNER JOIN user_info ON relations.FOLLOWER = user_info.USERNAME WHERE relations.FOLLOWED = ?", username);

        while (rs.next()) {
            user.setFirstName(rs.getString("NAME"));
            user.setInfo("Unknown Location");
            user.setStatus("Offline");
            rsltList.add(user);
        }

        return rsltList;
    }

}
