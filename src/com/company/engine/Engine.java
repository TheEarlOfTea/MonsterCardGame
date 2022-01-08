package com.company.engine;

import com.company.cards.BaseCard;
import com.company.deckTools.Deck;

public class Engine {
    private Deck deckA;
    private Deck deckB;
    private int maxRounds;
    private final int AUTO_MAX_ROUNDS=5;

    public Engine(Deck deckA, Deck deckB, int maxRounds){
        this.deckA=deckA;
        this.deckB=deckB;
        this.maxRounds=maxRounds;
    }

    public Engine(Deck deckA, Deck deckB) {
        this.deckA = deckA;
        this.deckB = deckB;
        this.maxRounds=AUTO_MAX_ROUNDS;
    }

    public Result battle(){
        System.out.println("New Game!\n" +
                "\n" + deckA.getOwner() +"s Deck: \n" + deckA +
                "\n" + deckB.getOwner() +"s Deck: \n" + deckB);
        BaseCard cardA;
        BaseCard cardB;
        Result result;
        Result endResult=new Result();
        if(deckA.size()==0 || deckB.size()==0){
            System.out.println("Please enter two non-empty decks" +
                    "\nCards in Deck A: " + deckA.size() +
                    "\nCards in Deck B: " + deckB.size());
        }
        for(int i=0; i<maxRounds;i++){
            cardA=deckA.getRandomCard();
            cardB=deckB.getRandomCard();
            result= Judge.judgeFight(cardA,cardB, deckA.getOwner(), deckB.getOwner());
            if(result.isDraw()){
                deckA.addCards(cardA);
                deckB.addCards(cardB);
            }
            else if(result.getWinner()==deckA.getOwner()){
                deckA.addCards(cardA, cardB);
            }
            else if(result.getWinner()==deckB.getOwner()){
                deckB.addCards(cardA, cardB);
            }

            if (checkForDeckOut()){
                if(deckA.size()==0){
                    endResult.setWinner(deckB.getOwner());
                    endResult.setLoser(deckA.getOwner());
                }
                else if(deckB.size()==0){
                    endResult.setWinner(deckA.getOwner());
                    endResult.setLoser(deckB.getOwner());
                }
                return endResult;
            }
        }
        System.out.println("Game ended with a Draw!\n");
        endResult.setDraw(true);
        return endResult;
    }

    public boolean checkForDeckOut(){
        if(deckA.size()==0){
            System.out.println(""+ deckA.getOwner()+ " lost the Game due to Deckout!\n");
        }
        else if(deckB.size()==0){
            System.out.println(""+ deckB.getOwner()+ " lost the Game due to Deckout!\n");
        }
        else{
            return false;
        }
        return true;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public Deck getDeckA() {
        return deckA;
    }

    public Deck getDeckB() {
        return deckB;
    }

    public void setDeckA(Deck deckA) {
        this.deckA = deckA;
    }

    public void setDeckB(Deck deckB) {
        this.deckB = deckB;
    }
}
