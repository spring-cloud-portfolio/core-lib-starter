package com.doroshenko.serhey.lib.core.service.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.function.Supplier;

/**
 * Service to instantiate new java key store or use an existing one.
 *
 * @author Serhey Doroshenko
 * @see DefaultJksService
 */
public interface JksService {

    PublicKey loadOrStorePublicKey(Supplier<Certificate> certificate);

    PrivateKey loadOrStorePrivateKey(Supplier<PrivateKey> privateKey, final Supplier<Certificate> certificate);

}
