package com.cukurova.tasks;

import com.cukurova.model.CoordinatesModel;
import com.cukurova.model.Location;
import com.cukurova.utils.Conn;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationTasks extends Location {

    public LocationTasks(CoordinatesModel coordinates, String locationName) {
        super(coordinates, locationName);
    }

    public LocationTasks() {
        super();
    }

    public Location getLocation() throws SQLException {

        ResultSet rs = new Conn().sqlExecuteSelect("SELECT * FROM saved_locations WHERE USERNAME = ? ORDER BY DATE_CREATED DESC LIMIT 1", "");
        if (rs.next()) {
            return extractFromResultSet(rs);
        } else {
            return null;
        }

    }

    private Location extractFromResultSet(ResultSet rs) {
        Location location = new Location();

        return location;
    }

    public void saveLocationBySecurityLevel(SecurityLevel security) throws SQLException {
        new Conn().sqlExecuteInsert("INSERT INTO saved_locations(USERNAME , LOCATION_NAME , SECURITY_LEVEL , LONGITIUDE , LATITUDE) VALUES (?,?,?,?,?) ",
                this.getLocationOwner(), this.getLocationName(), security.ordinal(), this.getCoordinates().getLng(), this.getCoordinates().getLat());
    }

    public SecurityLevel calculateLocationSecurityLevel() throws SQLException {
        ResultSet rs = new Conn().sqlExecuteSelect("", "");
        SecurityLevel level = SecurityLevel.INSECURE;
        return level;
    }
}
