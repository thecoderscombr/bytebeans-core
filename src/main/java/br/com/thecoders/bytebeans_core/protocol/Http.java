/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.protocol;

import br.com.thecoders.bytebeans_core.core.HttpContextHolder;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Http extends BaseHttp {

    private HttpServer httpServer;

    @Override
    public HttpServer getServer() {
        return httpServer;
    }

    @Override
    public void start(String hostname, int port) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
        try {
            httpServer = HttpServer.create(inetSocketAddress, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        httpServer.setExecutor(executorService);

        for (HttpContextHolder httpContext : getFileHttpContexts()) {
            httpServer.createContext(httpContext.path(), httpContext.httpHandler());
        }

        for (HttpContextHolder httpContext : getApiHttpContexts()) {
            httpServer.createContext(httpContext.path(), httpContext.httpHandler());
        }

        httpServer.start();
        System.out.println("INFO: http Server is up at port: " + port);
    }

    @Override
    public void stop() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }
}
