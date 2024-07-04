/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.core;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Responder {

    public Responder() {
        throw new AssertionError("Not instantiable");
    }

    public static void reply(HttpExchange exchange, int status, final byte[] bytes) {

        try {
            exchange.sendResponseHeaders(status, bytes.length);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(bytes);
            responseBody.flush();
            responseBody.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void reply(HttpExchange exchange, int status, String response) {
        reply(exchange, status, response.getBytes());
    }
}
