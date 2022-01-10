package com.company.auxilliary;

public class TokenNames {
    public static String getAdminToken(){
        return "adminToken";
    }
    public static String getUserToken(String username){
        return (username+"-mtcgToken");
    }
}
