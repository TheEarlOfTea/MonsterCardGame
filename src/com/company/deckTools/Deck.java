package com.company.deckTools;
import com.company.cards.BaseCard;
import java.util.LinkedList;

public class Deck {
    private LinkedList list;

    public Deck(BaseCard... cards) {
        this.list=new LinkedList();
        for (BaseCard card:cards){
            list.add(card);
        }
    }

    public int size(){
        return list.size();
    }

    //Eventuell ducktype für return
    public BaseCard getRandomCard(){
        int size = this.list.size();
        if(size==0){
            return null;
        }
        int index= ((int)(Math.random()*list.size()));
        return (BaseCard) list.remove(index);
    }

    public void addCards(BaseCard... cards){
        for(BaseCard card:cards){
            list.add(card);
        }
    }
    public String toString(){
        int size= list.size();
        StringBuilder sb= new StringBuilder();
        sb.append("\tAmount of Cards: " + size() + "\n");
        sb.append("\tCards{");
        for(int i=0; i<size; i++){
            sb.append("\n\t\tCard " + (i+1) + ":");
            sb.append("\n" + list.get(i));
            sb.append("\n\n");
        }
        sb.append("\t}\n");
        return sb.toString();

    }
}
