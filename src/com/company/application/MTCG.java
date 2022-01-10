package com.company.application;

import com.company.auxilliary.StringToEnumConverter;
import com.company.auxilliary.Token;
import com.company.cards.BaseCard;
import com.company.dataBaseTools.DataBaseConnector;
import com.company.auxilliary.User;
import com.company.dataBaseTools.TableNames;
import com.company.server.http.ContentType;
import com.company.stackTools.Stack;
import com.company.server.Request;
import com.company.server.Response;
import com.company.server.ServerApplication;
import com.company.server.http.HttpStatus;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.sql.SQLException;

public class MTCG implements ServerApplication {

    @Override
    public Response handleRequest(Request request) {
        return chooseTask(request);
    }

    private User decodeUserJSON(String json) throws JsonProcessingException{
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        User user= new User();
        JsonNode node=mapper.readTree(json);
        user.setUsername(node.get("Username").toString().replace("\"", ""));
        user.setPassword(node.get("Password").toString().replace("\"", ""));
        return user;
    }
    private Stack decodeDeckJson(String json)throws JsonProcessingException{
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        JsonNode node=mapper.readTree(json);
        Stack s= new Stack(node.get(0).toString().replace("\"", "").toLowerCase());
        BaseCard b;
        for(int i=1; i<node.size(); i++){
            b=decodeCardJson(node.get(i));
            s.addCards(b);
        }
        return s;
    }
    private String objectToJson(Object o) throws JsonProcessingException{
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        return mapper.writeValueAsString(o);
    }

    private BaseCard decodeCardJson(JsonNode node){
        BaseCard b= new BaseCard();

        String type=node.get("Type").toString().replace("\"", "");
        String element=node.get("Element").toString().replace("\"", "");

        b.setUid(node.get("Id").toString());
        b.setName(node.get("Name").toString());
        b.setPower(Integer.parseInt(node.get("Damage").toString()));
        b.setElement(StringToEnumConverter.getElement(element));
        b.setType(StringToEnumConverter.getCardType(type));

        return b;
    }

    private String serializeObject(Object object) throws JsonProcessingException{
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        return mapper.writeValueAsString(object);
    }

    private Response chooseTask(Request request){
        DataBaseConnector db= new DataBaseConnector();
        try{
            db.connect();
        }catch (SQLException e){
            printException(e);
            return get500Response();
        }

        switch(request.getRoute()){
            case "/users":
                return createUser(request,db);
            case "/sessions":
                return login(request,db);
        }
        Token token= db.checkToken(request.getAuthorization());
        if(db.checkAdminToken(token.getToken())){
            switch(request.getRoute()){
                case "/packages":
                    return createPackage(request, db);
            }
        }
        if(!token.getToken().isEmpty()){
            switch (request.getRoute()){
                case "/transactions/packages":
                    return acquirePack(token.getUsername(), request.getContent(), db);
                case"/cards":
                    return getCollection(token.getUsername(), db);
            }
        }
        try{
            db.disconnect();
        }catch (SQLException e){
            printException(e);
            return get500Response();
        }
        return get501Response();


    }

    public Response createUser(Request request, DataBaseConnector db){
        User user;
        Response response= new Response();
        if(request.getMethod().compareTo("POST")!=0){
            return get501Response();
        }
        try{
            user=decodeUserJSON(request.getContent());
            db.createUser(user);
            response.setStatus(HttpStatus.OK);
            response.setContent("<!DOCTYPE html><html><body><h1>User Sucessfully Created</h1></body></html>");
        }catch (JsonProcessingException e){
            printException(e);
            return get500Response();
        }catch (SQLException e){
            printException(e);
            response.setStatus(HttpStatus.SQL_ERROR);
            response.setContent("<!DOCTYPE html><html><body><h1>ERROR 900 || User already exists</h1></body></html>");
        }
        return response;
    }

    public Response createPackage(Request request, DataBaseConnector db){
        Stack s;
        Response response= new Response();
        if(request.getMethod().compareTo("POST")!=0){
            return get501Response();
        }
        try{
            s=decodeDeckJson(request.getContent());
            db.createPackageTable(s);
        }catch (JsonProcessingException e){
            printException(e);
            return get500Response();
        }catch (SQLException e){
            printException(e);
            response.setStatus(HttpStatus.SQL_ERROR);
            response.setContent("<!DOCTYPE html><html><body><h1>ERROR 900 || Package already Exists</h1></body></html>");
            return response;
        }
        response.setContent("<!DOCTYPE html><html><body><h1>Packs successfully created</h1></body></html>");
        return response;
    }

    public Response login(Request request, DataBaseConnector db){
        User user;
        Token token;
        Response response= new Response();
        if(request.getMethod().compareTo("POST")!=0){
            return get501Response();
        }
        try{
            user=decodeUserJSON(request.getContent());
            token=db.tryLogin(user);
            if(token.getToken().compareTo("NO_TOKEN")!=0){
                response.setContent(objectToJson(token));
                response.setContentType(ContentType.JSON);
            }
            else{
                response.setStatus(HttpStatus.BAD_LOGIN);
                response.setContent("<!DOCTYPE html><html><body><h1>No match for combination of username and password found</h1></body></html>");
            }
        }catch (JsonProcessingException e){
            printException(e);
            return get400Response();
        }catch (SQLException e){
            printException(e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setContent("<!DOCTYPE html><html><body><h1>ERROR 500 || Login Error</h1></body></html>");
        }
        return response;
    }

    public Response acquirePack(String username, String packName, DataBaseConnector db){
        Response response= new Response();
        response.setContent("<!DOCTYPE html><html><body><h1>Pack successfully acquired</h1></body></html>");
        try{
            if(db.getCoins(username)<5){
                response.setContent("<!DOCTYPE html><html><body><h1>User has not enough coins</h1></body></html>");
                return response;
            }
        }catch (SQLException e){
            printException(e);
            return get500Response();
        }

        try{
            if(db.checkForValue("name", TableNames.getPackageListTableName(), packName.replace("\"", ""))){
                Stack s= db.getDeckFromTable(packName.replace("\"", ""), "-p");
                db.changeCoins(username, -5);
                for(BaseCard b: s.getDeck()){
                    db.addCardToUserTable(b.getUid(), username);
                }
                return response;
            }
            response.setContent("<!DOCTYPE html><html><body><h1>ERROR 500 || Pack konnte nicht gefunden werden</h1></body></html>");
        }catch (SQLException e){
            printException(e);
            return get500Response();
        }
        return response;
    }
    public Response getCollection(String username, DataBaseConnector db){
        Response response= new Response();
        try{
            Stack s= db.getDeckFromTable(username, "-u");
            response.setContent(serializeObject(s));
            return response;
        }catch (Exception e){
            printException(e);
            return get500Response();
        }
    }


    public void printException(Exception e){
        //e.printStackTrace();
        System.out.println(e.getMessage());
    }
    public Response get400Response(){
        Response response= new Response();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setContent("<!DOCTYPE html><html><body><h1>ERROR 400 || Bad request - JSON was not parseable</h1></body></html>");
        return response;
    }
    public Response get500Response(){
        Response response= new Response();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setContent("<!DOCTYPE html><html><body><h1>ERROR 500 || Internal Server Error</h1></body></html>");
        return response;
    }
    public Response get501Response(){
        Response response=new Response();
        response.setStatus(HttpStatus.NOT_IMPLEMENTED);
        response.setContent("<!DOCTYPE html><html><body><h1>ERROR 501 || Not Implemented</h1></body></html>");
        return response;
    }
}
