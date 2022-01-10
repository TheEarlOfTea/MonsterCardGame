package com.company.dataBaseTools;

public class DBAuthentication {
    public static String getDBLink(){
        return "jdbc:postgresql://localhost:5432/mydb";
    }
    public static String getDBUser(){
        return "postgres";
    }
    public static String getDBPassword(){
        return "";
    }
}
