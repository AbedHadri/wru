package com.cukurova.tasks;

import com.cukurova.model.CoordinatesModel;
import com.cukurova.utils.Conn;
import com.cukurova.utils.DateOps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Response;

public class CoordinateTasks extends CoordinatesModel {

    /*
    *call parent 3 parameter constructor, parent here is CoordinatesModel class. 
    *since coordinateTasks(this class) extends CoordinatesModel at least one of the constructors MUST be called in this class's constructors
     */
    public CoordinateTasks(String coordOwner, double lng, double lat) {

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

    public void updateCoordinates(Date createDate) throws SQLException {
        //initiate connection to perform database operations (select insert update ..etc)
        Conn conn = new Conn();
        //we will insert here this class's elements to the database
        conn.sqlExecuteUpdate("UPDATE coordinates SET   X_AXIS = ? , Y_AXIS = ? , UPDATE_DATE = NOW()  WHERE USERNAME = ? AND CREATE_DATE = ?",
                this.getLat(), this.getLng(), this.getCoordOwner(), createDate);

    }

    public Response pushCoordinatesToDataBaseResponse(String owner, double longitude, double latitude) throws SQLException {
        CoordinateTasks cTasks = new CoordinateTasks(owner, longitude, latitude);
        CoordinatesModel lastCoord = getContactCoordinates(owner, owner);
        if (lastCoord.getCreateDate() == null || (lastCoord.getCreateDate() != null && DateOps.periodBetweenDates(lastCoord.getCreateDate(), lastCoord.getUpdateDate(), DateOps.PeriodUnit.HOUR) >= 1)) {
            cTasks.pushCoordinatesToDataBase();
        } else {
            cTasks.updateCoordinates(lastCoord.getCreateDate());
        }

        return Response.ok().build();
    }

    public CoordinatesModel getContactCoordinates(String requestUser, String coordOwner) throws SQLException {
        CoordinatesModel coordinates = new CoordinatesModel();

        ResultSet rs;

        if (requestUser == coordOwner) {
            rs = new Conn().sqlExecuteSelect("SELECT * FROM coordinates"
                    + " WHERE USERNAME = ? ORDER BY UPDATE_DATE DESC LIMIT 1", coordOwner);
        } else {
            rs = new Conn().sqlExecuteSelect("SELECT * FROM coordinates"
                    + " INNER JOIN relations ON relations.FOLLOWER = ? AND relations.ACCEPTED = 1"
                    + " WHERE coordinates.USERNAME = ? ORDER BY coordinates.UPDATE_DATE DESC LIMIT 1", requestUser, coordOwner);
        }

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
        coord.setLat(rs.getDouble("X_AXIS"));
        coord.setLng(rs.getDouble("Y_AXIS"));
        coord.setUpdateDate(DateOps.getDateFromTimeStamp(rs.getTimestamp("UPDATE_DATE")));
        coord.setCreateDate(DateOps.getDateFromTimeStamp(rs.getTimestamp("CREATE_DATE")));
        return coord;
    }

}
