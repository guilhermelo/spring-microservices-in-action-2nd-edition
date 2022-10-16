package melo.guilhermer.license;

import org.springframework.boot.context.properties.ConfigurationProperties;

// busca informações do arquivo de properties
@ConfigurationProperties(prefix = "example")
public class ServiceConfig {
    private String property;

    public String getProperty() {
        return property;
    }
}
