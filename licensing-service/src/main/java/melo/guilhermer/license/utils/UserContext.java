package melo.guilhermer.license.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN     = "tmx-auth-token";
    public static final String USER_ID        = "tmx-user-id";
    public static final String ORGANIZATION_ID = "tmx-organization-id";

    private String correlationId= new String();
    private String authToken= new String();
    private String userId = new String();
    private String organizationId = new String();

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getAuthToken() {
        return authToken;
    }
}
