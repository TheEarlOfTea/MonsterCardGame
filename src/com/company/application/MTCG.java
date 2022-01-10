package com.company.application;

import com.company.auxilliary.*;
import com.company.auxilliary.enumUtils.StringToEnumConverter;
import com.company.auxilliary.scoreBoardUtils.ScoreBoard;
import com.company.stackTools.cards.BaseCard;
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


    /**
     * deserializes a json into a user
     * @param json json to decode
     * @return returns a User
     * @throws JsonProcessingException
     */
    private User decodeJSONTOUser(String json) throws JsonProcessingException{
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        User user= new User();
        JsonNode node=mapper.readTree(json);
        user.setUsername(node.get("Username").toString().replace("\"", ""));
        user.setPassword(node.get("Password").toString().replace("\"", ""));
        return user;
    }

    /**
     * deserializes a json into a stack
     * @param json json to decode
     * @return returns a Stack
     * @throws JsonProcessingException
     */
    private Stack decodeJsonToStackJson(String json)throws JsonProcessingException{
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        JsonNode node=mapper.readTree(json);
        Stack s= new Stack(node.get(0).toString().replace("\"", "").toLowerCase());
        BaseCard b;
        for(int i=1; i<node.size(); i++){
            b= decodeJSONToCard(node.get(i));
            s.addCards(b);
        }
        return s;
    }

    /**
     * serializes an object into a json-string
     * @param o object to serialize
     * @return returns a json-string
     * @throws JsonProcessingException
     */
    private String serializeObjectToJSON(Object o) throws JsonProcessingException{
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        return mapper.writeValueAsString(o);
    }

    /**
     * deserializes a json-string into a BaseCard
     * @param node node to be deserialized
     * @return a BaseCard
     */
    private BaseCard decodeJSONToCard(JsonNode node){
        BaseCard b= new BaseCard();

        b.setUid(node.get("Id").toString().replace("\"", ""));
        b.setName(node.get("Name").toString().replace("\"", ""));
        b.setPower(Integer.parseInt(node.get("Damage").toString()));
        b.setElement(StringToEnumConverter.getElement(node.get("Element").toString().replace("\"", "")));
        b.setType(StringToEnumConverter.getCardType(node.get("Type").toString().replace("\"", "")));

        return b;
    }

    /**
     * decodes a json-string into a DeckList and changes its owner to the given username
     * @param json json-string to deserialize
     * @param username new owner of the DeckList
     * @return returns a DeckList
     * @throws JsonProcessingException
     */
    private DeckList decodeJSONToDeckList(String json, String username) throws JsonProcessingException{
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        DeckList l= new DeckList(username);
        JsonNode node= mapper.readTree(json);
        for(int i=0; i< node.size(); i++){
            l.addUID(node.get(i).toString().replace("\"", ""));
        }
        return l;
    }

    /**
     * deserializes a json-string into a Profile
     * @param json json-string to decode
     * @return  a Profile
     * @throws JsonProcessingException
     */
    private Profile decodeJSONToProfile(String json) throws JsonProcessingException {
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        JsonNode node= mapper.readTree(json);
        Profile profile= new Profile();
        profile.setName(node.get("Name").toString().replace("\"", ""));
        profile.setBio(node.get("Bio").toString().replace("\"", ""));
        profile.setImage(node.get("Image").toString().replace("\"", ""));
        return profile;
    }

    /**
     * chooses based on given Route which interaction with the DataBase to conduct
     * @param request given Request containing Route and Method
     * @return a Response to be sent to the client
     */
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

    /**
     * creates User in the db
     * @param request containing user data as json-string
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
    public Response createUser(Request request, DataBaseConnector db){
        User user;
        Response response= new Response();
        if(request.getMethod().compareTo("POST")!=0){
            return get501Response();
        }
        try{
            user= decodeJSONTOUser(request.getContent());
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

    /**
     * creates a new Package in the DataBase
     * @param request containing package data as json-string
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
    public Response createPackage(Request request, DataBaseConnector db){
        Stack s;
        Response response= new Response();
        if(request.getMethod().compareTo("POST")!=0){
            return get501Response();
        }
        try{
            s= decodeJsonToStackJson(request.getContent());
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

    /**
     * trys to retrieve a token from the Database
     * @param request containing login data as json-string
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
    public Response login(Request request, DataBaseConnector db){
        User user;
        Token token;
        Response response= new Response();
        if(request.getMethod().compareTo("POST")!=0){
            return get501Response();
        }
        try{
            user= decodeJSONTOUser(request.getContent());
            token=db.tryLogin(user);
            if(token.getToken().compareTo("NO_TOKEN")!=0){
                response.setContent(serializeObjectToJSON(token));
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

    /**
     * aquires a pck for the given client
     * @param username client which tries to acquire pack
     * @param request containing the name of the pack
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
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
            response.setContent(serializeObjectToJSON(s));
            response.setContentType(ContentType.JSON);
            return response;
        }catch (Exception e){
            printException(e);
            return get500Response();
        }
    }

    /**
     * tries to access a deck of a given user
     * @param username user whose collection
     * @param request a request containng Method and a Login-Token
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
    public Response accessDeck(String username, Request request, DataBaseConnector db){
        switch (request.getMethod()){
            case "GET":
                return getDeck(username, db);
            case "PUT":
                try{
                    DeckList l= decodeJSONToDeckList(request.getContent(), username);
                    return configureDeck(l, db);
                }catch (JsonProcessingException e){
                    return get500Response();
                }
        }
        return get501Response();
    }

    /**
     * retrieves deck of given user from the Database
     * @param username user whose deck is to be retrieved
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
    public Response getDeck(String username, DataBaseConnector db){
        Response response= new Response();
        try{
            Stack s= db.getDeckFromTable(username, "-d");
            if(s.getDeck().size()==0){
                response.setContent("<!DOCTYPE html><html><body><h1>Deck of " + s.getOwner() + " is Empty</h1></body></html>");
                return response;
            }
            response.setContent(serializeObjectToJSON(s));
            return response;
        }catch (Exception e){
            printException(e);
            return get500Response();
        }
    }

    /**
     * changes deck on the Server
     * @param l DeckList which contains the new deck list
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
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

    /**
     * retrieves a simpler version of the Deck from the server
     * @param username user whose DeckList is to be rtrieved
     * @param request a request containing a Method and a Login-Token
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
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
            String json= serializeObjectToJSON(l);
            response.setContent(json);
            response.setContentType(ContentType.JSON);
        }catch (Exception e){
            printException(e);
            return get500Response();
        }

        return response;
    }


    /**
     * tries to access a users profile
     * @param username the user of which the profile is to be accessed
     * @param request request containing method of access and Login-Token
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
    public Response accessProfile(String username, Request request, DataBaseConnector db){
        switch (request.getMethod()){
            case "PUT":
                try{
                    return setProfile(username, decodeJSONToProfile(request.getContent()), db);
                }catch (JsonProcessingException e){
                    printException(e);
                    return get400Response();
                }

            case "GET":
                return getProfile(username, db);
        }

        return get501Response();
    }

    /**
     * changes the profile of a user
     * @param username user whose profile gets changed
     * @param profile Profile containing the changes to conduct
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
    public Response setProfile(String username, Profile profile, DataBaseConnector db){
        Response response=new Response();
        if(db.changeProfile(username, profile)){
            response.setContent("<!DOCTYPE html><html><body><h1>Profile successfully changed</h1></body></html>");
            return response;
        }
        return get400Response();
    }

    /**
     * retrieves a full Profile of the user form the DataBase
     * @param username user which profile is to be retrieved
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
    public Response getProfile(String username, DataBaseConnector db){
        Response response=new Response();
        Profile profile= db.getProfile(username);
        if(profile==null){
            return get400Response();
        }
        try{
            response.setContent(serializeObjectToJSON(profile));
            response.setContentType(ContentType.JSON);
            return response;
        }catch (JsonProcessingException e){
            printException(e);
        }
        return get500Response();
    }

    /**
     * retrieves the Scoreboard from the server
     * @param db DataBaseConnector, which handles all Database interactions
     * @return a Response to be sent to the client
     */
    public Response getScoreBoard(DataBaseConnector db){
        Response response= new Response();
        try{
            ScoreBoard sb= db.getScoreBoard();
            response.setContent(serializeObjectToJSON(sb));
            response.setContentType(ContentType.JSON);
            return response;
        }catch (Exception e) {
            printException(e);

        }
        return get500Response();
    }


    /**
     * prints an exception
     * @param e exception to be printed
     */
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
