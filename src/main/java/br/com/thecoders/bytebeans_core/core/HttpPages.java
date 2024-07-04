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
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpPages {

    public static final Set<String> control = new HashSet<>();

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

    private void updateContexts(CopyOnWriteArraySet<HttpContextHolder> httpContextHolders,
                                Map<String, File> pagesFiles,
                                HttpServer server,
                                Path rootDirectory) {

        control.clear();
        control.addAll(httpContextHolders.stream().map(HttpContextHolder::path).toList());

        for (HttpContextHolder httpContext : httpContextHolders) {
            String toBackSlash = httpContext.path().replace("/", "\\");
            if (!pagesFiles.containsKey(toBackSlash)) {
                httpContextHolders.remove(httpContext);
                server.removeContext(httpContext.path());
            }
        }

        pagesFiles.clear();


        if (httpContextHolders.stream().map(HttpContextHolder::path).toList().contains("/")) {
            httpContextHolders.remove(new HttpContextHolder("/", exchange -> {
            }));
            server.removeContext("/");
        }

        for (Map.Entry<String, File> entry : RootDirectoryHandler.getFiles(pagesFiles, rootDirectory).entrySet()) {
            String toForwardSlash = entry.getKey().replace("\\", "/");

            String c = toForwardSlash.substring(0, toForwardSlash.lastIndexOf('/'));
            String last = toForwardSlash.substring(toForwardSlash.lastIndexOf('/'));

            if (last.equalsIgnoreCase("/index.html") && !toForwardSlash.equalsIgnoreCase("/index.html")) {
                if (!httpContextHolders.stream().map(HttpContextHolder::path).toList().contains(c)) {
                    HttpContext context = server.createContext(c, rootDirectoryHandler.httpHandler(entry.getValue()));
                    httpContextHolders.add(new HttpContextHolder(c, context.getHandler()));
                    control.add(c);
                }
            }

            if (!httpContextHolders.stream().map(HttpContextHolder::path).toList().contains(toForwardSlash)) {
                HttpContext context = server.createContext(toForwardSlash, rootDirectoryHandler.httpHandler(entry.getValue()));
                httpContextHolders.add(new HttpContextHolder(toForwardSlash, context.getHandler()));
                control.add(toForwardSlash);
            }


            if (entry.getValue().toPath().toString().equalsIgnoreCase(rootDirectory + File.separator + "index.html")) {
                if (!httpContextHolders.stream().map(HttpContextHolder::path).toList().contains("/")) {
                    HttpContext context = server.createContext("/", rootDirectoryHandler.httpHandler(entry.getValue()));
                    httpContextHolders.add(new HttpContextHolder(context.getPath(), context.getHandler()));
                }
            }

        }
    }

    public void update(long period, TimeUnit timeUnit) {
        scheduledExecutorService.scheduleAtFixedRate(() ->
                        updateContexts(baseHttp.fileHttpContexts, pagesFiles, baseHttp.getServer(), rootDirectory),
                0L, period, timeUnit);
    }
}
