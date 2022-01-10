package com.company.cards;
import com.company.auxilliary.CardType;
import com.company.auxilliary.Elements;

public class SpellCard extends BaseCard{
    public SpellCard(String uid, String name, int power, Elements element){
        super(uid, name, power, element, CardType.SPELL);
    }
    public SpellCard(String uid, String name, int power){
        super(uid, name, power, CardType.CREATURE);
    }
    public SpellCard(){
        super();
    }
}
