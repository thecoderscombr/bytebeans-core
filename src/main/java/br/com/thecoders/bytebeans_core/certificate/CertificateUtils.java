/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.certificate;

import br.com.thecoders.bytebeans_core.core.ProcessExecutor;

import java.nio.file.Files;
import java.nio.file.Path;

public class CertificateUtils {

    public CertificateUtils() {
        throw new AssertionError("Not instantiable");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    public static void importCertificate(CertParts certParts, Path location, String p12FileName) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("openssl pkcs12 -export -out cert_parts.temp")
                .append(" ").append("-inkey").append(" ").append(certParts.privateKey())
                .append(" ").append("-certfile").append(" ").append(certParts.caBundleCrt())
                .append(" ").append("-in").append(" ").append(certParts.certificateCrt())
                .append(" ").append("-passout").append(" ").append("pass:").append(certParts.password());

        String[] commands = {
                isWindows() ? "cmd.exe" : "sh",
                isWindows() ? "/c" : "-c",
                stringBuilder.toString()};

        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.redirectErrorStream(true);
        builder.directory(location.toFile());

        Path tempFile = Path.of(String.valueOf(location), "cert_parts.temp");

        ProcessExecutor.execAndCheckFileCreation(builder, tempFile);

        stringBuilder.setLength(0);

        stringBuilder.append("keytool -v -importkeystore -srckeystore cert_parts.temp -srcstoretype PKCS12 -deststoretype PKCS12 -srcstorepass")
                .append(" ").append(certParts.password())
                .append(" ").append("-deststorepass").append(" ").append(certParts.password())
                .append(" ").append("-destkeystore").append(" ").append(p12FileName).append(".p12");

        commands[2] = stringBuilder.toString();

        builder.command(commands);

        Path path = Path.of(String.valueOf(location), p12FileName + ".p12");

        if (Files.exists(path)) {
            System.out.println("INFO: " + path + " already exists, p12 import was skipped.");
            return;
        }

        ProcessExecutor.execAndCheckFileCreation(builder, path);
    }


    public static void generate(SelfSignedCert ssc) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("keytool -keystore").append(" ").append(ssc.getKeyStoreName()).append(".")
                .append(ssc.getType().equalsIgnoreCase("pkcs12") ? "p12" : ssc.getType())
                .append(" ").append("-storepass").append(" ").append(ssc.getStorePass())
                .append(" ").append("-deststoretype").append(" ").append(ssc.getType())
                .append(" ").append("-genkeypair -keyalg RSA -validity 365 -alias").append(" ").append(ssc.getKeyStoreName())
                .append(" ").append("-dname").append(" \"CN=").append(ssc.getCommonName())
                .append(", ").append("OU=").append(ssc.getOrganizationalUnitName())
                .append(", ").append("O=").append(ssc.getOrganizationName())
                .append(", ").append("L=").append(ssc.getLocalityName())
                .append(", ").append("ST=").append(ssc.getStateOrProvinceName())
                .append(", ").append("C=").append(ssc.getCountryName()).append("\"")
                .append(" ").append("-ext").append(" ").append("\"SAN=DNS:www.").append(ssc.getIp())
                .append(",IP:").append(ssc.getIp()).append("\"");

        String[] commands = {
                isWindows() ? "cmd.exe" : "sh",
                isWindows() ? "/c" : "-c",
                stringBuilder.toString()};

        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.redirectErrorStream(true);
        builder.directory(ssc.getKeyStoreParentPath().toFile());

        Path path = Path.of(ssc.getKeyStoreParentPath().toString(), ssc.getKeyStoreName().concat(".")
                .concat(ssc.getType().equalsIgnoreCase("pkcs12") ? "p12" : ssc.getType()));

        if (Files.exists(path)) {
            System.out.println("INFO: " + path + " already exists, p12 creation was aborted.");
            return;
        }

        ProcessExecutor.execAndCheckFileCreation(builder, path);
    }

}
