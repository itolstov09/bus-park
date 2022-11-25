package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.service.AddressService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AddressController.class)
public class AddressControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AddressService addressService;

    private final Address validEmployeeAddress = new Address("Street", 47);
    private final Address validBusStopAddress =  new Address("Street", null);
    private final List<Address> addresses = new ArrayList<>() {{
        add(validEmployeeAddress);
        add(validBusStopAddress);
    }};


    @Test
    public void whenGetAddresses_thenReturnOK() throws Exception {
        given(addressService.findAll()).willReturn(addresses);
        mockMvc.perform(
                get("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetEmptyAddressList_thenReturnNoContent() throws Exception {
        when(addressService.findAll()).thenReturn(List.of());
        mockMvc.perform(
                        get("/api/v1/addresses")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenGetAddress_thenReturnOK() throws Exception {
        when(addressService.findById(Mockito.anyLong())).thenReturn(validEmployeeAddress);
        mockMvc.perform(
                        get("/api/v1/addresses/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetByInvalidId_thenNotFound() throws Exception {
        when(addressService.findById(Mockito.anyLong())).thenThrow(BPEntityNotFoundException.class);
        mockMvc.perform(
                        get("/api/v1/addresses/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }




}
