package com.company.application;

import com.company.server.Request;
import com.company.server.Response;
import com.company.server.ServerApplication;
import com.company.server.http.ContentType;

public class MonsterTradingCardGame implements ServerApplication {

    public Response handleRequest(Request request) {
        Response response = new Response();
        response.setContent("{ " +
                "\"route\": \"" + request.getRoute() + "\", " +
                "\"method\": \"" + request.getMethod() + "\" " +
                "}");
        response.setContentType(ContentType.JSON);
        return response;
    }
}
