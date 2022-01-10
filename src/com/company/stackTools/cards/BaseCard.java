package com.company.stackTools.cards;
import com.company.auxilliary.enumUtils.Elements;
import com.company.auxilliary.enumUtils.CardType;

public class BaseCard {
    private  String name;
    private  int power;
    private  Elements element;
    private  CardType type;
    private  String uid;

    public BaseCard(String uid, String name, int power, Elements element, CardType type){
        this.name=name;
        this.power=power;
        this.element=element;
        this.type=type;
        this.uid=uid;
    }
    public BaseCard(String uid, String name, int power, CardType type){
        this.name=name;
        this.power=power;
        this.type=type;
        this.element= Elements.NORMAL;
        this.uid=uid;
    }
    public BaseCard(){
        name=null;
        power=0;
        element=null;
        type=null;
        uid=null;
    }

    public String toString(){
        String tabs= "\t\t\t";
        StringBuilder sb= new StringBuilder();
        sb.append(tabs + "UID: ");
        sb.append(this.uid);
        sb.append("\n" + tabs + "Name: ");
        sb.append(this.name);
        sb.append("\n" + tabs + "Power: ");
        sb.append(this.power);
        sb.append("\n" + tabs + "Element: ");
        sb.append(this.element);
        sb.append("\n" + tabs + "Type: ");
        sb.append(this.type);
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Elements getElement() {
        return element;
    }

    public void setElement(Elements element) {
        this.element = element;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}


