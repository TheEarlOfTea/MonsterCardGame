package com.company.application;

import com.company.auxilliary.*;
import com.company.auxilliary.enumUtils.StringToEnumConverter;
import com.company.auxilliary.scoreBoardUtils.ScoreBoard;
import com.company.cards.BaseCard;
import com.company.dataBaseTools.DataBaseConnector;
import com.company.dataBaseTools.TableNames;
import com.company.server.http.ContentType;
import com.company.stackTools.DeckList;
import com.company.stackTools.Stack;
import com.company.server.Request;
import com.company.server.Response;
import com.company.server.ServerApplication;
import com.company.server.http.HttpStatus;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.sql.SQLException;
import java.util.LinkedList;

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

        b.setUid(node.get("Id").toString().replace("\"", ""));
        b.setName(node.get("Name").toString().replace("\"", ""));
        b.setPower(Integer.parseInt(node.get("Damage").toString()));
        b.setElement(StringToEnumConverter.getElement(node.get("Element").toString().replace("\"", "")));
        b.setType(StringToEnumConverter.getCardType(node.get("Type").toString().replace("\"", "")));

        return b;
    }
    private DeckList decodeListJson(String json, String username) throws JsonProcessingException{
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        DeckList l= new DeckList(username);
        JsonNode node= mapper.readTree(json);
        for(int i=0; i< node.size(); i++){
            l.addUID(node.get(i).toString().replace("\"", ""));
        }
        return l;
    }

    private Profile decodeProfileJson(String json) throws JsonProcessingException {
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        JsonNode node= mapper.readTree(json);
        Profile profile= new Profile();
        profile.setName(node.get("Name").toString().replace("\"", ""));
        profile.setBio(node.get("Bio").toString().replace("\"", ""));
        profile.setImage(node.get("Image").toString().replace("\"", ""));
        return profile;
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
                    return acquirePack(token.getUsername(), request, db);
                case "/cards":
                    return getCollection(token.getUsername(), request, db);
                case "/deck":
                    return accessDeck(token.getUsername(), request, db);
                case "/deck?format=plain":
                    return getPlainDeck(token.getUsername(), request, db);
                case "/score":
                    return getScoreBoard(db);
            }
            if(request.getRoute().length()>7){
                if(request.getRoute().substring(0, 7).compareTo("/users/")==0){
                    if(token.getUsername().compareTo(request.getRoute().substring(7))==0){
                       return accessProfile(request.getRoute().substring(7), request, db);
                    }
                    return get404Response();
                }
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

    public Response acquirePack(String username, Request request, DataBaseConnector db){
        if(request.getMethod().compareTo("POST")!=0){
            return get501Response();
        }
        String packName= request.getContent();
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
    public Response getCollection(String username, Request request, DataBaseConnector db){
        if(request.getMethod().compareTo("GET")!=0){
            return get501Response();
        }
        Response response= new Response();
        try{
            Stack s= db.getDeckFromTable(username, "-u");
            response.setContent(serializeObject(s));
            response.setContentType(ContentType.JSON);
            return response;
        }catch (Exception e){
            printException(e);
            return get500Response();
        }
    }

    public Response accessDeck(String username, Request request, DataBaseConnector db){
        switch (request.getMethod()){
            case "GET":
                return getDeck(username, db);
            case "PUT":
                try{
                    DeckList l=decodeListJson(request.getContent(), username);
                    return configureDeck(l, db);
                }catch (JsonProcessingException e){
                    return get500Response();
                }
        }
        return get501Response();
    }

    public Response getDeck(String username, DataBaseConnector db){
        Response response= new Response();
        try{
            Stack s= db.getDeckFromTable(username, "-d");
            if(s.getDeck().size()==0){
                response.setContent("<!DOCTYPE html><html><body><h1>Deck of " + s.getOwner() + " is Empty</h1></body></html>");
                return response;
            }
            response.setContent(serializeObject(s));
            return response;
        }catch (Exception e){
            printException(e);
            return get500Response();
        }
    }

    public Response configureDeck(DeckList l, DataBaseConnector db){
        Response response= new Response();
            if(l.getList().size()!=4){
                return get400Response();
            }
            try{
                db.addListToDeck(l.getOWNER(), l);
            }catch (SQLException e){
                printException(e);
                return get400Response();
            }
        response.setContent("<!DOCTYPE html><html><body><h1>Deck of " + l.getOWNER() + " successfully changed</h1></body></html>");
        return response;
    }

    public Response getPlainDeck(String username, Request request, DataBaseConnector db){
        if(request.getMethod().compareTo("GET")!=0){
            return get501Response();
        }
        Response response= new Response();
        try{
            Stack s= db.getDeckFromTable(username, "-d");
            LinkedList<String> l= new LinkedList<>();
            for(BaseCard b: s.getDeck()){
                l.add(b.getName());
            }
            String json= serializeObject(l);
            response.setContent(json);
            response.setContentType(ContentType.JSON);
        }catch (Exception e){
            printException(e);
            return get500Response();
        }

        return response;
    }

    public Response accessProfile(String username, Request request, DataBaseConnector db){
        Response response= new Response();
        switch (request.getMethod()){
            case "PUT":
                try{
                    return setProfile(username, decodeProfileJson(request.getContent()), db);
                }catch (JsonProcessingException e){
                    printException(e);
                    return get400Response();
                }

            case "GET":
                return getProfile(username, db);
        }

        return get501Response();
    }

    public Response setProfile(String username, Profile profile, DataBaseConnector db){
        Response response=new Response();
        if(db.changeProfile(username, profile)){
            response.setContent("<!DOCTYPE html><html><body><h1>Profile successfully changed</h1></body></html>");
            return response;
        }
        return get400Response();
    }
    public Response getProfile(String username, DataBaseConnector db){
        Response response=new Response();
        Profile profile= db.getProfile(username);
        if(profile==null){
            return get400Response();
        }
        try{
            response.setContent(serializeObject(profile));
            response.setContentType(ContentType.JSON);
            return response;
        }catch (JsonProcessingException e){
            printException(e);
        }
        return get500Response();
    }

    public Response getScoreBoard(DataBaseConnector db){
        Response response= new Response();
        try{
            ScoreBoard sb= db.getScoreBoard();
            response.setContent(serializeObject(sb));
            response.setContentType(ContentType.JSON);
            return response;
        }catch (Exception e) {
            printException(e);

        }
        return get500Response();
    }




    public void printException(Exception e){
        //e.printStackTrace();
        System.out.println(e.getMessage());
    }
    public Response get400Response(){
        Response response= new Response();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setContent("<!DOCTYPE html><html><body><h1>ERROR 400 || Bad request - sent illegal JSON</h1></body></html>");
        return response;
    }
    public Response get404Response(){
        Response response= new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setContent("<!DOCTYPE html><html><body><h1>ERROR 404 || Site not found</h1></body></html>");
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
