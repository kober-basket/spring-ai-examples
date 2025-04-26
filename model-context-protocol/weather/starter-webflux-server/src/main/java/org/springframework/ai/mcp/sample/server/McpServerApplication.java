package org.springframework.ai.mcp.sample.server;

import io.modelcontextprotocol.server.transport.WebFluxSseServerTransportProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpServerApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider weatherTools(WeatherService weatherService, WeatherService2 weatherService2) {
		return MethodToolCallbackProvider.builder().toolObjects(weatherService, weatherService2).build();
	}

	public record TextInput(String input) {
	}

	@Bean
	public ToolCallback toUpperCase() {
		return FunctionToolCallback.builder("toUpperCase", (TextInput input) -> input.input().toUpperCase())
				.inputType(TextInput.class)
				.description("Put the text to upper case")
				.build();
	}

	@Bean
	public ToolCallback toUpperCase2() {
		return FunctionToolCallback.builder("toUpperCase2", (TextInput input) -> input.input().toUpperCase())
				.inputType(TextInput.class)
				.description("Put the text to upper case")
				.build();
	}

}
