package org.springframework.ai.mcp.sample.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.transport.WebFluxSseServerTransportProvider;
import org.springframework.ai.mcp.server.autoconfigure.McpServerProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

@Configuration
public class MyMcpWebServerAutoConfig {

    private static final String CUSTOM_SSE_ENDPOINT = "/test/hhh";

    private static final String CUSTOM_MESSAGE_ENDPOINT = "/mcp/message";


    @Bean
    public WebFluxSseServerTransportProvider webFluxTransport(ObjectProvider<ObjectMapper> objectMapperProvider,
                                                              McpServerProperties serverProperties) {
        ObjectMapper objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);

        return WebFluxSseServerTransportProvider.builder()
                .sseEndpoint(CUSTOM_SSE_ENDPOINT)
                .messageEndpoint(CUSTOM_MESSAGE_ENDPOINT)
                .objectMapper(objectMapper)
                .build();

//        return new WebFluxSseServerTransportProvider(objectMapper, serverProperties.getSseMessageEndpoint());
    }

    // Router function for SSE transport used by Spring WebFlux to start an HTTP server.
    @Bean
    public RouterFunction<?> webfluxMcpRouterFunction(WebFluxSseServerTransportProvider webFluxProvider) {
        return webFluxProvider.getRouterFunction().filter(
                (request, next) -> {
                    String path = request.path();
                    if (CUSTOM_SSE_ENDPOINT.equals(path)) {
                        String token = request.queryParam("token").orElse(null);
                        if (token == null || !token.equals("12345")) {
                            System.out.println("Invalid token: " + token);
                            return Mono.empty();
//                            return Mono.just(ServerResponse.status(HttpStatus.FORBIDDEN).build());
//                            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
                        }
                        System.out.println("Valid token: " + token);
                        return next.handle(request);
                    }


                    return next.handle(request);
                }
        );
    }

}
