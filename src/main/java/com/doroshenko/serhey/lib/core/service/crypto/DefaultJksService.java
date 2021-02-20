package com.doroshenko.serhey.lib.core.service.crypto;

import com.doroshenko.serhey.lib.core.config.properties.JksProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.function.Supplier;

/**
 * Default implementation of {@link JksService}
 *
 * @author Serhey Doroshenko
 * @see JksService
 */
public class DefaultJksService implements JksService {

    private final char[] password;
    private final KeyStore keyStore;
    private final JksProperties properties;

    private static final Logger log = LoggerFactory.getLogger(DefaultJksService.class);

    public DefaultJksService(final JksProperties properties) {
        this.properties = properties;
        this.password = properties.getPassword().toCharArray();
        this.keyStore = this.instantiateKeyStore(this.password);
    }

    @Override
    public PublicKey loadOrStorePublicKey(final Supplier<Certificate> certificate) {
        try {
            final String certificateAlias = properties.getCertificateAlias();
            if (!keyStore.containsAlias(certificateAlias)) {
                keyStore.setCertificateEntry(certificateAlias, certificate.get());
            }
            return keyStore.getCertificate(certificateAlias).getPublicKey();
        } catch (KeyStoreException ex) {
            throw new IllegalStateException("An exception occurred during public key loading", ex);
        }
    }

    @Override
    public PrivateKey loadOrStorePrivateKey(final Supplier<PrivateKey> privateKey, final Supplier<Certificate> certificate) {
        try {
            final String privateKeyAlias = properties.getPrivateKeyAlias();
            if (!keyStore.containsAlias(privateKeyAlias)) {
                keyStore.setKeyEntry(privateKeyAlias, privateKey.get(), password, new Certificate[]{certificate.get()});
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
            if (Files.notExists(jksFilePath)) {
                Files.createFile(jksFilePath);
                log.info("Created new JKS file [{}]", jksFilePath);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("An exception occurred during file path creation", ex);
        }
    }

    private KeyStore instantiateKeyStore(final char[] password) {
        try {
            final KeyStore keyStoreInstance = KeyStore.getInstance(KeyStore.getDefaultType());
            final Path filePath = Path.of(properties.getDirectoryPath()).resolve(properties.getFileName());
            if (Files.notExists(filePath)) {
                createFilePath();
                keyStoreInstance.load(null, password);
                try (final OutputStream os = Files.newOutputStream(filePath)) {
                    keyStoreInstance.store(os, password);
                }
            }
            try (final InputStream is = Files.newInputStream(filePath)) {
                keyStoreInstance.load(is, password);
            }
            return keyStoreInstance;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
            throw new IllegalStateException("An exception occurred during key store instantiation", ex);
        }
    }

}
