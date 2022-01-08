package com.company.application;

import com.company.server.Request;
import com.company.server.Response;
import com.company.server.ServerApplication;
import com.company.server.http.ContentType;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class MTCG implements ServerApplication {

    @Override
    public Response handleRequest(Request request) {
        String route= request.getRoute();
        String content= request.getContent();
        String type= request.getContentType();
        String method= request.getMethod();
        if(request.getContent()!=null){
            System.out.println(decodeJSON(request.getContent()));
        }



        Response response = new Response();
        response.setContent("{ " +
                "\"route\": \"" + request.getRoute() + "\", " +
                "\"method\": \"" + request.getMethod() + "\" " +
                "}");
        response.setContentType(ContentType.JSON);
        return response;
    }

    private String decodeJSON(String json){
        JsonFactory factory= new JsonFactory();
        ObjectMapper mapper= new ObjectMapper(factory);
        try{
            JsonNode node=mapper.readTree(json);
            return node.get(1).toString();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return "";
    }
}
