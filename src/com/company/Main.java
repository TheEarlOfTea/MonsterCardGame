package com.company;
import com.company.application.MTCG;
import com.company.auxilliary.Profile;
import com.company.auxilliary.Token;
import com.company.dataBaseTools.DataBaseSetup;
import com.company.server.MyServer;
import com.company.dataBaseTools.DataBaseConnector;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException{

        DataBaseSetup.setUp();
        startServer();
        //DB();



    }

    public static void startServer(){
        MyServer server = new MyServer(new MTCG());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void DB(){
        DataBaseConnector db= new DataBaseConnector();
        try{
            db.connect();
            db.disconnect();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
