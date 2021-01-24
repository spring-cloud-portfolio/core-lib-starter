package com.doroshenko.serhey.lib.core.config;

import com.doroshenko.serhey.lib.core.config.properties.CertificateProperties;
import com.doroshenko.serhey.lib.core.config.properties.JksProperties;
import com.doroshenko.serhey.lib.core.service.crypto.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan({"com.doroshenko.serhey.lib.core"})
@ConfigurationPropertiesScan({"com.doroshenko.serhey.lib.core.config.properties"})
public class CoreLibAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JksService jksService(final JksProperties properties) {
        return new DefaultJksService(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyPairService keyPairService(final JksService jksService,
                                         final CertificateService certificateService) {
        return new RsaKeyPairService(jksService, certificateService);
    }

    @Bean
    @ConditionalOnMissingBean
    public CertificateService certificateService(final CertificateProperties properties) {
        return new X509V3CertificateService(properties);
    }

}
