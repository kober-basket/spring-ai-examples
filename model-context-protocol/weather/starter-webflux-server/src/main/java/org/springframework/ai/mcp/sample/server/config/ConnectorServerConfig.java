package org.springframework.ai.mcp.sample.server.config;

import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.WebFluxSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ConnectorServerConfig {

    @Resource
    private WebFluxSseServerTransportProvider  webFluxProvider;

    @PostConstruct
    public void init() {
        // 启动一个server
        McpServerFeatures.AsyncToolSpecification tool = new McpServerFeatures.AsyncToolSpecification(
                new McpSchema.Tool(
                        "tool1",
                        "tool1 description",
                        new McpSchema.JsonSchema("string", null, null, null)),
                (exchange, request) ->
                        Mono.just(new McpSchema.CallToolResult("tool1 result", false)));
        McpAsyncServer server = McpServer.async(webFluxProvider)
                .serverInfo("test", "test")
                .tools(tool)
                .build();
        System.out.println("server started, server info: " + server.getServerInfo());
    }
}
