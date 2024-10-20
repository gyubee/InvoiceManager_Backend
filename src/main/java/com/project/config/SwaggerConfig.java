package com.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI serverApiConfig() {

        Server deployServer = new Server();
        deployServer.setUrl("https://invo.flaresolution.com");
        deployServer.setDescription("Deploy");

        Server localServer = new Server();
        localServer.setUrl("https://localhost:8001");
        localServer.setDescription("Local");

        Info information = new Info()
                .title("Invoice Manager API")
                .version("1.0")
                .description("This API exposes endpoints to manage invoices.");

        return new OpenAPI().info(information).servers(Arrays.asList(deployServer, localServer));
    }
}
