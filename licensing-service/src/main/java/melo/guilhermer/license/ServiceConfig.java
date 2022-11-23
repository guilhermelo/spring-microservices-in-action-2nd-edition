package melo.guilhermer.license;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
// busca informações do arquivo de properties
@ConfigurationProperties(prefix = "example")
public class ServiceConfig {
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
