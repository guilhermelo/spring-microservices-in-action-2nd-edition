package melo.guilhermer.license;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
// busca informações do arquivo de properties
@ConfigurationProperties(prefix = "example")
public class ServiceConfig {
    private String property;

    @Value("${redis.server}")
    private String redisServer = "";

    @Value("${redis.port}")
    private String redisPort = "";

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getRedisPort() {
        return redisPort;
    }

    public String getRedisServer() {
        return redisServer;
    }
}
