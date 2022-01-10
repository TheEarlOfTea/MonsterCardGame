package com.company.auxilliary;

public class Token {
    private String token;

    public Token(String token){
        this.token=token;
    }
    public Token(){
        this.token="";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
