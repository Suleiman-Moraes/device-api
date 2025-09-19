package com.moraes.device_api.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;

import com.moraes.device_api.api.exception.ResourceNotFoundException;
import com.moraes.device_api.api.exception.ValidException;
import com.moraes.device_api.api.mapper.IDeviceMapper;
import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.dto.device.DeviceFilterDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;
import com.moraes.device_api.api.repository.IDeviceCustomRepository;
import com.moraes.device_api.api.repository.IDeviceRepository;
import com.moraes.device_api.mock.MockDevice;
import com.moraes.device_api.mock.MockDeviceDTO;
import com.moraes.device_api.mock.MockDeviceListDTO;

class DeviceServiceTest {

    @Spy
    @InjectMocks
    private DeviceService service;

    @Mock
    private IDeviceRepository repository;

    @Mock
    private IDeviceCustomRepository customRepository;

    @Mock
    private IDeviceMapper mapper;

    private MockDevice input;
    private MockDeviceDTO mockDeviceDTO;
    private MockDeviceListDTO mockDeviceListDTO;

    private final Long id = 1l;
    private Device entity;

    @BeforeEach
    void setUp() {
        input = new MockDevice();
        mockDeviceDTO = new MockDeviceDTO();
        mockDeviceListDTO = new MockDeviceListDTO();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setId(id);
    }

    @Test
    @DisplayName("JUnit test given DeviceDTO when insert then return Device ID")
    void testGivenDeviceDTOWhenToEntityThenParseToDevice() {
        final var dto = mockDeviceDTO.mockEntity(1);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        final Long response = service.insert(dto);

        assertNotNull(response, "ID should not be null");
        assertEquals(id, response, "ID should be equal " + id);
    }

    @Test
    @DisplayName("JUnit test given Device ID when getById then return Device")
    void testGivenDeviceIDWhenGetByIdThenReturnDevice() {
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        final Device response = service.getById(id);

        assertNotNull(response, "Device should not be null");
        assertEquals(id, response.getId(), "ID should be equal " + id);
        assertEquals(entity.getName(), response.getName(), "Name should be equal " + entity.getName());
        assertEquals(entity.getBrand(), response.getBrand(), "Brand should be equal " + entity.getBrand());
        assertEquals(entity.getState(), response.getState(), "State should be equal " + entity.getState());
        assertEquals(entity.getCreationTime(), response.getCreationTime(),
                "Creation Time should be equal " + entity.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test given Device ID invalid when getById then throw ResourceNotFoundException")
    void testGivenDeviceIDInvalidWhenGetByIdThenThrowResourceNotFoundException() {
        when(repository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(id);
        });
        assertEquals("Device not found with ID: " + id, ex.getMessage(), "Message should be equal");
    }

    @Test
    @DisplayName("JUnit test given Device ID when getDTOById then return DeviceDTO")
    void testGivenDeviceIDWhenGetDTOByIdThenReturnDeviceDTO() {
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toListDTO(entity)).thenReturn(mockDeviceListDTO.mockEntity(1));

        final DeviceListDTO response = service.getDTOById(id);

        assertNotNull(response, "DeviceDTO should not be null");
        assertEquals(entity.getName(), response.getName(), "Name should be equal " + entity.getName());
        assertEquals(entity.getBrand(), response.getBrand(), "Brand should be equal " + entity.getBrand());
        assertEquals(entity.getState(), response.getState(), "State should be equal " + entity.getState());
    }

    @Test
    @DisplayName("JUnit test given Device ID and DeviceDTO when update then update Device")
    void testGivenDeviceIDAndDeviceDTOWhenUpdateThenUpdateDevice() {
        final var dto = mockDeviceDTO.mockEntity(1);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> service.update(id, dto), "Should not throw exception");
        InOrder inOrder = inOrder(service, mapper, repository);

