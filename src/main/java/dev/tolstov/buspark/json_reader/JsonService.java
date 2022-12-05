package dev.tolstov.buspark.json_reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Загружает в БД записи из json файла.
     *
     * У записей не должно быть id, так как это создание новых записей
     * Запись должна соответствовать одной из моделей
     *
     * Исключения:
     * - Не удалось смаппить json-объект к модели - нет такой модели в списке разрешенных
     * - Ошибка валидации при сохранении в БД
     *
     */
    @Transactional
    public void load() {
        try {
            List<BPEntity> list = new ArrayList<>();

            InputStreamReader streamReader = new InputStreamReader(
                    importResource.getInputStream(), StandardCharsets.UTF_8);
            JsonNode tree = objectMapper.readTree(streamReader);
            tree.forEach(jsonNode -> {
                try {
                    String json = jsonNode.toString();
                    boolean nodeHasId = jsonHasId(json);
                    if (nodeHasId) {
                        throw new RuntimeException("json должен быть без id!");
                    }
                    BPEntity bpEntity = objectMapper.readValue(json, BPEntity.class);

                    list.add(bpEntity);
                } catch (InvalidTypeIdException invalidTypeIdException) {
                    throw new RuntimeException("invalid modelName");
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

    public void saveToDB(List<BPEntity> list) {
        list.forEach(entity -> {
            if (entity instanceof Address) {
                addressService.save((Address) entity);
            } else if (entity instanceof BusStop) {
                busStopService.save((BusStop) entity);
            } else {
                throw new RuntimeException("Неожиданный класс при сохранении entity в БД из JSON");
            }
        });

    }

    private boolean jsonHasId(String json) {
        return json.contains("\"id\":");
    }
}
