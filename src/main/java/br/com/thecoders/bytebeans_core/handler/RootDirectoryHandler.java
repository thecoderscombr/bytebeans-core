/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.handler;

import br.com.thecoders.bytebeans_core.core.HttpPages;
import br.com.thecoders.bytebeans_core.core.Responder;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class RootDirectoryHandler {
    private static int i = 0;
    private static String root = null;

    public static Map<String, File> getFiles(Map<String, File> files, Path path) {

        if (i == 0) root = path.toString();

        i++;

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            for (Path path1 : directoryStream) {
                if (Files.isDirectory(path1)) {
                    getFiles(files, path1);
                } else {
                    File file = path1.toFile();
                    if (file.getName().equalsIgnoreCase("settings.yml")) continue;
                    String context = file.getAbsolutePath().replace(root, "");
                    files.put(context, path1.toFile());
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return files;
    }


    public HttpHandler httpHandler(File file) {

        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);

        return exchange -> {

            switch (extension) {
                case "js" -> exchange.getResponseHeaders().set("Content-Type", "text/javascript");
                case "html", "htm" -> exchange.getResponseHeaders().set("Content-Type", "text/html");
                case "css" -> exchange.getResponseHeaders().set("Content-Type", "text/css");
            }

            String uri = exchange.getRequestURI().getPath();

            if (!uri.equals("/") && !HttpPages.control.contains(uri)) {
                Responder.reply(exchange, 404, "404 - NOT FOUND");
                return;
            }

            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = inputStream.readAllBytes();
            Responder.reply(exchange, 200, bytes);
            inputStream.close();
        };
    }

}
