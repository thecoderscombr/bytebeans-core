/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.core;

import br.com.thecoders.bytebeans_core.handler.RootDirectoryHandler;
import br.com.thecoders.bytebeans_core.protocol.BaseHttp;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpPages {

    private Path rootDirectory;
    private BaseHttp baseHttp;

    private final Map<String, File> pagesFiles = new HashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final RootDirectoryHandler rootDirectoryHandler = new RootDirectoryHandler();

    public void startPages(String hostname, int port, Path rootDirectory, BaseHttp baseHttp) {
        this.rootDirectory = rootDirectory;
        this.baseHttp = baseHttp;
        baseHttp.start(hostname, port);
        update(2L, TimeUnit.SECONDS);
    }

    private void updateContexts(CopyOnWriteArraySet<HttpContextHandler> httpContexts,
                                Map<String, File> pagesFiles,
                                HttpServer server,
                                Path rootDirectory) {


        for (HttpContextHandler httpContext : httpContexts) {
            String toBackSlash = httpContext.path().replace("/", "\\");
            if (!pagesFiles.containsKey(toBackSlash)) {
                httpContexts.remove(httpContext);
                server.removeContext(httpContext.path());
            }
        }

        pagesFiles.clear();

        for (Map.Entry<String, File> entry : RootDirectoryHandler.getFiles(pagesFiles, rootDirectory).entrySet()) {
            if (entry.getValue().toPath().toString().equalsIgnoreCase(rootDirectory + File.separator + "index.html")) {
                if (!httpContexts.stream().map(HttpContextHandler::path).toList().contains("/")) {
                    HttpContext context = server.createContext("/", rootDirectoryHandler.httpHandler(entry.getValue()));
                    httpContexts.add(new HttpContextHandler(context.getPath(), context.getHandler()));
                }
            }

            String toForwardSlash = entry.getKey().replace("\\", "/");
            if (!httpContexts.stream().map(HttpContextHandler::path).toList().contains(toForwardSlash)) {
                HttpContext context = server.createContext(toForwardSlash, rootDirectoryHandler.httpHandler(entry.getValue()));
                httpContexts.add(new HttpContextHandler(context.getPath(), context.getHandler()));
            }
        }

        if (httpContexts.stream().map(HttpContextHandler::path).toList().contains("/")) {
            server.removeContext("/");
        }

        try {

            HttpContextHandler httpContextHandler = new HttpContextHandler("/", rootDirectoryHandler
                    .httpHandler(new RootFileCreator().create(pagesFiles, rootDirectory)));

            server.createContext(httpContextHandler.path(), httpContextHandler.httpHandler());
            httpContexts.add(httpContextHandler);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(long period, TimeUnit timeUnit) {
        scheduledExecutorService.scheduleAtFixedRate(() ->
                        updateContexts(baseHttp.httpContexts, pagesFiles, baseHttp.getServer(), rootDirectory),
                0L, period, timeUnit);
    }
}
