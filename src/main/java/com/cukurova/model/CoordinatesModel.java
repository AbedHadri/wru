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
    private BigDecimal lng;
    private BigDecimal lat;

    public CoordinatesModel(String coordinateOwner, BigDecimal lng, BigDecimal lat) {
        this.lng = lng;
        this.lat = lat;
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

    public void setLongitude(BigDecimal lng) {
        this.lng = lng;
    }

    public void setLatitude(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLongitude() {
        return lng;
    }

    public BigDecimal getLatitude() {
        return lat;
    }

}
