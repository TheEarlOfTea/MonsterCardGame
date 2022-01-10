package com.company.engine;

import com.company.cards.BaseCard;
import com.company.stackTools.Stack;

public class Engine {
    private Stack stackA;
    private Stack stackB;
    private int maxRounds;
    private final int AUTO_MAX_ROUNDS=5;

    public Engine(Stack stackA, Stack stackB, int maxRounds){
        this.stackA = stackA;
        this.stackB = stackB;
        this.maxRounds=maxRounds;
    }

    public Engine(Stack stackA, Stack stackB) {
        this.stackA = stackA;
        this.stackB = stackB;
        this.maxRounds=AUTO_MAX_ROUNDS;
    }

    public Result battle(){
        System.out.println("New Game!\n" +
                "\n" + stackA.getOwner() +"s Deck: \n" + stackA +
                "\n" + stackB.getOwner() +"s Deck: \n" + stackB);
        BaseCard cardA;
        BaseCard cardB;
        Result result;
        Result endResult=new Result();
        if(stackA.size()==0 || stackB.size()==0){
            System.out.println("Please enter two non-empty decks" +
                    "\nCards in Deck A: " + stackA.size() +
                    "\nCards in Deck B: " + stackB.size());
        }
        for(int i=0; i<maxRounds;i++){
            cardA= stackA.getRandomCard();
            cardB= stackB.getRandomCard();
            result= Judge.judgeFight(cardA,cardB, stackA.getOwner(), stackB.getOwner());
            if(result.isDraw()){
                stackA.addCards(cardA);
                stackB.addCards(cardB);
            }
            else if(result.getWinner()== stackA.getOwner()){
                stackA.addCards(cardA, cardB);
            }
            else if(result.getWinner()== stackB.getOwner()){
                stackB.addCards(cardA, cardB);
            }

            if (checkForDeckOut()){
                if(stackA.size()==0){
                    endResult.setWinner(stackB.getOwner());
                    endResult.setLoser(stackA.getOwner());
                }
                else if(stackB.size()==0){
                    endResult.setWinner(stackA.getOwner());
                    endResult.setLoser(stackB.getOwner());
                }
                return endResult;
            }
        }
        System.out.println("Game ended with a Draw!\n");
        endResult.setDraw(true);
        return endResult;
    }

    public boolean checkForDeckOut(){
        if(stackA.size()==0){
            System.out.println(""+ stackA.getOwner()+ " lost the Game due to Deckout!\n");
        }
        else if(stackB.size()==0){
            System.out.println(""+ stackB.getOwner()+ " lost the Game due to Deckout!\n");
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

    public Stack getDeckA() {
        return stackA;
    }

    public Stack getDeckB() {
        return stackB;
    }

    public void setDeckA(Stack stackA) {
        this.stackA = stackA;
    }

    public void setDeckB(Stack stackB) {
        this.stackB = stackB;
    }
}
