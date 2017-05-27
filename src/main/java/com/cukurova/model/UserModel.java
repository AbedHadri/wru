package com.cukurova.model;

import com.cukurova.utils.Conn;
import com.cukurova.utils.Secure;
import com.cukurova.utils.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserModel {

    private String username;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String email;

    private String password;

    public UserModel() {
    }

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    static public String eee() {
        return "";
    }

    public UserModel(String username, String firstName, String lastName, String phoneNo, String email, String password) {

        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;

    }

    public UserModel(String username) {

        this.username = username;
        Conn conn = new Conn();
        ResultSet rs;

        try {
            rs = conn.sqlExecuteSelect("SELECT * FROM USER_DATA WHERE USERNAME = ?", username);

            if (rs.next()) {
                getUserByResultSet(rs);
            } else {
                this.username = null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    private void getUserByResultSet(ResultSet rs) throws SQLException {

        this.email = rs.getString("EMAIL");
        this.firstName = rs.getString("FIRST_NAME");
        this.lastName = rs.getString("LAST_NAME");
        this.password = rs.getString("PASSWORD");
        this.username = rs.getString("USERNAME");

    }

    public boolean usernameExists() throws Exception {
        return new Conn().sqlExecuteSelect("SELECT USERNAME FROM USER_INFO WHERE USERNAME = ?  ", this.username).next();
    }

    public boolean credentialsMatch() throws SQLException, Exception {

        return new Conn().sqlExecuteSelect("SELECT USERNAME FROM USER_INFO WHERE USERNAME = ? AND PASSWORD = ?", this.username, new Secure().hash(this.password, "")).next();
    }

    public boolean hasConstraintViolationFree() throws Exception {
        if (this.usernameExists() && this.username.length() < 3 && this.username.length() > 50) {
            return false;
        } else if (this.firstName == null || this.firstName.length() < 3) {
            return false;
        } else if (this.email == null || !StringUtils.isEmail(this.email)) {
            return false;
        } else if (this.password == null || this.password.length() < 5) {
            return false;
        } else {
            return true;
        }
    }
}
