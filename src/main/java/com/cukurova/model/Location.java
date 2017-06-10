package com.cukurova.model;

public class Location {

    private CoordinatesModel coordinates;
    private String locationName;
    private String locationOwner;
    private String requestOwner;

    public Location() {

    }

    public Location(CoordinatesModel coordinates, String locationName) {
        this.coordinates = coordinates;
        this.locationName = locationName;
    }

    public Location(CoordinatesModel coordinates, String locationName, String locationOwner, String requestOwner) {
        this.coordinates = coordinates;
        this.locationName = locationName;
        this.locationOwner = locationOwner;
        this.requestOwner = requestOwner;
    }

    public enum SecurityLevel {
        SECURE(10), NEUTRAL(5), INSECURE(0);

        private int value;

        public void setSecureLevel(int value) {
            this.value = value;

        }

         SecurityLevel(int value) {
            this.value = value;
        }

        public int getNumericValue() {
            return this.value;
        }
        
        public static SecurityLevel fromId(int id) {
                for (SecurityLevel type : SecurityLevel.values()) {
                    if (type.getNumericValue() == id) {
                        return type;
                    }
                }
                return null;
            }
    }

    public CoordinatesModel getCoordinates() {
        return coordinates;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationOwner() {
        return locationOwner;
    }

    public String getRequestOwner() {
        return requestOwner;
    }

    public void setCoordinates(CoordinatesModel coordinates) {
        this.coordinates = coordinates;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationOwner(String locationOwner) {
        this.locationOwner = locationOwner;
    }

    public void setRequestOwner(String requestOwner) {
        this.requestOwner = requestOwner;
    }

}
