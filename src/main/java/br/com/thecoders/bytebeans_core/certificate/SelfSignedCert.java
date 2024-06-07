/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.certificate;

import java.nio.file.Path;

public class SelfSignedCert {
    private final Path keyStoreParentPath;
    private final String keyStoreName;
    private final String type;
    private final String storePass;
    private final String commonName;
    private final String organizationalUnitName;
    private final String organizationName;
    private final String localityName;
    private final String stateOrProvinceName;
    private final String countryName;
    private final String ip;

    public SelfSignedCert(Builder builder) {
        this.keyStoreParentPath = builder.keyStoreParentPath;
        this.keyStoreName = builder.keyStoreName;
        this.type = builder.type;
        this.storePass = builder.storePass;
        this.commonName = builder.commonName;
        this.organizationalUnitName = builder.organizationalUnitName;
        this.organizationName = builder.organizationName;
        this.localityName = builder.localityName;
        this.stateOrProvinceName = builder.stateOrProvinceName;
        this.countryName = builder.countryName;
        this.ip = builder.ip;
    }


    public Path getKeyStoreParentPath() {
        return keyStoreParentPath;
    }

    public String getKeyStoreName() {
        return keyStoreName;
    }

    public String getType() {
        return type;
    }

    public String getStorePass() {
        return storePass;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getOrganizationalUnitName() {
        return organizationalUnitName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public String getStateOrProvinceName() {
        return stateOrProvinceName;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getIp() {
        return ip;
    }

    public static class Builder {
        private Path keyStoreParentPath;
        private String keyStoreName;
        private String type;
        private String storePass;
        private String commonName;
        private String organizationalUnitName;
        private String organizationName;
        private String localityName;
        private String stateOrProvinceName;
        private String countryName;
        private String ip;


        public Builder keyStoreParentPath(Path keyStoreParentPath) {
            this.keyStoreParentPath = keyStoreParentPath;
            return this;
        }

        public Builder keyStoreName(String keyStoreName) {
            this.keyStoreName = keyStoreName;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder storePass(String storePass) {
            this.storePass = storePass;
            return this;
        }

        public Builder commonName(String commonName) {
            this.commonName = commonName;
            return this;
        }

        public Builder organizationalUnitName(String organizationalUnitName) {
            this.organizationalUnitName = organizationalUnitName;
            return this;
        }

        public Builder organizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public Builder localityName(String localityName) {
            this.localityName = localityName;
            return this;
        }

        public Builder stateOrProvinceName(String stateOrProvinceName) {
            this.stateOrProvinceName = stateOrProvinceName;
            return this;
        }

        public Builder countryName(String countryName) {
            this.countryName = countryName;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public SelfSignedCert build() {
            return new SelfSignedCert(this);
        }
    }
}
