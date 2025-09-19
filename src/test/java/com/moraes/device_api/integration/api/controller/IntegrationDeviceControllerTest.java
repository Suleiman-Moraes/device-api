package com.moraes.device_api.integration.api.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;
import com.moraes.device_api.config.AbstractIntegrationTest;
import com.moraes.device_api.config.TestConfig;
import com.moraes.device_api.mock.MockDeviceDTO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class IntegrationDeviceControllerTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/devices";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static MockDeviceDTO mockDeviceDTO;
    private static DeviceDTO dto;
    private static Long id;

    @BeforeAll
    static void setup() {
        // Create an ObjectMapper instance
        mapper = new ObjectMapper();
        mockDeviceDTO = new MockDeviceDTO();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        dto = mockDeviceDTO.mockEntity(1);

        // Create a RequestSpecification instance
        specification = new RequestSpecBuilder()
                .setBasePath(BASE_URL)
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("JUnit Integration test Given DeviceDTO When insert Then return key")
    void testGivenDeviceDTOWhenInsertThenReturnKey() throws Exception {
        final Response response = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(dto)
                .when()
                .post();

        response.then().statusCode(201);
        id = mapper.readValue(response.getBody().asString(), Long.class);
        assertNotNull(id, "ID is null");
        assertTrue(id > 0, "ID is not greater than zero");
    }

    @Test
    @Order(2)
    @DisplayName("JUnit Integration test Given key When getById Then return DeviceDTO")
    void testGivenKeyWhenGetByIdThenReturnDevice() throws Exception {
        final Response response = getById();
        response.then().statusCode(200);
        final DeviceDTO dtoResponse = mapper.readValue(response.getBody().asString(), DeviceDTO.class);

        assertNotNull(dtoResponse, "DTO is null");
        assertEquals(dto.getName(), dtoResponse.getName(), "Name does not match");
        assertEquals(dto.getBrand(), dtoResponse.getBrand(), "Brand does not match");
        assertEquals(dto.getState(), dtoResponse.getState(), "State does not match");
    }

    @Test
    @Order(3)
    @DisplayName("JUnit Integration test Given DeviceDTO and id When update Then return 204")
    void testGivenDeviceDTOAndIdWhenUpdateThenReturn204() {
        dto.setName("Updated Name");
        final Response response = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(dto)
                .pathParam("id", id)
                .when()
                .put("/{id}");
        response.then().statusCode(204);
    }

    @Test
    @Order(4)
    @DisplayName("JUnit Integration test Given ID When getById After update Then return DeviceDTO with updated data")
    void testGivenIdWhenGetByIdAfterUpdateThenReturnDeviceDTOWithUpdatedData() throws Exception {
        final Response response = getById();
        response.then().statusCode(200);
        final DeviceDTO dtoResponse = mapper.readValue(response.getBody().asString(), DeviceDTO.class);

        assertNotNull(dtoResponse, "DTO is null");
        assertEquals(dto.getName(), dtoResponse.getName(), "Name was not updated");
        assertEquals(dto.getBrand(), dtoResponse.getBrand(), "Brand does not match");
        assertEquals(dto.getState(), dtoResponse.getState(), "State does not match");
    }

    @Test
    @Order(5)
    @DisplayName("JUnit Integration test Given DeviceFilterDTO with paginate false When getAll Then return Page<DeviceListDTO>")
    void testGivenDeviceFilterDTOWithPaginateFalseWhenGetAllThenReturnPageDeviceListDTO() {
        final Response response = given().spec(specification)
                .queryParam("name", "Updated Name")
                .queryParam("paginate", false)
                .when()
                .get();
        response.then().statusCode(200);

        String json = response.getBody().asString();
        assertNotNull(json, "Response body is null");
        assertTrue(json.contains("Updated Name"), "Response does not contain expected name");
        assertTrue(json.contains("brand"), "Response does not contain expected brand");
        assertTrue(json.contains("state"), "Response does not contain expected state");
    }

    @Test
    @Order(6)
    @DisplayName("JUnit Integration test Given brand When getByBrand Then return List<DeviceListDTO>")
    void testGivenBrandWhenGetByBrandThenReturnListDeviceListDTO() {
        final Response response = given().spec(specification)
                .queryParam("brand", dto.getBrand())
                .when()
                .get("/brand");
        response.then().statusCode(200);

        String json = response.getBody().asString();
        assertNotNull(json, "Response body is null");
        assertTrue(json.contains("brand"), "Response does not contain expected brand");
        assertTrue(json.contains("state"), "Response does not contain expected state");
    }

    @Test
    @Order(7)
    @DisplayName("JUnit Integration test Given state When getByState Then return List<DeviceListDTO>")
    void testGivenStateWhenGetByStateThenReturnListDeviceListDTO() {
        final Response response = given().spec(specification)
                .queryParam("state", DeviceStateEnum.IN_USE)
                .when()
                .get("/state");
        response.then().statusCode(200);

        String json = response.getBody().asString();
        assertNotNull(json, "Response body is null");
        assertTrue(json.contains("brand"), "Response does not contain expected brand");
        assertTrue(json.contains("state"), "Response does not contain expected state");
    }

    @Test
    @Order(8)
    @DisplayName("JUnit Integration test Given Id When delete Then return no content")
    void testGivenIdWhenDeleteThenReturnNoContent() {
        final Response response = given().spec(specification)
                .pathParam("id", id)
                .when()
                .delete("/{id}");
        response.then().statusCode(204);
    }

    private static Response getById() {
        final Response response = given().spec(specification)
                .pathParam("id", id)
                .when()
                .get("/{id}");
        response.then().statusCode(200);
        return response;
    }
}
