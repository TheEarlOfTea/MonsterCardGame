package com.company.dataBaseTools;
import com.company.auxilliary.StringToEnumConverter;
import com.company.auxilliary.Token;
import com.company.auxilliary.TokenGenerator;
import com.company.auxilliary.User;
import com.company.cards.BaseCard;
import com.company.stackTools.Stack;

import java.sql.*;
public class DataBaseConnector {
    private Connection connection;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;

    public DataBaseConnector(){
        this.DB_URL=DBAuthentication.getDBLink();
        this.DB_USER=DBAuthentication.getDBUser();
        this.DB_PASSWORD=DBAuthentication.getDBPassword();
    }

    public void connect() throws SQLException{
        this.connection= DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    public void disconnect() throws SQLException{
        this.connection.close();
    }

    public void createUser(User user) throws SQLException{
        PreparedStatement ps;
        String stackName= TableNames.getUserStackTableName(user.getUsername());
        ps=connection.prepareStatement("INSERT INTO "+ TableNames.getUserListTableName()+" (username, password, collection, token) VALUES (?,?,?,?)");
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, stackName);
        ps.setString(4, TokenGenerator.getUserToken(user.getUsername()));
        ps.executeUpdate();
        createUserStackTable(stackName);
    }

    public void createUserStackTable(String stackName) throws SQLException{
        PreparedStatement ps;
        ps=connection.prepareStatement("CREATE TABLE "+ stackName +" (uid varchar(255), amount int default 1)");
        ps.executeUpdate();
        ps=connection.prepareStatement("CREATE UNIQUE INDEX " + stackName + "_uid_uindex ON " + stackName + " (uid);");
        ps.executeUpdate();
    }
    public void createPackageTable(Stack s) throws SQLException{
        PreparedStatement ps;
        ps=connection.prepareStatement("CREATE TABLE "+ TableNames.getPackageTableName(s.getOwner()) +" (uid varchar(255))");
        ps.executeUpdate();
        ps=connection.prepareStatement("CREATE UNIQUE INDEX " + TableNames.getPackageTableName(s.getOwner()) + "_uid_uindex ON " + TableNames.getPackageTableName(s.getOwner()) + " (uid);");
        ps.executeUpdate();

        ps=connection.prepareStatement("INSERT INTO " + TableNames.getPackageListTableName() + "(name) VALUES (?);");
        ps.setString(1, TableNames.getPackageTableName(s.getOwner()));
        ps.executeUpdate();

        addCardsToCardListTable(s);

        for(BaseCard b : s.getDeck()){
            addCardsToPackage(b.getUid(), s.getOwner());
        }
    }

    public boolean addCardToUserTable(String uid, String username) throws SQLException{
        PreparedStatement ps;
        ResultSet rs;
        BaseCard card= getCard(uid);
        if(card==null){
            return false;
        }
        String table= TableNames.getUserStackTableName(username);
        ps= connection.prepareStatement("SELECT amount FROM " + table +" WHERE uid =?");
        ps.setString(1, card.getUid());
        rs= ps.executeQuery();

        if(rs.next()) {
            int amount= rs.getInt(1);
            amount+=1;

            ps=connection.prepareStatement("UPDATE " + table + " SET amount=?  WHERE uid=?");
            ps.setInt(1, amount);
            ps.setString(2, card.getUid());
            ps.executeUpdate();

        }
        else{
            ps=connection.prepareStatement("INSERT INTO " + table + " (uid) VALUES (?)");
            ps.setString(1, card.getUid());
            ps.executeUpdate();
        }

        rs.close();
        return true;
    }

