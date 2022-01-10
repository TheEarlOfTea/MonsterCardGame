package com.company.dataBaseTools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseSetup {
    public static void setUp() throws SQLException {
        Connection connection= DriverManager.getConnection(DBAuthentication.getDBLink(), DBAuthentication.getDBUser(), DBAuthentication.getDBPassword());
        createUserTable(connection);
        createCardTable(connection);
        createTradeTable(connection);
        createPackageListTable(connection);
        connection.close();
    }

    private static void createUserTable(Connection connection){
        PreparedStatement ps;
        try{
            ps=connection.prepareStatement("CREATE TABLE " + TableNames.getUserListTableName() +" (username varchar(255) not null , password varchar(255) not null, coins int default 20, elo int default 100, wins int default 0, losses int default 0, battleready int default 0, collection varchar(255), token varchar(255), name varchar(255) default 'EMPTY_NAME', bio varchar(255) default 'EMPTY BIO', image varchar(255) default 'EMPTY IMAGE')");
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        try{
            ps=connection.prepareStatement("CREATE UNIQUE INDEX users_username_uindex ON users (username);");
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void createCardTable(Connection connection){
        PreparedStatement ps;
        try{
            ps=connection.prepareStatement("CREATE TABLE "+ TableNames.getCardListTableName() + " (uid varchar(255) not null, name varchar(255) not null, power integer not null, element varchar(255) not null, type varchar(255) not null)");
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    private static void createTradeTable(Connection connection){
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("CREATE TABLE " + TableNames.getTradeTableName() +" (id serial, trader varchar(255) not NULL , cardUID varchar(255) not NULL, condition varchar(255) not NULL, power int not NULL)");
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void createPackageListTable(Connection connection){
        PreparedStatement ps;
        try{
            ps=connection.prepareStatement("CREATE TABLE "+ TableNames.getPackageListTableName() +" (id serial, name varchar(255) not null)");
            ps.executeUpdate();
            ps=connection.prepareStatement("CREATE UNIQUE INDEX " + TableNames.getPackageListTableName()+ "_name_uindex ON " + TableNames.getPackageListTableName() + " (name);");
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
