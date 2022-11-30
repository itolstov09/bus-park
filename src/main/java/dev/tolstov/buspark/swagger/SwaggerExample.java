package dev.tolstov.buspark.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.examples.Example;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SwaggerExample {
    @Value("classpath:/swagger/requestExamples/bus-stop.json")
    Resource busStopRequestsResource;

    @Bean
    public Map.Entry<String, List<Example>> loginExample() throws IllegalAccessError, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Example> exampleList = new ArrayList<>();
        AbstractMap.SimpleEntry<String, List<Example>> loginExampleMap = new AbstractMap.SimpleEntry<>("login", null);

        JsonNode json = objectMapper.readTree(new InputStreamReader(busStopRequestsResource.getInputStream(), StandardCharsets.UTF_8));
        json.forEach(exampleJson -> {
            Example example = new Example();
            example.setDescription(exampleJson.get("exampleKey").asText());
            example.setValue(exampleJson.get("requestBody"));
            exampleList.add(example);
        } );
//        JsonNode jsonExamples = json.path(0);



//        for (Object s : json.keySet()){
//            Example example = new Example();
//            example.setValue(json.get(s));
//            example.setDescription(s.toString());
//            ExampleList.add(example);
//        }
        loginExampleMap.setValue(exampleList);

        return loginExampleMap;
    }
}
