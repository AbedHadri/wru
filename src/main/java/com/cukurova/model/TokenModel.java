package com.cukurova.model;

import com.cukurova.utils.Conn;
import com.cukurova.security.PasswordGenerator;
import com.cukurova.security.Secure;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public class TokenModel {

    static final int USER = 1;
    static final int TOKEN = 2;

    private String clientToken;
    private String username;
    private Date validity;

    public TokenModel() {
    }

    public TokenModel(String username) {
        this.username = username;
    }

    public TokenModel(String clientToken, String username) throws SQLException, ParseException {
        this.clientToken = clientToken;
        this.username = username;

        if (clientToken == "") {
            this.clientToken = createAndPushToken(username);
        }

    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientToken() {
        return clientToken;
    }

    public String getUsername() {
        return username;
    }

    public Date getValidity() {
        return validity;
    }

    public boolean hasDuplicatedToken(String clientToken) throws SQLException {
        Conn conn = new Conn();
        return conn.sqlExecuteSelect("SELECT TOKEN FROM tokens WHERE TOKEN = ?", clientToken).next();
    }

    public void pushTokenData() throws SQLException {
        Conn conn = new Conn();
        conn.sqlExecuteInsert("INSERT INTO tokens(TOKEN,USERNAME) VALUES(?,?)", this.clientToken, this.username);
    }

    /**
     *
     * @param username
     * @return
     * @throws SQLException
     * @throws ParseException 
     * This methods takes the username and it returns the
     * created token.
     */
    public String createAndPushToken(String username) throws SQLException, ParseException {

        PasswordGenerator pg;
        TokenModel tokenData = new TokenModel();
        String chosenToken;

        do {
            pg = new PasswordGenerator(170, 190);
            chosenToken = String.valueOf(pg.generatePassword());
        } while (this.hasDuplicatedToken(chosenToken));

        tokenData.setClientToken(chosenToken);
        tokenData.setUsername(username);
        tokenData.pushTokenData();

        return tokenData.getClientToken();

    }
}
