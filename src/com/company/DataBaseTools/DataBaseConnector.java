package com.company.DataBaseTools;
import com.company.cards.BaseCard;
import com.company.auxilliary.TableNames;

import java.sql.*;
public class DataBaseConnector {
    private Connection connection;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;

    public DataBaseConnector(String url, String user, String password){
        this.DB_URL= url;
        this.DB_USER= user;
        this.DB_PASSWORD= password;
    }

    public void connect() throws SQLException{
        this.connection= DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    public void disconnect() throws SQLException{
        this.connection.close();
    }
    public void createTables() throws SQLException{
        createUserTable();
        createTradeTable();
    }
    public void createUserTable(){
        PreparedStatement ps;
        try{
            ps=connection.prepareStatement("CREATE TABLE " + TableNames.getUserListTableName() +" (username varchar(255) not null , password varchar(255) not null, coins int default 20, elo int default 100, wins int default 0, losses int default 0, collection varchar(255))");
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("User-Table already exists");
        }
        try{
            ps=connection.prepareStatement("CREATE UNIQUE INDEX users_username_uindex ON users (username);");
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("User index already exists");
        }
    }

    private void createTradeTable(){
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("CREATE TABLE " + TableNames.getTradeTableName() +" (id serial, trader varchar(255) not NULL , cardID int not NULL, condition varchar(255) not NULL, power int not NULL)");
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println("Trade-Table already exists");
        }
    }

    public void setElo(String username, int eloChange) throws SQLException{
        PreparedStatement ps;
        int updatedElo;

        updatedElo=getElo(username)+eloChange;

        ps= connection.prepareStatement("UPDATE users SET elo=? WHERE username=?");
        ps.setInt(1, updatedElo);
        ps.setString(2, username);
        ps.executeUpdate();

    }

    public void setCoins(String username, int coinChange) throws SQLException{
        PreparedStatement ps;
        int updatedCoins;

        updatedCoins= getCoins(username)+ coinChange;

        ps= connection.prepareStatement("UPDATE users SET coins=? WHERE username=?");
        ps.setInt(1, updatedCoins);
        ps.setString(2, username);
        ps.executeUpdate();

    }

    public int getElo(String username) throws SQLException{
        PreparedStatement ps;
        ResultSet rs;
        int elo;

        ps=connection.prepareStatement("SELECT elo FROM users WHERE username=?");
        ps.setString(1, username);
        rs=ps.executeQuery();
        rs.next();
        elo= rs.getInt(1);
        rs.close();
        return elo;
    }

    public int getCoins(String username) throws SQLException{
        PreparedStatement ps;
        ResultSet rs;
        int coins;

        ps=connection.prepareStatement("SELECT coins FROM users WHERE username=?");
        ps.setString(1, username);
        rs=ps.executeQuery();
        rs.next();
        coins= rs.getInt(1);
        rs.close();
        return coins;
    }

    public void createUser(String username, String password) throws SQLException{
        PreparedStatement ps;
        String stackName= username + TableNames.getUserStackTableAddon();
        ps=connection.prepareStatement("INSERT INTO users (username, password, collection) VALUES (?,?,?)");
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, stackName);
        ps.executeUpdate();
        createUserTable(stackName);
    }

    private void createUserTable(String stackName) throws SQLException{
        PreparedStatement ps;
        ps=connection.prepareStatement("CREATE TABLE "+ stackName +" (id serial, name varchar(255) not NULL , power int not NULL , element varchar(255) not NULL , type varchar(255) not NULL, amount int default 1)");
        ps.executeUpdate();
    }

    public void addCard(BaseCard card, String username) throws SQLException{
        PreparedStatement ps;
        ResultSet rs;
        String table= username + TableNames.getUserStackTableAddon();
        ps= connection.prepareStatement("SELECT amount FROM " + table +" WHERE name =?");
        ps.setString(1, card.getName());
        rs= ps.executeQuery();

        if(rs.next()) {
            int amount= rs.getInt(1);
            amount+=1;

            ps=connection.prepareStatement("UPDATE " + table + " SET amount=?  WHERE name=?");
            ps.setInt(1, amount);
            ps.setString(2, card.getName());
            ps.executeUpdate();

        }
        else{
            ps=connection.prepareStatement("INSERT INTO " + table + " (name, power, element, type) VALUES (?,?,?,?)");
            ps.setString(1, card.getName());
            ps.setInt(2, card.getPower());
            ps.setString(3, card.getElement().toString());
            ps.setString(4, card.getType().toString());
            ps.executeUpdate();
        }

        rs.close();
    }

    public boolean tryLogin(String username, String password) throws SQLException{
        PreparedStatement ps;
        ResultSet rs;
        ps= connection.prepareStatement("SELECT * FROM users WHERE username =? AND password =?");
        ps.setString(1, username);
        ps.setString(2, password);
        rs=ps.executeQuery();
        if(rs.next()){
            rs.close();
            return true;
        }
        rs.close();
        return false;
    }

    public boolean tryTrade(){
        return true;
    }


}
