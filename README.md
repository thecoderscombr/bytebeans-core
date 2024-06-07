1. If you want to create a self-signed certificate to use HTTPS, first generate a p12 file as your keystore within a self-signed certificate, use the following to do so.

```java
    public static void main(String[] args) {

        SelfSignedCert selfSignedCert = new SelfSignedCert.Builder()
                .keyStoreParentPath(Path.of("C:/Users/uname/Documents"))
                .storePass("change_it")
                .type("pkcs12")
                .keyStoreName("self_signed_cert")
                .countryName("BR")
                .organizationalUnitName("TC")
                .stateOrProvinceName("SP")
                .localityName("SP")
                .commonName("localhost")
                .ip("127.0.0.1")
                .organizationName("TC")
                .build();

        CertificateUtils.generate(selfSignedCert);
                
        ...
}
```
**OR**
- If you want to use a valid trusted signed certificate to use HTTPS, import the certificate in a p12 file as your keystore, use the following to do so.

```java
    public static void main(String[] args) {

        CertParts certParts = new CertParts(
                Path.of("C:/Users/uname/Documents/private.key"),
                Path.of("C:/Users/uname/Documents/ca_bundle.crt"),
                Path.of("C:/Users/uname/Documents/certificate.crt"),
                "change_it"
        );

        CertificateUtils.importCertificate(certParts, Path.of("C:/Users/uname/Documents"), "keystore");
        
        ...
}
```
2. Create the settings.yml file in resources folder if using maven or gradle or in project root while using a java project.

3. Create the folder that will store you website page files, in this example located in resources folder as well.

```yml
Server:
  Http:
    Root-Directory: C:/Users/uname/my-project/src/main/resources/my-website
    Hostname: localhost
    Port: 1234
  Https:
    Keystore:
      File: C:/Users/uname/Documents/self_signed_cert.p12
      Password: "change_it"
    Root-Directory: C:/Users/uname/my-project/src/main/resources/my-website
    Hostname: localhost
    Port: 4321
```

4. Use the following to start the server with a http, https or both protocols.

```java
    public static void main(String[] args) {
        ...
    
        ByteBeansCore byteBeansCore = new ByteBeansCore();
        byteBeansCore.start(ServerType.HTTP);
        byteBeansCore.start(ServerType.HTTPS);
    }
```

5. A webpages version of both protocols can be used by passing `ServerType.HTTP_PAGES` or `ServerType.HTTPS_PAGES`

```java
    public static void main(String[] args) {
        ...
    
        ByteBeansCore byteBeansCore = new ByteBeansCore();
        byteBeansCore.start(ServerType.HTTP_PAGES);
        byteBeansCore.start(ServerType.HTTPS_PAGES);
    }
```

note: that HTTP and HTTP_PAGES cannot be used at the same time, HTTPS and HTTPS_PAGES they cannot either.