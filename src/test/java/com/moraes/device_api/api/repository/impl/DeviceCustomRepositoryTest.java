package com.moraes.device_api.api.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.moraes.device_api.api.model.dto.device.DeviceFilterDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

class DeviceCustomRepositoryTest {

    @Spy
    @InjectMocks
    private DeviceCustomRepository service;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("JUnit test given DeviceFilterDTO with brand, name and searchText when applyFilters then append SQL and map values")
    void testGivenDeviceFilterDTOWithAllFieldsWhenApplyFiltersThenAppendSQLAndMapValues() {
        final var filter = DeviceFilterDTO.builder()
                .brand("Samsung")
                .name("Galaxy")
                .searchText("phone")
                .build();
        final Map<String, Object> params = new HashMap<>();
        final StringBuilder sql = new StringBuilder("SELECT * FROM item WHERE 1=1");

        service.applyFilters(filter, params, sql);

        assertNotNull(sql, "SQL should not be null");
        assertNotNull(params, "Params map should not be null");

        final String resultSQL = sql.toString();
        assertEquals("SELECT * FROM item WHERE 1=1 AND item.brand = :brand AND item.name ILIKE :name" +
                " (item.name ILIKE :searchText  OR item.brand ILIKE :searchText " +
                " OR item.state ILIKE :searchText  OR TO_CHAR(item.creation_time, 'YYYY-MM-DD HH24:MI:SS') ILIKE :searchText) ",
                resultSQL, "SQL should contain all appended filters");

        assertEquals("Samsung", params.get("brand"), "Brand param should match");
        assertEquals("%Galaxy%", params.get("name"), "Name param should match");
        assertEquals("%phone%", params.get("searchText"), "SearchText param should be wrapped with %");
    }

    @Test
    @DisplayName("JUnit test given DeviceFilterDTO with only brand when applyFilters then append brand filter")
    void testGivenDeviceFilterDTOWithOnlyBrandWhenApplyFiltersThenAppendBrandFilter() {
        final var filter = DeviceFilterDTO.builder().brand("Apple").build();
        final Map<String, Object> params = new HashMap<>();
        final StringBuilder sql = new StringBuilder("SELECT * FROM item WHERE 1=1");

        service.applyFilters(filter, params, sql);

        final String resultSQL = sql.toString();
        assertEquals("SELECT * FROM item WHERE 1=1 AND item.brand = :brand", resultSQL,
                "SQL should contain brand filter only");
        assertEquals("Apple", params.get("brand"), "Brand param should match");
    }

    @Test
    @DisplayName("JUnit test given DeviceFilterDTO with only state when applyFilters then append state filter")
    void testGivenDeviceFilterDTOWithOnlyStateWhenApplyFiltersThenAppendStateFilter() {
        final var filter = DeviceFilterDTO.builder().state(DeviceStateEnum.AVAILABLE).build();
        final Map<String, Object> params = new HashMap<>();
        final StringBuilder sql = new StringBuilder("SELECT * FROM item WHERE 1=1");

        service.applyFilters(filter, params, sql);

        final String resultSQL = sql.toString();
        assertEquals("SELECT * FROM item WHERE 1=1 AND item.state = :state", resultSQL,
                "SQL should contain state filter only");
        assertEquals(DeviceStateEnum.AVAILABLE.name(), params.get("state"), "State param should match");
    }

    @Test
    @DisplayName("JUnit test given DeviceFilterDTO with only name when applyFilters then append name filter")
    void testGivenDeviceFilterDTOWithOnlyNameWhenApplyFiltersThenAppendNameFilter() {
        final var filter = DeviceFilterDTO.builder().name("iPhone").build();
        final Map<String, Object> params = new HashMap<>();
        final StringBuilder sql = new StringBuilder("SELECT * FROM item WHERE 1=1");

        service.applyFilters(filter, params, sql);

        final String resultSQL = sql.toString();
        assertEquals("SELECT * FROM item WHERE 1=1 AND item.name ILIKE :name", resultSQL,
                "SQL should contain name filter only");
        assertEquals("%iPhone%", params.get("name"), "Name param should match");
    }

