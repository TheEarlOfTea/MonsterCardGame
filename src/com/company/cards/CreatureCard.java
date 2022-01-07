package com.company.cards;
import com.company.auxilliary.Elements;
import com.company.auxilliary.CardType;

public class CreatureCard extends BaseCard{
    public CreatureCard(String name, int power, Elements element){
        super(name, power, element, CardType.CREATURE);
    }
    public CreatureCard(String name, int power){
        super(name, power, CardType.CREATURE);
    }
}
