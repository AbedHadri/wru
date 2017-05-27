package com.cukurova.tasks;

import com.cukurova.model.CoordinatesModel;
import com.cukurova.utils.Conn;
import java.math.BigDecimal;
import java.sql.SQLException;
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

    public void pushCoordinatesToDataBase() throws SQLException {
        //initiate connection to perform database operations (select insert update ..etc)
        Conn conn = new Conn();
        //we will insert here this class's elements to the database
        conn.sqlExecuteInsert("INSERT INTO LOCATION_INFO (USERNAME , X_AXIS , Y_AXIS , UPDATE_DATE, LOCATION_ID) VALUES(?,?,?,NOW(),'')", this.getCoordOwner(), this.getLatitude(), this.getLongitude());

    }

    public Response pushCoordinatesToDataBaseResponse(String owner, BigDecimal longitude, BigDecimal latitude) throws SQLException {
        CoordinateTasks cTasks = new CoordinateTasks(owner, longitude, latitude);
        cTasks.pushCoordinatesToDataBase();
        return Response.ok().build();
    }
    
//    public 

}