    @Test
    @DisplayName("JUnit test given DeviceFilterDTO with only searchText when applyFilters then append searchText filter")
    void testGivenDeviceFilterDTOWithOnlySearchTextWhenApplyFiltersThenAppendSearchTextFilter() {
        final var filter = DeviceFilterDTO.builder().searchText("test").build();
        final Map<String, Object> params = new HashMap<>();
        final StringBuilder sql = new StringBuilder("SELECT * FROM item WHERE 1=1");

        service.applyFilters(filter, params, sql);

        final String resultSQL = sql.toString();
        assertEquals("SELECT * FROM item WHERE 1=1 (item.name ILIKE :searchText  OR item.brand ILIKE :searchText " +
                " OR item.state ILIKE :searchText  OR TO_CHAR(item.creation_time, 'YYYY-MM-DD HH24:MI:SS') ILIKE :searchText) ",
                resultSQL, "SQL should contain searchText filter only");
        assertEquals("%test%", params.get("searchText"), "SearchText param should be wrapped with %");
    }

    @Test
    @DisplayName("JUnit test given DeviceFilterDTO with no values when applyFilters then SQL remains unchanged")
    void testGivenDeviceFilterDTOWithNoValuesWhenApplyFiltersThenSQLRemainsUnchanged() {
        final var filter = DeviceFilterDTO.builder().build();
        final Map<String, Object> params = new HashMap<>();
        final StringBuilder sql = new StringBuilder("SELECT * FROM item WHERE 1=1");

        service.applyFilters(filter, params, sql);

        final String resultSQL = sql.toString();
        assertEquals("SELECT * FROM item WHERE 1=1", resultSQL,
                "SQL should remain unchanged when no filters are provided");
        assertEquals(0, params.size(), "Params map should be empty");
    }

    @Test
    @DisplayName("JUnit test given filter with brand when getQueryByFilter and mappingName null then create native query without mapping")
    void testGivenFilterWithBrandWhenGetQueryByFilterAndMappingNameNullThenCreateNativeQuery() {
        final var filter = DeviceFilterDTO.builder().brand("Samsung").build();
        final String sql = "SELECT *";
        final String complement = "ORDER BY item.id";

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);

        final Query response = service.getQueryByFilter(filter, sql, complement, null);

