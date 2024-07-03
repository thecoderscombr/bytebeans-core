/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.core;

import com.sun.net.httpserver.HttpHandler;

import java.util.Objects;

public record HttpContextHolder(String path, HttpHandler httpHandler)  {



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpContextHolder that)) return false;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }
}
