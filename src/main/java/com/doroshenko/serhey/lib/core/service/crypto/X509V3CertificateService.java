package com.doroshenko.serhey.lib.core.service.crypto;

import com.doroshenko.serhey.lib.core.config.properties.CertificateProperties;
import org.apache.commons.lang3.time.DateUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Date;

import static java.math.BigInteger.ONE;

/**
 * Implementation of {@link CertificateService} for x.509 certificate generation
 *
 * @author Serhey Doroshenko
 * @see CertificateService
 * @see CertificateProperties
 */
public class X509V3CertificateService implements CertificateService {

    public static final String X_509_CERTIFICATE_TYPE = "x.509";

    private final CertificateProperties properties;

    public X509V3CertificateService(final CertificateProperties properties) {
        this.properties = properties;
    }

    public Certificate generateCertificate(final KeyPair keyPair) {
        try (final InputStream is = new ByteArrayInputStream(buildCertificate(keyPair).getEncoded())) {
            return CertificateFactory.getInstance(X_509_CERTIFICATE_TYPE).generateCertificate(is);
        } catch (CertificateException | IOException ex) {
            throw new IllegalStateException("An exception occurred during certificate generation", ex);
        }
    }

    /* Private methods */
    private X509CertificateHolder buildCertificate(final KeyPair keyPair) {
        try {
            final X509v3CertificateBuilder builder = configureCertificateBuilder(keyPair);
            final JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder(properties.getAlgorithm());
            return builder.build(signerBuilder.build(keyPair.getPrivate()));
        } catch (OperatorCreationException ex) {
            throw new IllegalStateException("An exception occurred during certificate building", ex);
        }
    }

    private X509v3CertificateBuilder configureCertificateBuilder(final KeyPair keyPair) {
        final Date now = new Date();
        final X500Name issuer = new X500Name(properties.getIssuer());
        final X500Name subject = new X500Name(properties.getSubject());
        final Date expiry = DateUtils.addDays(now, properties.getExpiration());
        final SubjectPublicKeyInfo keyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        return new X509v3CertificateBuilder(issuer, ONE, now, expiry, subject, keyInfo);
    }

}
