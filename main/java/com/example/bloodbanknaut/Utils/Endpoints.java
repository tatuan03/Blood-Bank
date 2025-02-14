package com.example.bloodbanknaut.Utils;

public class Endpoints {
//    http://172.22.6.167
    private static final String base_url = "http://192.168.1.17/blood_bank/";

    public static final String register_url = base_url+"register.php";
    public static final String login_url = base_url+"login.php";
    public static final String upload_request = base_url + "upload_request.php";
    public static final String get_requests = base_url + "get_requests.php";
    public static final String search_donors = base_url + "search_donors.php";
}
