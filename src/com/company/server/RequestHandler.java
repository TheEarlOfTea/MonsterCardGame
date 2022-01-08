package com.company.server;

import com.company.server.util.RequestBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private BufferedReader in;
    private PrintWriter out;

    private final Socket socket;
    private final ServerApplication application;

    public RequestHandler(Socket socket, ServerApplication application) {
        this.socket = socket;
        this.application = application;
    }

    @Override
    public void run() {
        try {
            Request request = getRequest();
            Response response = application.handleRequest(request);
            sendResponse(response);
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() + " Error: " + e.getMessage());
            //e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Request getRequest() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        return RequestBuilder.build(in);
    }

    public void sendResponse(Response response) throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);

        out.write(response.toString());
    }
}
