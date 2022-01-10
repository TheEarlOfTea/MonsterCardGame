package com.company.engine;
import com.company.auxilliary.enumUtils.Elements;
import com.company.cards.BaseCard;
import com.company.auxilliary.enumUtils.CardType;

public class Judge {
    public static Result judgeFight(BaseCard a, BaseCard b, String playerA, String playerB){
        int[] elModifier;
        if(a.getType()== CardType.SPELL || b.getType()== CardType.SPELL) {
            Elements elementA = a.getElement();
            Elements elementB = b.getElement();
            elModifier = compareElements(elementA, elementB);
        }
        else{
            elModifier=new int[]{1,1};
        }
        return documentFight(a, b, playerA, playerB, elModifier);
    }

    private static int[] compareElements(Elements a, Elements b){
        //same elements have no effectivity modifier
        if (a==b) {
            return new int[]{1, 1};
        }
        switch(a) {
            case FIRE :
                switch (b){
                    case WATER:
                        return new int[]{0, 2};
                    case PLANT:
                        return new int[]{2, 0};
            }
            case WATER:
                switch (b) {
                    case PLANT:
                        return new int[]{0, 2};
                    case FIRE:
                        return new int[]{2, 0};
                }
            case PLANT:
                switch (b) {
                    case FIRE:
                        return new int[]{0, 2};
                    case WATER:
                        return new int[]{2, 0};
                }
        }
        //at least 1 element is of type NORMAL
        return new int[]{1, 1};
    }
    private static Result documentFight(BaseCard a, BaseCard b, String playerA, String playerB, int[] elementModifier){
        Result result=new Result();
        int damageA=a.getPower()*elementModifier[0];
        int damageB=b.getPower()*elementModifier[1];
        System.out.println("******** New Fight! ********");
        System.out.println(a.getName() + " VS " + b.getName());

        System.out.println("Power of " + a.getName() + ": " + damageA);
        System.out.println("Power of " + b.getName() + ": " + damageB);
        if(damageA == damageB){
            System.out.println("It's a draw!");
            result.setDraw(true);
        }
        else if(damageA>damageB){
            System.out.println("" + playerA +" won!");
            result.setWinner(playerA);
            result.setLoser(playerB);
        }
        else{
            System.out.println("" + playerB +" won!");
            result.setWinner(playerB);
            result.setLoser(playerA);
        }
        System.out.println("****************************\n");
        return result;
    }
}