        inOrder.verify(service).validateBeforeUpdate(entity, dto);
        inOrder.verify(mapper).updateFromDeviceDTO(dto, entity);
        inOrder.verify(repository).save(entity);

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("JUnit test given Device ID when delete then delete Device")
    void testGivenDeviceIDWhenDeleteThenDeleteDevice() {
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> service.delete(id), "Should not throw exception");
    }

    @Test
    @DisplayName("JUnit test given Device ID when delete with device in use then throw ValidException")
    void testGivenDeviceIDWhenDeleteWithDeviceInUseThenThrowValidException() {
        entity.setState(DeviceStateEnum.IN_USE);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        final ValidException exception = assertThrows(ValidException.class, () -> {
            service.delete(id);
        }, "Should throw ValidException when device is in use");
        assertNotNull(exception.getErrs(), "Errors should not be null");
        assertEquals(1, exception.getErrs().size(), "Errors size should be equal 1");
        assertEquals("Device in use cannot be deleted.", exception.getErrs().get(0), "Error message should be equal");
    }

    @ParameterizedTest(name = "{index} => entity={0}, dto={1}, errorsSize={2}, description={3}")
    @MethodSource("provideParametersValidateBeforeUpdateShouldThrow")
    @DisplayName("JUnit test given any invalid Device and invalid DeviceDTO when validateBeforeUpdate then Does Not Throw Exception")
    void testGivenAnyInvalidDeviceAndInvalidDeviceDTOWhenValidateBeforeUpdateThenShouldThrowException(Device entity,
            DeviceDTO dto, int errorsSize, String description) {
        final ValidException exception = assertThrows(ValidException.class, () -> {
            service.validateBeforeUpdate(entity, dto);
        }, "Should throw ValidException when " + description);
        assertNotNull(exception.getErrs(), "Errors should not be null");
        assertEquals(errorsSize, exception.getErrs().size(), "Errors size should be equal " + errorsSize);
    }

    @ParameterizedTest(name = "{index} => entity={0}, dto={1}, description={2}")
    @MethodSource("provideParametersValidateBeforeUpdateDoesNotThrow")
    @DisplayName("JUnit test given any valid Device and valid DeviceDTO when validateBeforeUpdate then Does Not Throw Exception")
    void testGivenAnyValidDeviceAndValidDeviceDTOWhenValidateBeforeUpdateThenDoesNotThrowException(Device entity,
            DeviceDTO dto, String description) {
        assertDoesNotThrow(() -> service.validateBeforeUpdate(entity, dto),
                "Should not throw exception when " + description);
    }

    @Test
    @DisplayName("JUnit test given DeviceFilterDTO when getAll then return Page of DeviceListDTO")
    void testGivenDeviceFilterDTOWhenGetAllThenReturnPageOfDeviceListDTO() {
        final DeviceFilterDTO filter = DeviceFilterDTO.builder().build();

        when(customRepository.findByFilter(filter)).thenReturn(Page.empty());

        final Page<DeviceListDTO> response = service.getAll(filter);

        assertNotNull(response, "Page should not be null");
        assertTrue(response.isEmpty(), "Page should be empty");
    }

    @Test
    @DisplayName("JUnit test given Device ID and DeviceDTO when updatePartial then updated Device")
    void testGivenDeviceIDAndDeviceDTOWhenUpdatePartialThenUpdatedDevice() {
        final var dto = mockDeviceDTO.mockEntity(1);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> service.updatePartial(id, dto), "Should not throw exception");
        InOrder inOrder = inOrder(service, mapper, repository);

        inOrder.verify(service).validateBeforeUpdate(entity, dto);
        inOrder.verify(mapper).updatePartialFromDeviceDTO(dto, entity);
        inOrder.verify(repository).save(entity);

        inOrder.verifyNoMoreInteractions();
    }

    // void validateDevicesByParam(String param, final List<Device> devices)
    @Test
    @DisplayName("JUnit test given DeviceStateEnum and empty devices list when validateDevicesByParam then throw ResourceNotFoundException")
    void testGivenDeviceStateEnumAndEmptyDevicesListWhenValidateDevicesByParamThenThrowResourceNotFoundException() {
        final var state = DeviceStateEnum.AVAILABLE;
        final List<Device> devices = List.of();

        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.validateDevicesByParam(state.name(), devices);
        }, "Should throw ResourceNotFoundException when devices list is empty");
        assertEquals("No devices found with param: " + state.name(), exception.getMessage(),
                "Message should be equal");
    }

    @Test
    @DisplayName("JUnit test given random param and devices list when validateDevicesByParam then does not throw exception")
    void testGivenRandomParamAndDevicesListWhenValidateDevicesByParamThenDoesNotThrowException() {
        final var param = "randomParam";
        final List<Device> devices = List.of(input.mockEntity(1));

        assertDoesNotThrow(() -> service.validateDevicesByParam(param, devices), "Should not throw exception");
    }

    private static Stream<Arguments> provideParametersValidateBeforeUpdateShouldThrow() {
        final DeviceStateEnum state = DeviceStateEnum.IN_USE;
        final String name = "Device Name";
        final String brand = "Device Brand";
        final Device entityInUse = Device.builder()
                .state(state)
                .name(name)
                .brand(brand)
                .build();
        final DeviceDTO dtoChangingName = DeviceDTO.builder()
                .state(state)
                .name("Different Name")
                .brand(brand)
                .build();
        final DeviceDTO dtoChangingBrand = DeviceDTO.builder()
                .state(state)
                .name(name)
                .brand("Different Brand")
                .build();
        final DeviceDTO dtoChangeingBoth = DeviceDTO.builder()
                .state(state)
                .name("Different Name")
                .brand("Different Brand")
                .build();

        return Stream.of(Arguments.of(entityInUse, dtoChangingName, 1, "Name changed while in use"),
                Arguments.of(entityInUse, dtoChangingBrand, 1, "Brand changed while in use"),
                Arguments.of(entityInUse, dtoChangeingBoth, 2, "Both name and brand changed while in use"));
    }

    private static Stream<Arguments> provideParametersValidateBeforeUpdateDoesNotThrow() {
        final String name = "Device Name";
        final String brand = "Device Brand";
        final Device entityAvailable = Device.builder().state(DeviceStateEnum.AVAILABLE).build();
        final Device entityInactive = Device.builder().state(DeviceStateEnum.INACTIVE).build();
        final Device entityInUse = Device.builder()
                .state(DeviceStateEnum.IN_USE)
                .name(name)
                .brand(brand)
                .build();
        final DeviceDTO dtoInUse = DeviceDTO.builder()
                .state(DeviceStateEnum.IN_USE)
                .name(name)
                .brand(brand)
                .build();
        final DeviceDTO dtoNotInUse = DeviceDTO.builder().state(DeviceStateEnum.AVAILABLE).build();

        return Stream.of(Arguments.of(entityAvailable, dtoInUse, "Entity is available"),
                Arguments.of(entityInactive, dtoNotInUse, "Entity is inactive"),
                Arguments.of(entityInUse, dtoInUse, "Entity is in use but name and brand are the same"));
    }
}
