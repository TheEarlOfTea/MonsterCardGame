package com.company.stackTools;
import com.company.cards.BaseCard;
import java.util.LinkedList;

public class Stack {
    private LinkedList<BaseCard> deck;
    private String OWNER;

    public Stack(String owner, BaseCard... cards) {
        this.deck =new LinkedList<BaseCard>();
        for (BaseCard card:cards){
            deck.add(card);
        }
        this.OWNER=owner;
    }
    public Stack(){
        this.OWNER=null;
        this.deck =new LinkedList<BaseCard>();
    }

    public LinkedList<BaseCard> getDeck() {
        return deck;
    }

    public Stack(String owner){
        this.deck =new LinkedList();
        this.OWNER=owner;
    }

    public int size(){
        return deck.size();
    }
    
    public BaseCard getRandomCard(){
        int size = this.deck.size();
        if(size==0){
            return null;
        }
        int index= ((int)(Math.random()* deck.size()));
        return deck.remove(index);
    }

    public void addCards(BaseCard... cards){
        for(BaseCard card:cards){
            deck.add(card);
        }
    }
    public String getOwner(){
        return this.OWNER;
    }
    public String toString(){
        int size= deck.size();
        StringBuilder sb= new StringBuilder();
        sb.append("\tOwner: " + this.OWNER + "\n");
        sb.append("\tAmount of Cards: " + size() + "\n");
        sb.append("\tCards{");
        for(int i=0; i<size; i++){
            sb.append("\n\t\tCard " + (i+1) + ":");
            sb.append("\n" + deck.get(i));
            sb.append("\n\n");
        }
        sb.append("\t}\n");
        return sb.toString();

    }
}
