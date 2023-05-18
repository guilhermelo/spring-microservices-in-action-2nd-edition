package com.optimagrowth.organization.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN     = "Authorization";
    public static final String USER_ID        = "tmx-user-id";
    public static final String ORG_ID         = "tmx-org-id";

    private static final ThreadLocal<String> correlationId = new ThreadLocal<String>();
    private static final ThreadLocal<String> authToken = new ThreadLocal<String>();
    private static final ThreadLocal<String> userId = new ThreadLocal<String>();
    private static final ThreadLocal<String> orgId = new ThreadLocal<String>();

    public static HttpHeaders getHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CORRELATION_ID, getCorrelationId().get());

        return httpHeaders;
    }

    public static ThreadLocal<String> getCorrelationId() {
        return correlationId;
    }

}
