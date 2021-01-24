package com.doroshenko.serhey.lib.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConstructorBinding
@ConfigurationProperties("core")
public class CoreProperties {

    @NestedConfigurationProperty
    private final JksProperties jks;
    @NestedConfigurationProperty
    private final CertificateProperties certificate;

    public CoreProperties(@DefaultValue final JksProperties jks,
                          @DefaultValue final CertificateProperties certificate) {
        this.jks = jks;
        this.certificate = certificate;
    }

    /* Getters */
    public JksProperties getJks() {
        return jks;
    }

    public CertificateProperties getCertificate() {
        return certificate;
    }

}
