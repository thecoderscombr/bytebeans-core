/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.protocol;

import br.com.thecoders.bytebeans_core.core.HttpContextHolder;
import com.sun.net.httpserver.HttpServer;

import java.util.concurrent.CopyOnWriteArraySet;

public abstract class BaseHttp {

    public abstract void start(String hostname, int port);

    public abstract void stop();

    public abstract HttpServer getServer();

    public final CopyOnWriteArraySet<HttpContextHolder> fileHttpContexts = new CopyOnWriteArraySet<>();

    public CopyOnWriteArraySet<HttpContextHolder> getFileHttpContexts() {
        return fileHttpContexts;
    }


    public final CopyOnWriteArraySet<HttpContextHolder> apiHttpContexts = new CopyOnWriteArraySet<>();

    public CopyOnWriteArraySet<HttpContextHolder> getApiHttpContexts() {
        return apiHttpContexts;
    }

}
