package dev.tolstov.buspark.swagger;

import io.swagger.v3.oas.models.examples.Example;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Autowired
    SwaggerExample swaggerExample;

    @Bean
    public OpenApiCustomiser openApiCustomiser(Collection<Map.Entry<String, List<Example>>> examples) {
        return openApi -> {
            examples.forEach(example -> {
                example.getValue().forEach(e -> openApi.getComponents().addExamples(e.getDescription(), e));
            });
        };
    }
}
