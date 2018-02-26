package config.refresh.client;

import java.security.GeneralSecurityException;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnClass({ ConfigServicePropertySourceLocator.class, RestTemplate.class })
public class ConfigClientApplicationConfig {

    private final ConfigServicePropertySourceLocator locator;

    @Autowired
    public ConfigClientApplicationConfig(ConfigServicePropertySourceLocator locator) {
        this.locator = locator;
    }

    @PostConstruct
    public void init() throws GeneralSecurityException {
        locator.setRestTemplate(getRestTemplate());
    }

    @Bean
    @ConfigurationProperties()
    public HttpComponentsClientHttpRequestFactory getHttpRequestFactory() throws GeneralSecurityException {
        SSLContext sslContext = new SSLContextBuilder().build();
        HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Primary
    @Bean
    public RestTemplate getRestTemplate() throws GeneralSecurityException {
        return new RestTemplate(getHttpRequestFactory());
    }
}
