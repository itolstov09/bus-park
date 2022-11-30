package dev.tolstov.buspark.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.examples.Example;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Value("classpath:/swagger/requestExamples/bus-stop.json")
    Resource busStopRequestsResource;

    @Value("classpath:/swagger/requestExamples/employee.json")
    Resource employeeRequestResource;


    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Resource> resourceList = List.of(
                this.busStopRequestsResource,
                this.employeeRequestResource
        );
        Map<String, Example> examples = new HashMap<>();
        resourceList.forEach(resource -> {
            String filenameWithExtension = resource.getFilename();
            String filename = filenameWithExtension.substring(0, filenameWithExtension.lastIndexOf("."));

            try {
                JsonNode json = objectMapper.readTree(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
                json.forEach(exampleJson -> {
                    Example example = new Example();
                    example.setDescription(exampleJson.get("description").asText());
                    example.setValue(exampleJson.get("requestBody"));
                    String key = String.format("%s-%s", filename, exampleJson.get("exampleKey").asText());
                    examples.put(key, example);
                } );
            } catch (IOException e) {
                e.printStackTrace();
            }

        });



        return openApi -> examples.forEach( (key, example) -> openApi
                .getComponents()
                .addExamples(key, example) );
    }
}
