package com.company.stackTools.cards;
import com.company.auxilliary.enumUtils.CardType;
import com.company.auxilliary.enumUtils.Elements;

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
