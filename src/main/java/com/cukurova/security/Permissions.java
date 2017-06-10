package com.cukurova.security;

import com.cukurova.utils.Conn;
import java.sql.SQLException;

public class Permissions {

    public static boolean hasPermissionToReach(String requestOwner, String questedUser) throws SQLException {
        return new Conn().sqlExecuteSelect("SELECT * FROM RELATIONS WHERE FOLLOWER = ? AND FOLLOWED = ?", requestOwner, questedUser).next();
    }
}
