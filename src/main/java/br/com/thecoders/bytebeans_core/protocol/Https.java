/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.protocol;

import br.com.thecoders.bytebeans_core.core.HttpContextHolder;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Https extends BaseHttp {

    private HttpsServer httpsServer;

    private SSLContext sslContext;

    @Override
    public HttpServer getServer() {
        return httpsServer;
    }

    public KeyStore loadKeyStore(String type, File keyStoreFile, String storePass) {
        try {
            KeyStore keyStore = KeyStore.getInstance(type);
            if (keyStoreFile.exists()) {
                keyStore.load(new FileInputStream(keyStoreFile), storePass.toCharArray());
            } else throw new FileNotFoundException(keyStoreFile.getAbsolutePath() + " does not exist");
            return keyStore;
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSslContext(KeyStore keyStore, String storePass) {
        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, storePass.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(String hostname, int port) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
        try {
            httpsServer = HttpsServer.create(inetSocketAddress, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        httpsServer.setExecutor(executorService);

        for (HttpContextHolder httpContext : getFileHttpContexts()) {
            httpsServer.createContext(httpContext.path(), httpContext.httpHandler());
        }

        for (HttpContextHolder httpContext : getApiHttpContexts()) {
            httpsServer.createContext(httpContext.path(), httpContext.httpHandler());
        }

        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext));
        httpsServer.start();
        System.out.println("INFO: https Server is up at port: " + port);
    }

    @Override
    public void stop() {
        if (httpsServer != null) {
            httpsServer.stop(0);
        }
    }
}
