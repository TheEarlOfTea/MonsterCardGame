package com.company.cards;
import com.company.auxilliary.CardType;
import com.company.auxilliary.Elements;

public class SpellCard extends BaseCard{
    public SpellCard(String name, int power, Elements element){
        super(name, power, element, CardType.SPELL);
    }
    public SpellCard(String name, int power){
        super(name, power, CardType.CREATURE);
    }
}
