package com.doroshenko.serhey.lib.core.service.crypto;

import com.doroshenko.serhey.lib.core.config.CoreLibAutoConfiguration;
import com.doroshenko.serhey.lib.core.config.properties.CertificateProperties;
import com.doroshenko.serhey.lib.core.config.properties.JksProperties;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Service to load {@link KeyPair} or {@link PublicKey} and {@link PrivateKey} separately.
 * Configuration goes through the {@link JksProperties} and {@link CertificateProperties}.
 * In order to change the configuration, simply write your settings in the sprung configuration file (e.g. application.yml).
 * During autoconfiguration, this service will be instantiated as a Spring Bean with {@link RsaKeyPairService} implementation by default.
 * In order to change autoconfiguration behavior, just instantiate your own bean with {@link KeyPairService} type and its custom implementation.
 *
 * @author Serhey Doroshenko
 * @see JksProperties
 * @see CertificateProperties
 * @see RsaKeyPairService
 * @see CoreLibAutoConfiguration
 */
public interface KeyPairService {

    KeyPair lodKeyPair();

    PublicKey loadPublicKey();

    PrivateKey loadPrivateKey();

}
