package dev.tolstov.buspark.json_reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tolstov.buspark.exception.JsonServiceException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BPEntity;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.service.AddressService;
import dev.tolstov.buspark.service.BusStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class JsonService {
    @Value("classpath:/import.json")
    Resource importResource;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private AddressService addressService;
    @Autowired
    private BusStopService busStopService;

    private final Set<String> models = Set.of(
            "address",
            "busStop"
    );

    /**
     * Загружает в БД записи из json файла.
     *
     * У записей не должно быть id, так как это создание новых записей
     * Запись должна соответствовать одной из моделей - address, busStop
     *
     * Исключения:
     * - Не удалось смаппить json-объект к модели - нет такой модели в списке разрешенных
     * - Ошибка валидации при сохранении в БД
     *
     */
    @Transactional
    public void loadAndSaveToDB() {
        try {
            List<BPEntity> list = new ArrayList<>();

            InputStreamReader streamReader = new InputStreamReader(
                    importResource.getInputStream(), StandardCharsets.UTF_8);
            JsonNode tree = objectMapper.readTree(streamReader);
            tree.forEach(jsonNode -> {
                try {
                    String json = jsonNode.toString();

                    JsonNode modelName = getField(jsonNode, "modelName");
                    String modelNameString = modelName.asText();
                    if (!models.contains(modelNameString)) {
                        throw new JsonServiceException("invalid modelName: " + json);
                    }
                    Class<?> entityClass = BPEntity.class;
                    switch (modelNameString) {
                        case "address":
                            entityClass = Address.class;
                            break;
                        case "busStop":
                            entityClass = BusStop.class;
                            break;
                    }

                    boolean nodeHasId = jsonHasId(json);
                    if (nodeHasId) {
                        throw new JsonServiceException("json must be without id: " + json);
                    }
                    JsonNode data = getField(jsonNode, "data");

                    BPEntity bpEntity = (BPEntity) objectMapper.readValue(data.toString(), entityClass);

                    list.add(bpEntity);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });

            saveToDB(list);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonNode getField(JsonNode jsonNode, String fieldName) {
        JsonNode node = jsonNode.get(fieldName);
        if (node == null) {
            throw new JsonServiceException(String.format("%s field is missing: '%s'", fieldName, jsonNode));
        }
        return node;
    }

    // todo разобраться как работают транзакции

    //по-хорошему должен быть private, но тогда не будут работать транзакции.. и хз почему..
    public void saveToDB(List<BPEntity> list) {
        list.forEach(entity -> {
            try {
                if (entity instanceof Address) {
                    addressService.save((Address) entity);
                } else if (entity instanceof BusStop) {
                    busStopService.save((BusStop) entity);
                } else {
                    throw new RuntimeException("Unexpected class on saving from JSON");
                }
            } catch (EntityExistsException | ConstraintViolationException exception) {
                throw new JsonServiceException(
                        String.format("cause: %s, data: %s", exception.getMessage(), entity.toString()));
            }
        });

    }

    private boolean jsonHasId(String json) {
        return json.contains("\"id\":");
    }
}
