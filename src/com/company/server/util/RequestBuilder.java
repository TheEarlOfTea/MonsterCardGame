package com.company.server.util;

import com.company.server.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestBuilder {

    public static Request build(BufferedReader in) throws IOException {
        StringBuilder builder = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null && !inputLine.equals("")) {
            builder.append(inputLine + "\n");
        }

        Request request = new Request();
        String header = builder.toString();

        parseHeader(request, header);

        if (0 == request.getContentLength()) {
            return request;
        }

        char[] content = new char[request.getContentLength()];
        int read = in.read(content, 0, content.length);

        request.setContent(new String(content));

        return request;
    }

    private static void parseHeader(Request request, String header) throws IOException {
        //System.out.println(header);

        parseMethodeAndRoute(request, header);
        // TODO parse parameters
        parseContentType(request, header);
        parseContentLength(request, header);
    }

    private static void parseMethodeAndRoute(Request request, String header) throws IOException {
        Pattern r = Pattern.compile("^([A-Z]+)\\s([^\\s]+)");
        Matcher m = r.matcher(header);

        if (!m.find()) {
            throw new IOException("No HTTP Request");
        }

        request.setMethod(m.group(1));
        request.setRoute(m.group(2));
    }

    private static void parseContentType(Request request, String header) {
        request.setContentType(getHttpHeader(header, "Content-Type"));
    }

    private static void parseContentLength(Request request, String header) {
        String contentLength = getHttpHeader(header, "Content-Length");

        if (null == contentLength) {
            return;
        }

        request.setContentLength(Integer.parseInt(contentLength));
    }

    private static String getHttpHeader(String header, String httpHeader) {
        Pattern r = Pattern.compile("^" + httpHeader + ":\\s(.+)", Pattern.MULTILINE);
        Matcher m = r.matcher(header);

        if (!m.find()) {
            return null;
        }

        return m.group(1);
    }
}
