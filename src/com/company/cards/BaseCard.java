package com.company.cards;
import com.company.auxilliary.Elements;
import com.company.auxilliary.CardType;

public abstract class BaseCard {
    private final String name;
    private final int power;
    private final Elements element;
    private final CardType type;

    protected BaseCard(String name, int power, Elements element, CardType type){
        this.name=name;
        this.power=power;
        this.element=element;
        this.type=type;

    }
    protected BaseCard(String name, int power, CardType type){
        this.name=name;
        this.power=power;
        this.type=type;
        this.element= Elements.NORMAL;

    }

    public String toString(){
        String tabs= "\t\t\t";
        StringBuilder sb= new StringBuilder();
        sb.append(tabs + "Name: ");
        sb.append(this.name);
        sb.append("\n" + tabs + "Power: ");
        sb.append(this.power);
        sb.append("\n" + tabs + "Element: ");
        sb.append(this.element);
        sb.append("\n" + tabs + "Type: ");
        sb.append(this.type);
        return sb.toString();
    }

    public String getName(){
        return name;
    }
    public int getPower() {
        return this.power;
    }
    public Elements getElement() {
        return element;
    }
    public CardType getType() {
        return type;
    }

}
