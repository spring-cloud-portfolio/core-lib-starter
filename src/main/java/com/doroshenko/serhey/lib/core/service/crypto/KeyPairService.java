package com.doroshenko.serhey.lib.core.service.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyPairService {

    KeyPair lodKeyPair();

    PublicKey loadPublicKey();

    PrivateKey loadPrivateKey();

}
