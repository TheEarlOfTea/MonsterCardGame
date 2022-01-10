package com.company.auxilliary;

public class TokenGenerator {
    public static String getAdminToken(){
        return "adminToken";
    }
    public static String getUserToken(String username){
        return (username+"-mtcgToken");
    }
}