    public boolean addCardsToPackage(String uid, String tableName) throws SQLException{
        PreparedStatement ps;
        BaseCard card= getCard(uid);
        if(card==null){
            return false;
        }
        ps= connection.prepareStatement("INSERT INTO " + TableNames.getPackageTableName(tableName) + "(uid) VALUES (?);");
        ps.setString(1, card.getUid());
        ps.executeUpdate();
        return true;
    }
    public boolean checkForValue(String field, String table,  String value) throws SQLException{
        PreparedStatement ps;
        ResultSet rs;
        ps=connection.prepareStatement("SELECT " + field + " FROM " + table + " WHERE " + field + "=?");
        ps.setString(1, TableNames.getPackageTableName(value.toLowerCase()));
        rs=ps.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    public boolean addCardsToCardListTable(Stack d){
        PreparedStatement ps;
        boolean wasIssue=false;
        for(BaseCard b: d.getDeck()){
            try{
                ps=connection.prepareStatement("INSERT INTO "+ TableNames.getCardListTableName() + " (uid, name, power, element, type) VALUES (?,?,?,?,?)");
                ps.setString(1, b.getUid());
                ps.setString(2,b.getName());
                ps.setInt(3,b.getPower());
                ps.setString(4, b.getElement().toString());
                ps.setString(5, b.getType().toString());
                ps.executeUpdate();
            }catch (SQLException e){
                wasIssue=true;
            }
        }
        return wasIssue;
    }

    public Token tryLogin(User user) throws SQLException{
        PreparedStatement ps;
        ResultSet rs;
        Token token=new Token();
        ps= connection.prepareStatement("SELECT token FROM users WHERE username =? AND password =?");
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        rs=ps.executeQuery();
        if(rs.next()){
            token.setToken(rs.getString("token"));
            rs.close();
            return token;
        }
        rs.close();
        token.setToken("NO_TOKEN");
        return token;
    }

    public Stack getDeckFromTable(String name, String options) throws SQLException{
        String tableName;
        Stack d= new Stack(name);
        switch(options){
            case "-u":
                tableName=TableNames.getUserStackTableName(name);
                break;
            case "-p":
                tableName=TableNames.getPackageTableName(name);
                break;
            default:
                return d;
        }
        BaseCard b;
        PreparedStatement ps;
        ResultSet uids;
        try{
            ps=connection.prepareStatement("SELECT uid FROM " + tableName);
            uids=ps.executeQuery();
            while(uids.next()){
                if((b=getCard(uids.getString(1)))!=null){
                    d.addCards(b);
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  d;
    }

    public BaseCard getCard (String uid) throws SQLException{
        PreparedStatement ps;
        ResultSet rs;
        BaseCard b= new BaseCard();
        ps=connection.prepareStatement("SELECT * FROM " + TableNames.getCardListTableName() + " WHERE uid=?");
        ps.setString(1,uid);
        rs=ps.executeQuery();
        if(rs.next()){
            b.setUid(rs.getString(1));
            b.setName(rs.getString(2));
            b.setPower(rs.getInt(3));
            b.setElement(StringToEnumConverter.getElement(rs.getString(4)));
            b.setType(StringToEnumConverter.getCardType(rs.getString(5)));
            return b;
        }
        return null;
    }

    public boolean tryTrade(){
        return true;
    }

    public Token checkToken(String givenToken){
        PreparedStatement ps;
        ResultSet rs;
        Token token= new Token();
        try {
            ps = connection.prepareStatement("SELECT username, token FROM " + TableNames.getUserListTableName() + " WHERE token=?;");
            ps.setString(1, givenToken);
            rs = ps.executeQuery();
            if (rs.next()) {
                token.setUsername(rs.getString("username"));
                token.setToken(rs.getString("token"));
            }
            rs.close();
            return token;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return token;
        }
    }
    public boolean checkAdminToken(String givenToken){
        if(givenToken.compareTo(TokenGenerator.getAdminToken())==0){
            return true;
        }
        return false;
    }

    public void changeElo(String username, int eloChange) throws SQLException{
        PreparedStatement ps;
        int updatedElo;

        updatedElo=getElo(username)+eloChange;

        ps= connection.prepareStatement("UPDATE users SET elo=? WHERE username=?");
        ps.setInt(1, updatedElo);
        ps.setString(2, username);
        ps.executeUpdate();

    }

    public void changeCoins(String username, int coinChange) throws SQLException{
        PreparedStatement ps;
        int updatedCoins;

        updatedCoins= getCoins(username) + coinChange;

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


}
