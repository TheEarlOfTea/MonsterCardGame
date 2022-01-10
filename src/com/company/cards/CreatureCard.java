package com.company.cards;
import com.company.auxilliary.Elements;
import com.company.auxilliary.CardType;

public class CreatureCard extends BaseCard{
    public CreatureCard(String uid, String name, int power, Elements element){
        super(uid, name, power, element, CardType.CREATURE);
    }
    public CreatureCard(String uid, String name, int power){
        super(uid, name, power, CardType.CREATURE);
    }
    public CreatureCard(){
        super();
    }
}
