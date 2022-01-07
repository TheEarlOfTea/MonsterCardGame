package com.company;
import com.company.DataBaseTools.DataBaseConnector;
import com.company.auxilliary.CardType;
import com.company.auxilliary.Elements;
import com.company.cards.BaseCard;
import com.company.cards.CreatureCard;
import com.company.cards.SpellCard;
import com.company.deckTools.Deck;
import com.company.engine.Engine;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        DataBaseConnector db= new DataBaseConnector("jdbc:postgresql://localhost:5432/mydb", "postgres", "");
        try{
            db.connect();
            if(db.tryLogin("rt", "test")){
                System.out.println("login erfolgreich");
            }
            else{
                System.out.println("login ist fehlgeschlagen");
            }
            db.disconnect();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
