package com.doroshenko.serhey.lib.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("core.certificate")
public class CertificateProperties {

    private final String issuer;
    private final String subject;
    /**
     * Certificate expiration days (default: 365)
     */
    private final int expiration;
    /**
     * JCA signature algorithm (default: SHA512withRSA)
     */
    private final String algorithm;
    private final String privateKeyAlias;
    private final String certificateAlias;

    public CertificateProperties(@DefaultValue("CN=CloudPortfolio") final String issuer,
                                 @DefaultValue("CN=SerheyDoroshenko") final String subject,
                                 @DefaultValue("365") final int expiration,
                                 @DefaultValue("SHA512withRSA") final String algorithm,
                                 @DefaultValue("private-key-alias") final String privateKeyAlias,
                                 @DefaultValue("certificate-alias") final String certificateAlias) {
        this.issuer = issuer;
        this.subject = subject;
        this.algorithm = algorithm;
        this.expiration = expiration;
        this.privateKeyAlias = privateKeyAlias;
        this.certificateAlias = certificateAlias;
    }

    /* Getters */
    public String getIssuer() {
        return issuer;
    }

    public String getSubject() {
        return subject;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getCertificateAlias() {
        return certificateAlias;
    }

    public String getPrivateKeyAlias() {
        return privateKeyAlias;
    }

}
