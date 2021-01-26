package com.doroshenko.serhey.lib.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConstructorBinding
@ConfigurationProperties("core.jks")
public class JksProperties {

    private final String password;
    private final String fileName;
    private final String directoryPath;
    private final String privateKeyAlias;
    private final String certificateAlias;

    public JksProperties(@DefaultValue("secret") final String password,
                         @DefaultValue("default.p12") final String fileName,
                         @DefaultValue("certificate") final String directoryPath,
                         @DefaultValue("private-key-alias") final String privateKeyAlias,
                         @DefaultValue("certificate-alias") final String certificateAlias) {
        this.password = password;
        this.fileName = fileName;
        this.directoryPath = directoryPath;
        this.privateKeyAlias = privateKeyAlias;
        this.certificateAlias = certificateAlias;
    }

    /* Getters */
    public String getPassword() {
        return password;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public String getCertificateAlias() {
        return certificateAlias;
    }

    public String getPrivateKeyAlias() {
        return privateKeyAlias;
    }

}