        assertNotNull(response, "Query should not be null");
        verify(entityManager, times(1)).createNativeQuery(contains("FROM device_api.device item"));
        verify(query, times(1)).setParameter("brand", "Samsung");
    }

    @Test
    @DisplayName("JUnit test given filter with name and mappingName not null when getQueryByFilter then create native query with mapping")
    void testGivenFilterWithNameAndMappingNameNotNullWhenGetQueryByFilterThenCreateNativeQueryWithMapping() {
        final var filter = DeviceFilterDTO.builder().name("Galaxy").build();
        final String sql = "SELECT *";
        final String complement = "ORDER BY item.name";
        final String mappingName = "DeviceMapping";

        when(entityManager.createNativeQuery(anyString(), eq(mappingName))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);

        final Query response = service.getQueryByFilter(filter, sql, complement, mappingName);

        assertNotNull(response, "Query should not be null");
        verify(entityManager, times(1)).createNativeQuery(contains("FROM device_api.device item"), eq(mappingName));
        verify(query, times(1)).setParameter("name", "%Galaxy%");
    }

    @Test
    @DisplayName("JUnit test given filter with searchText when getQueryByFilter then set searchText parameter")
    void testGivenFilterWithSearchTextWhenGetQueryByFilterThenSetSearchTextParameter() {
        final var filter = DeviceFilterDTO.builder().searchText("phone").build();
        final String sql = "SELECT item.id";
        final String complement = "";

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);

        final Query response = service.getQueryByFilter(filter, sql, complement, null);

        assertNotNull(response, "Query should not be null");
        verify(query, times(1)).setParameter("searchText", "%phone%");
    }

    @Test
    @DisplayName("JUnit test given empty filter when getQueryByFilter then no parameters are set")
    void testGivenEmptyFilterWhenGetQueryByFilterThenNoParametersAreSet() {
        final var filter = DeviceFilterDTO.builder().build();
        final String sql = "SELECT *";
        final String complement = "";

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);

        final Query response = service.getQueryByFilter(filter, sql, complement, null);

        assertNotNull(response, "Query should not be null");
        verify(query, never()).setParameter(anyString(), any());
    }

    @Test
    @DisplayName("JUnit test given EntityManager throws exception when getQueryByFilter then rethrow exception")
    void testGivenEntityManagerThrowsExceptionWhenGetQueryByFilterThenRethrowException() {
        final var filter = DeviceFilterDTO.builder().brand("ErrorTest").build();
        final String sql = "SELECT *";
        final String complement = "";

        when(entityManager.createNativeQuery(anyString())).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.getQueryByFilter(filter, sql, complement, null);
        }, "Should rethrow RuntimeException when EntityManager fails");

        assertEquals("DB error", ex.getMessage(), "Exception message should be equal");
    }

    @Test
    @DisplayName("JUnit test given valid filter when countByFilter then return count as integer")
    void testGivenValidFilterWhenCountByFilterThenReturnCountAsInteger() {
        final var filter = DeviceFilterDTO.builder().brand("Samsung").build();

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(5L);

        final Integer response = service.countByFilter(filter);

        assertNotNull(response, "Response should not be null");
        assertEquals(5, response, "Count should be equal to query result");
        verify(query, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("JUnit test given filter when getSingleResult returns String then parse to integer")
    void testGivenFilterWhenGetSingleResultReturnsStringThenParseToInteger() {
        final var filter = DeviceFilterDTO.builder().name("Galaxy").build();

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn("10");

        final Integer response = service.countByFilter(filter);

        assertNotNull(response, "Response should not be null");
        assertEquals(10, response, "Count should be parsed from string result");
    }

    @Test
    @DisplayName("JUnit test given filter when query throws exception then return zero")
    void testGivenFilterWhenQueryThrowsExceptionThenReturnZero() {
        final var filter = DeviceFilterDTO.builder().searchText("error").build();

        when(entityManager.createNativeQuery(anyString())).thenThrow(new RuntimeException("DB error"));

        final Integer response = service.countByFilter(filter);

        assertNotNull(response, "Response should not be null");
        assertEquals(0, response, "Count should be zero when exception occurs");
    }

    @Test
    @DisplayName("JUnit test given filter when getQueryByFilter throws exception then listByFilter returns null")
    void testGivenFilterWhenGetQueryByFilterThrowsExceptionThenReturnNull() {
        final var filter = DeviceFilterDTO.builder().property("state").direction(Direction.ASC).build();

        when(entityManager.createNativeQuery(anyString(), eq(DeviceListDTO.DEVICE_LIST_DTO_MAPPING)))
                .thenThrow(new RuntimeException("DB error"));

        final List<DeviceListDTO> response = service.listByFilter(filter);

        assertNull(response, "Response should be null when exception occurs");
    }

    @Test
    @DisplayName("JUnit test given exception when findByFilter then return empty page")
    void testGivenExceptionWhenFindByFilterThenReturnEmptyPage() {
        final var filter = DeviceFilterDTO.builder()
                .property("state")
                .direction(Direction.DESC)
                .page(0)
                .size(2)
                .paginate(true)
                .build();

        when(entityManager.createNativeQuery(anyString(), eq(DeviceListDTO.DEVICE_LIST_DTO_MAPPING)))
                .thenThrow(new RuntimeException("DB error"));

        final Page<DeviceListDTO> response = service.findByFilter(filter);

        assertNotNull(response, "Response page should not be null");
        assertTrue(response.getContent().isEmpty(), "Page content should be empty when exception occurs");
        assertEquals(0, response.getTotalElements(), "Total elements should be zero when exception occurs");
    }
}
