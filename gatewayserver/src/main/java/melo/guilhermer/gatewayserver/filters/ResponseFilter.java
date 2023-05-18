package melo.guilhermer.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;


public class ResponseFilter {
    final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

    @Autowired
    FilterUtils filterUtils;

    @Autowired
    Tracer tracer;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    String traceId = tracer.currentSpan().context().traceId();
                    logger.debug("Adding the correlation id to the outbound headers. {}", traceId);
                    exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
                    logger.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
                }));
    }
}

