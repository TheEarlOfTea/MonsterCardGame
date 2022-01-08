package com.company;
import com.company.application.MTCG;
import com.company.application.MonsterTradingCardGame;
import com.company.server.MyServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        startServer();

        /*DataBaseConnector db= new DataBaseConnector("jdbc:postgresql://localhost:5432/mydb", "postgres", "");
        try{
            db.connect();
            db.addCard(new CreatureCard("FireGoblin", 50, Elements.FIRE), "test");
            db.addCard(new CreatureCard("FireGoblin", 50, Elements.FIRE), "test");
            db.addCard(new SpellCard("WaterSpout", 20, Elements.WATER), "test");
            db.disconnect();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }*/
    }

    public static void startServer(){
        MyServer server = new MyServer(new MTCG());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
