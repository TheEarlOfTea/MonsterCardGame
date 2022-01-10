package com.company.auxilliary.enumUtils;

public class StringToEnumConverter {
    public static Elements getElement(String element){
        switch (element.toUpperCase()){
            case "FIRE":
                return Elements.FIRE;
            case "WATER":
                return Elements.WATER;
            case "PLANT":
                return Elements.PLANT;
            case "NORMAL":
                return Elements.NORMAL;
        }
    return null;
    }
    public static CardType getCardType(String type){
        switch (type.toUpperCase()){
            case "CREATURE":
                return CardType.CREATURE;
            case "SPELL":
                return CardType.SPELL;
        }
        return null;
    }
}
