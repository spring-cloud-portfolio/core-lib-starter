package com.doroshenko.serhey.lib.core.service.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public interface JksService {

    PublicKey loadOrStorePublicKey(final Certificate certificate);

    PrivateKey loadOrStorePrivateKey(final PrivateKey privateKey, final Certificate certificate);

}
