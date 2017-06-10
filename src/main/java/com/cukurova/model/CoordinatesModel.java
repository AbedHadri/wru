package com.cukurova.model;

import java.math.BigDecimal;

/**
 * Longitude = boylam\n latitude = enlem\n This is the data model for Google
 * Maps , its usage is easier than other APIs. To apply any tasks on this model
 * check tasks.CoordinateTasks. longitude will represent the Y-Axis , latitude
 * will represent X-Axis
 */
public class CoordinatesModel {
    
    private String coordOwner;
    /**
     * user issuing the request. used to prevent claiming of data from unauthorized users.
     */
    private String requestUser;
    /**
     * this is stored as Y_AXIS in the database.
     */
    private BigDecimal lng;
    /**
     * this is stored as X_AXIS in the database.
     */
    private BigDecimal lat;

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public CoordinatesModel(String coordinateOwner, BigDecimal lng, BigDecimal lat) {
        this.lng = lng;
        this.lat = lat;
        this.coordOwner = coordinateOwner;
    }

    public CoordinatesModel(String coordinateOwner) {

        this.coordOwner = coordinateOwner;
    }

    public void setCoordOwner(String coordOwner) {
        this.coordOwner = coordOwner;
    }

    public String getCoordOwner() {
        return coordOwner;
    }

    public CoordinatesModel() {
        coordOwner = "";//setting everything to default so we don't get NullPointerException
        lng = BigDecimal.ZERO;
        lat = BigDecimal.ZERO;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

}
