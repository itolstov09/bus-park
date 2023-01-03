package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.service.AddressServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
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
    AddressServiceImpl addressServiceImpl;

    private final Address validEmployeeAddress = new Address("Street", 47);
    private final Address validBusStopAddress =  new Address("Street", null);
    private final List<Address> addresses = new ArrayList<>() {{
        add(validEmployeeAddress);
        add(validBusStopAddress);
    }};


    @Test
    public void whenGetAddresses_thenReturnOK() throws Exception {
        PageImpl<Address> addressesPage = new PageImpl<>(this.addresses);
        given(addressServiceImpl.getPage(0,10)).willReturn(addressesPage);
        mockMvc.perform(
                get("/api/v1/addresses?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetEmptyAddressList_thenReturnOK() throws Exception {
        when(addressServiceImpl.getPage(0, 10)).thenReturn(null);
        mockMvc.perform(
                        get("/api/v1/addresses?page=0&size=10")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetAddress_thenReturnOK() throws Exception {
        when(addressServiceImpl.findById(Mockito.anyLong())).thenReturn(validEmployeeAddress);
        mockMvc.perform(
                        get("/api/v1/addresses/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetByInvalidId_thenNotFound() throws Exception {
        when(addressServiceImpl.findById(Mockito.anyLong())).thenThrow(BPEntityNotFoundException.class);
        mockMvc.perform(
                        get("/api/v1/addresses/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }




}
