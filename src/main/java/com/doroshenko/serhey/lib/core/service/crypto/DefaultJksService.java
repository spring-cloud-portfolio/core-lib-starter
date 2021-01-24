package com.doroshenko.serhey.lib.core.service.crypto;

import com.doroshenko.serhey.lib.core.config.properties.JksProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.function.Supplier;

public class DefaultJksService implements JksService {

    private final Path filePath;
    private final char[] password;
    private final KeyStore keyStore;
    private final JksProperties properties;

    public DefaultJksService(final JksProperties properties) {
        this.properties = properties;
        this.keyStore = instantiateKeyStore();
        this.password = properties.getPassword().toCharArray();
        this.filePath = Path.of(properties.getDirectoryPath()).resolve(properties.getFileName());
    }

    @Override
    public PublicKey loadOrStorePublicKey(final Certificate certificate) {
        try {
            final String certificateAlias = properties.getCertificateAlias();
            if (!keyStore.containsAlias(certificateAlias)) {
                keyStore.setCertificateEntry(certificateAlias, certificate);
            }
            return keyStore.getCertificate(certificateAlias).getPublicKey();
        } catch (KeyStoreException ex) {
            throw new IllegalStateException("An exception occurred during public key loading", ex);
        }
    }

    @Override
    public PrivateKey loadOrStorePrivateKey(final PrivateKey privateKey, final Certificate certificate) {
        try {
            final String privateKeyAlias = properties.getPrivateKeyAlias();
            if (!keyStore.containsAlias(privateKeyAlias)) {
                keyStore.setKeyEntry(privateKeyAlias, privateKey, password, new Certificate[]{certificate});
            }
            return (PrivateKey) keyStore.getKey(privateKeyAlias, password);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            throw new IllegalStateException("An exception occurred during public key loading", ex);
        }
    }

    /* Private methods */
    private void createFilePath() {
        try {
            final Path jksDirectoryPath = Path.of(properties.getDirectoryPath());
            if (Files.notExists(jksDirectoryPath)) Files.createDirectories(jksDirectoryPath);
            final Path jksFilePath = jksDirectoryPath.resolve(properties.getFileName());
            if (Files.notExists(jksFilePath)) Files.createFile(jksFilePath);
        } catch (IOException ex) {
            throw new IllegalStateException("An exception occurred during file path creation", ex);
        }
    }

    private KeyStore instantiateKeyStore() {
        try (final InputStream is = Files.newInputStream(filePath)) {
            final KeyStore keyStoreInstance = KeyStore.getInstance(KeyStore.getDefaultType());
            if (Files.notExists(filePath)) {
                createFilePath();
                keyStoreInstance.load(null, password);
                try (final OutputStream os = Files.newOutputStream(filePath)) {
                    keyStoreInstance.store(os, password);
                }
            }
            keyStoreInstance.load(is, password);
            return keyStoreInstance;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
            throw new IllegalStateException("An exception occurred during key store instantiation", ex);
        }
    }

}
