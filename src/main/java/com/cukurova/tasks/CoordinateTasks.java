package com.cukurova.tasks;

import com.cukurova.model.CoordinatesModel;
import com.cukurova.utils.Conn;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.core.Response;

public class CoordinateTasks extends CoordinatesModel {

    /*
    *call parent 3 parameter constructor, parent here is CoordinatesModel class. 
    *since coordinateTasks(this class) extends CoordinatesModel at least one of the constructors MUST be called in this class's constructors
     */
    public CoordinateTasks(String coordOwner, BigDecimal lng, BigDecimal lat) {

        super(coordOwner, lng, lat);
    }

    /*
    *call parent non-parameter constructor parent here is CoordinatesModel class
     */
    public CoordinateTasks() {
        super();
    }

    /*
    *call parent non-parameter constructor parent here is CoordinatesModel class
     */
    public CoordinateTasks(String username) {
        super(username);
    }

    public void pushCoordinatesToDataBase() throws SQLException {
        //initiate connection to perform database operations (select insert update ..etc)
        Conn conn = new Conn();
        //we will insert here this class's elements to the database
        conn.sqlExecuteInsert("INSERT INTO coordinates (USERNAME , X_AXIS , Y_AXIS , UPDATE_DATE, LOCATION_ID) VALUES(?,?,?,NOW(),?)",
                this.getCoordOwner(), this.getLat(), this.getLng(), "");

    }

    public Response pushCoordinatesToDataBaseResponse(String owner, BigDecimal longitude, BigDecimal latitude) throws SQLException {
        CoordinateTasks cTasks = new CoordinateTasks(owner, longitude, latitude);
        cTasks.pushCoordinatesToDataBase();
        return Response.ok().build();
    }

    public CoordinatesModel getContactCoordinates(String requestUser, String coordOwner) throws SQLException {
        CoordinatesModel coordinates = new CoordinatesModel();

        ResultSet rs = new Conn().sqlExecuteSelect("SELECT * FROM coordinates"
                + " INNER JOIN relations ON relations.FOLLOWER = ? AND ACCEPTED = 1"
                + " WHERE USERNAME = ? ORDER BY UPDATE_DATE DESC LIMIT 1", requestUser, coordOwner);

        if (rs.next()) {
            coordinates = extractFromResultSet(rs);
        }

        return coordinates;
    }

    public void generateSosSignal() throws SQLException {
        Conn conn = new Conn();
        conn.initBatch("INSERT INTO notifications (USER_ID , TITLE , CONTENT , LINK) VALUES (?,?,?,?)");
        UserTasks user = new UserTasks();

        List<String> followers = user.getFullFollowerNameList(this.getCoordOwner());

        for (String follower : followers) {
            conn.addToBatch(follower, "I need Help!", "I trigger the Sos to ask you to help me I'm in the location in the link.", "");
        }
        conn.executeBatch();
    }

    private CoordinateTasks extractFromResultSet(ResultSet rs) throws SQLException {
        CoordinateTasks coord = new CoordinateTasks();
        coord.setCoordOwner(rs.getString("USERNAME"));
        coord.setLat(rs.getBigDecimal("X_AXIS"));
        coord.setLng(rs.getBigDecimal("Y_AXIS"));
        return coord;
    }

}
