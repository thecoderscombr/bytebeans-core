/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.core;

import com.sun.net.httpserver.HttpHandler;

public record HttpContextHandler(String path, HttpHandler httpHandler)  {
}
