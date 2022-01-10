package com.company.stackTools;

import java.util.LinkedList;

public class DeckList {
    private final String OWNER;
    private LinkedList<String> list;

    public DeckList(String owner){
        this.OWNER=owner;
        this.list=new LinkedList<>();
    }

    public String getOWNER() {
        return OWNER;
    }

    public LinkedList<String> getList() {
        return list;
    }
    public void addUID(String uid){
        list.add(uid);
    }
}
