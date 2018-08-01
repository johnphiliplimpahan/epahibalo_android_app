package com.app.epahibalo.e_pahibalo.Helpers;

public class URL {

    //public static final String SERVER_IP = "http://10.0.2.2";
    public static final String SERVER_IP = "http://192.168.1.8";
    public static final String PORT = "4000";
    public static final String BASE_URL = SERVER_IP+":"+PORT;
    public static final String IMAGE_PATH = SERVER_IP+"/epahibalo/";
    public static final String REQUEST_LOGIN_USER = BASE_URL+"/api/v1/epahibalo/login-user";
    public static final String REQUEST_BARANGAY_LIST = BASE_URL+"/api/v1/epahibalo/get-barangay-list";
    public static final String REQUEST_REGISTER_USER = BASE_URL+"/api/v1/epahibalo/register-user";
    public static final String REQUEST_CHECK_USERNAME = BASE_URL+"/api/v1/epahibalo/check-username";
    public static final String REQUEST_CHECK_CONTACT_NO = BASE_URL+"/api/v1/epahibalo/check-contact-number";
    public static final String REQUEST_UPDATE_PROFILE = BASE_URL+"/api/v1/epahibalo/update-citizen-profile";
    public static final String REQUEST_READINGS = BASE_URL+"/api/v1/jwws/readings-request";
    public static final String REQUEST_YEARS = BASE_URL+"/api/v1/jwws/years-request";
    public static final String REQUEST_RATES = BASE_URL+"/api/v1/jwws/rates-request";

}