/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class RootFileCreator {

    public File create(final Map<String, File> fileMap, Path rootDir) throws IOException {

        File serverRootFile = new File(rootDir.toFile(), "server-root.html");

        if (Files.exists(serverRootFile.toPath()))
            Files.delete(serverRootFile.toPath());


        if (Files.notExists(serverRootFile.toPath()))
            Files.createFile(serverRootFile.toPath());

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(serverRootFile, StandardCharsets.UTF_8));

        bufferedWriter.write("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Title</title>
                </head>
                <body>""");

        for (Map.Entry<String, File> stringFileEntry : fileMap.entrySet()) {
            bufferedWriter.write("""
                    <a href="%s">%s</a><br>
                    """.formatted(stringFileEntry.getKey().replaceAll("\\\\", "/"),
                    stringFileEntry.getKey().replaceAll("\\\\", "/")));
        }

        bufferedWriter.write("""
                </body>
                </html>""");

        bufferedWriter.flush();
        bufferedWriter.close();

        return serverRootFile;
    }
}
