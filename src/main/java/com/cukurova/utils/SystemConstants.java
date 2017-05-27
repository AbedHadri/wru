package com.cukurova.utils;

public class SystemConstants {

    /**
     * The secret key to use on public APIs which means that APIs like login
     * will prompt client to send this key to prevent clients who didn't
     * download the application exclusively made for this program from using
     * project's APIs
     */
    public static final String API_KEY = "OASKOPAFoZgEKRFJI43T5049AbEd0RFJI2RFN34OGO34LELO9MWL";

    /**
     * The system's standard permission types. To avoid misnaming the cases they
     * are here parameterized.
     */
    public enum Permissions {

        USER(1), ADMIN(2);
        int i;

        private Permissions(int i) {
            this.i = i;
        }
    }

}
