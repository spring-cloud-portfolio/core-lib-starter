package com.doroshenko.serhey.lib.core.service.crypto;

import java.security.KeyPair;
import java.security.cert.Certificate;

/**
 * Service for certificate generation
 *
 * @author Serhey Doroshenko
 */
public interface CertificateService {

    Certificate generateCertificate(KeyPair keyPair);

}
