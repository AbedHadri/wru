package com.cukurova.tasks;

import com.cukurova.model.TokenModel;
import com.cukurova.model.UserModel;
import com.cukurova.utils.Conn;
import com.cukurova.utils.Secure;
import com.cukurova.utils.SystemConstants;
import com.google.gson.Gson;
import java.sql.SQLException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class UserTasks {

    public UserModel getUserInfoByUsername(String username) {

        UserModel user = new UserModel(username);

        return user;
    }

    public void doSignUp(UserModel user) throws SQLException, Exception {

        new Conn().sqlExecuteInsert("INSERT INTO USER_INFO (EMAIL, FIRST_NAME, LAST_NAME , USERNAME, PASSWORD) VALUES(?,?,?,?,?)",
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

}
