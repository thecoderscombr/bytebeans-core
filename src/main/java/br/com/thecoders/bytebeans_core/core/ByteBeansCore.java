/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.core;

import br.com.thecoders.bytebeans_core.protocol.Http;
import br.com.thecoders.bytebeans_core.protocol.Https;
import br.com.thecoders.bytebeans_core.yml.BBYml;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ByteBeansCore {

    private static String hostnameHttp;
    private static String hostnameHttps;
    private static int portHttp;
    private static int portHttps;
    private static String rootDirHttp;
    private static String rootDirHttps;
    private static String keystorePath;
    private static String keystorePassword;

    private Http httpInstance;
    private Https httpsInstance;
    private HttpPages httpPagesInstance;
    private final Set<HttpContextHandler> httpContextHandler = new HashSet<>();

    private static void setSettings(File settings) {
        Map<String, Object> stringObjectMap = new BBYml().read(settings);
        hostnameHttp = (String) stringObjectMap.get("Server.Http.Hostname");
        hostnameHttps = (String) stringObjectMap.get("Server.Https.Hostname");
        portHttp = (int) stringObjectMap.get("Server.Http.Port");
        portHttps = (int) stringObjectMap.get("Server.Https.Port");
        rootDirHttp = (String) stringObjectMap.get("Server.Http.Root-Directory");
        rootDirHttps = (String) stringObjectMap.get("Server.Https.Root-Directory");
        keystorePath = (String) stringObjectMap.get("Server.Https.Keystore.File");
        keystorePassword = (String) stringObjectMap.get("Server.Https.Keystore.Password");
    }

    private Http getHttpInstance() {
        if (httpInstance == null) httpInstance = new Http();
        return httpInstance;
    }

    private Https getHttpsInstance() {
        if (httpsInstance == null) httpsInstance = new Https();
        return httpsInstance;
    }

    private HttpPages getHttpPagesInstance() {
        if (httpPagesInstance == null) httpPagesInstance = new HttpPages();
        return httpPagesInstance;
    }

    public void addHttpContext(String httpPath, HttpHandler httpHandler) {
        httpContextHandler.add(new HttpContextHandler(httpPath, httpHandler));
    }

    public void start(ServerType serverType) {
        switch (serverType) {
            case HTTP -> startHttp();
            case HTTP_PAGES -> startPagesHttp();
            case HTTPS -> startHttps();
            case HTTPS_PAGES -> startPagesHttps();
        }
    }

    private void startHttp() {
        startHttp(getSettingsPath().toFile());
    }

    private void startHttp(File settings) {
        setSettings(settings);
        for (HttpContextHandler contextHandler : httpContextHandler) {
            getHttpInstance().getHttpContexts().add(contextHandler);
        }
        getHttpInstance().start(hostnameHttp, portHttp);
    }

    private void startHttps() {
        startHttps(getSettingsPath().toFile());
    }

    private void startHttps(File settings) {
        setSettings(settings);
        KeyStore keyStore = getHttpsInstance().loadKeyStore("PKCS12", new File(keystorePath), keystorePassword);
        getHttpsInstance().setSslContext(keyStore, keystorePassword);
        for (HttpContextHandler contextHandler : httpContextHandler) {
            getHttpsInstance().getHttpContexts().add(contextHandler);
        }
        getHttpsInstance().start(hostnameHttps, portHttps);
    }

    private void startPagesHttp() {
        startPagesHttp(getSettingsPath().toFile());
    }

    private void startPagesHttp(File settings) {
        setSettings(settings);
        Path rootDir = Path.of(rootDirHttp);
        getHttpPagesInstance().startPages(hostnameHttp, portHttp, rootDir, getHttpInstance());
    }

    private void startPagesHttps() {
        startPagesHttps(getSettingsPath().toFile());
    }

    private void startPagesHttps(File settings) {
        setSettings(settings);
        KeyStore keyStore = getHttpsInstance().loadKeyStore("PKCS12", new File(keystorePath), keystorePassword);
        getHttpsInstance().setSslContext(keyStore, keystorePassword);
        for (HttpContextHandler contextHandler : httpContextHandler) {
            getHttpsInstance().getHttpContexts().add(contextHandler);
        }
        Path rootDir = Path.of(rootDirHttps);
        getHttpPagesInstance().startPages(hostnameHttps, portHttps, rootDir, getHttpsInstance());
    }

    private static Path getSettingsPath() {
        URL settingsUrl = ByteBeansCore.class.getClassLoader().getResource("settings.yml");
        try {
            if (settingsUrl == null) throw new FileNotFoundException("settings.yml file not found");
            return Path.of(settingsUrl.toURI());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
