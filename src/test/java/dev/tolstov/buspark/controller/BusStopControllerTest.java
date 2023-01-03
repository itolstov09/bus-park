package dev.tolstov.buspark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.service.BusStopServiceImpl;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BusStopController.class)
public class BusStopControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BusStopServiceImpl busStopServiceImpl;

    // get all: valid - ok
    // get all: empty - no content
    // get byId: valid - ok
    // get byId: invalid - not found
    // post: valid - created
    // post invalid - bad request

    // get all: valid - ok
    @Test
    public void whenGetAllBusStops_thenStatusOK() throws Exception {
        BusStop bs1 = new BusStop("Bus stop 1");
        BusStop bs2 = new BusStop("Bus stop 2");
        Page<BusStop> busStops = new PageImpl<>(List.of(bs1, bs2));

        when(busStopServiceImpl.getPage(0, 10)).thenReturn(busStops);
        mockMvc.perform(get("/api/v1/busStops?page=0&size=10").contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk());
    }

    // get all: empty - no content
    @Test
    public void whenGetEmptyList_thenStatusOK() throws Exception {
        when(busStopServiceImpl.getPage(0, 10)).thenReturn(null);
        mockMvc.perform(get("/api/v1/busStops?page=0&size=10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // get byId: valid - ok
    @Test
    public void whenGetByExistId_thenStatusOK() throws Exception {
        BusStop bs1 = new BusStop("Bus stop 1");

        when(busStopServiceImpl.findById(Mockito.anyLong())).thenReturn(bs1);
        mockMvc.perform(get("/api/v1/busStops/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    // get byId: invalid - not found
    @Test
    public void whenA_thenB() throws Exception {
        when(busStopServiceImpl.findById(Mockito.anyLong())).thenThrow(BPEntityNotFoundException.class);
        mockMvc.perform(
                        get("/api/v1/busStops/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // post: valid - created
    @Test
    public void whenSaveValidBusStop_thenStatusCreated() throws Exception {
        BusStop bs1 = new BusStop("Bus stop 1");
        bs1.setAddress(new Address("s", null));

        when(busStopServiceImpl.save(any(BusStop.class))).thenReturn(bs1);
        mockMvc.perform(
                        post("/api/v1/busStops")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(entityToJSON(bs1))
                ).andExpect(status().isCreated());
    }

    // todo доделать. вместо 400 почему то возвращает 201
    // post invalid - bad request
//    @Test
//    public void whenSaveInValidBusStop_thenStatusBadRequest() throws Exception {
//        Address address = new Address("", 50);
//        BusStop bs1 = new BusStop("");
//        bs1.setAddress(address);
//
//        when(busStopService.save(bs1, address)).thenThrow(ConstraintViolationException.class);
//        mockMvc.perform(
//                        post("/api/v1/busStops")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .accept(MediaType.APPLICATION_JSON)
//                                .content(entityToJSON(bs1))
//                ).andExpect(status().isBadRequest());
//    }


    @SneakyThrows
    private String entityToJSON(BusStop entity) {
        return objectMapper.writeValueAsString(entity);
    }
}
