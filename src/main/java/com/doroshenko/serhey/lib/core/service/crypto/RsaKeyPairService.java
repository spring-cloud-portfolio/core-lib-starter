package com.doroshenko.serhey.lib.core.service.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * Implementation of {@link KeyPairService} based on RSA key pair
 *
 * @author Serhey Doroshenko
 * @see KeyPairService
 */
public class RsaKeyPairService implements KeyPairService {

    public static final int KEY_SIZE = 2048;
    public static final String RSA_ALGORITHM = "RSA";

    private final JksService jksService;
    private final CertificateService certificateService;

    private KeyPair keyPair;
    private Certificate certificate;

    public RsaKeyPairService(final JksService jksService,
                             final CertificateService certificateService) {
        this.jksService = jksService;
        this.certificateService = certificateService;
    }

    @Override
    public KeyPair lodKeyPair() {
        return new KeyPair(loadPublicKey(), loadPrivateKey());
    }

    @Override
    public PublicKey loadPublicKey() {
        return jksService.loadOrStorePublicKey(this::getCertificate);
    }

    @Override
    public PrivateKey loadPrivateKey() {
        return jksService.loadOrStorePrivateKey(this::getPrivateKey, this::getCertificate);
    }

    /* Private methods */
    private KeyPair getKeyPair() {
        if (keyPair == null) keyPair = generateRsaKeyPair();
        return keyPair;
    }

    private PrivateKey getPrivateKey() {
        return getKeyPair().getPrivate();
    }

    private KeyPair generateRsaKeyPair() {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private Certificate getCertificate() {
        if (certificate == null) certificate = certificateService.generateCertificate(getKeyPair());
        return certificate;
    }

}
