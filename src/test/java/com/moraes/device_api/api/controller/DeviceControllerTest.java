package com.moraes.device_api.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.device_api.api.exception.ResourceNotFoundException;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.service.interfaces.IDeviceService;
import com.moraes.device_api.mock.MockDeviceDTO;

@WebMvcTest
class DeviceControllerTest {

    private static final String BASE_URL = "/api/v1/devices";

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    private IDeviceService service;

    @Autowired
    private ObjectMapper objectMapper;

    private MockDeviceDTO mockDeviceDTO;

    @BeforeEach
    void setUp() {
        mockDeviceDTO = new MockDeviceDTO();
    }

    @Test
    @DisplayName("JUnit test given DeviceDTO when insert then return created")
    void testGivenDeviceDTOWhenInsertThenReturnCreated() throws Exception {
        // given
        final var dto = mockDeviceDTO.mockEntity(1);
        final var json = objectMapper.writeValueAsString(dto);
        // when
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // then
        response.andExpect(status().isCreated()).andExpect(jsonPath("$", is(0)));
    }

    @Test
    @DisplayName("JUnit test given empty DeviceDTO when insert then return bad request")
    void testGivenEmptyDeviceDTOWhenInsertThenReturnBadRequest() throws Exception {
        // given
        final var json = "{}";
        // when
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // then
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("JUnit test given valid ID when getById then return DeviceListDTO and status OK")
    void testGivenValidIdWhenGetByIdThenReturnDeviceListDTO() throws Exception {
        // given
        long id = 1L;
        DeviceListDTO dto = new DeviceListDTO();
        given(service.getDTOById(id)).willReturn(dto);
        // when
        ResultActions response = mockMvc.perform(get(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @DisplayName("JUnit test given invalid ID when getById then return Not Found")
    void testGivenInvalidIdWhenGetByIdThenReturnNotFound() throws Exception {
        // given
        long id = 100L;
        given(service.getDTOById(id)).willThrow(new ResourceNotFoundException("Device not found"));
        // when
        ResultActions response = mockMvc.perform(get(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("JUnit test given valid DeviceDTO when update then return OK")
    void testGivenValidDeviceDTOWhenUpdateThenReturnOk() throws Exception {
        // given
        long id = 1L;
        DeviceDTO dto = mockDeviceDTO.mockEntity(1);
        String json = objectMapper.writeValueAsString(dto);
        // when
        ResultActions response = mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // then
        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("JUnit test given invalid DeviceDTO when update then return Bad Request")
    void testGivenInvalidDeviceDTOWhenUpdateThenReturnBadRequest() throws Exception {
        // given
        long id = 1L;
        String json = "{}";
        // when
        ResultActions response = mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // then
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("JUnit test given valid DeviceDTO when updatePartial then return OK")
    void testGivenValidDeviceDTOWhenUpdatePartialThenReturnOk() throws Exception {
        // given
        long id = 1L;
        DeviceDTO dto = mockDeviceDTO.mockEntity(1);
        String json = objectMapper.writeValueAsString(dto);
        // when
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // then
        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("JUnit test given empty DeviceDTO when updatePartial then return ok")
    void testGivenEmptyDeviceDTOWhenUpdatePartialThenReturnOk() throws Exception {
        // given
        long id = 1L;
        String json = "{}";
        // when
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // then
        response.andExpect(status().isOk());
    }
}
