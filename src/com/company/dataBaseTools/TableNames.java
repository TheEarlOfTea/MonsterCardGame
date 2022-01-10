package com.company.dataBaseTools;

public class TableNames {
    public static String getUserListTableName(){
        return "users";
    }
    public static String getTradeTableName(){
        return "trades";
    }
    public static String getUserStackTableName(String username){
        return "stack_"+username;
    }
    public static String getCardListTableName(){ return "cards"; }
    public static String getPackageTableName(String name){ return "package_"+name; }
    public static String getPackageListTableName(){ return "packages"; }
}
