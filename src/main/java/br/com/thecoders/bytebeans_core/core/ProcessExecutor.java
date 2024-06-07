/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.core;

import br.com.thecoders.bytebeans_core.exception.ProcessException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProcessExecutor {

    public static void execAndCheckFileCreation(ProcessBuilder processBuilder, Path createdFile) {
        try {
            Process process = processBuilder.start();
            String error = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            process.getInputStream().close();
            if (Files.notExists(createdFile)) throw new ProcessException(error);
            else System.out.println(createdFile + " created successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
