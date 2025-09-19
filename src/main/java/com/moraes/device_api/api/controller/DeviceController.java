package com.moraes.device_api.api.controller;

import java.net.URI;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.device_api.api.controller.interfaces.PartialChecks;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.dto.device.DeviceFilterDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.service.interfaces.IDeviceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final IDeviceService service;

    @Operation(summary = "Create a new device", description = "Inserts a new device into the system and returns its generated ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Long> insert(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Device data to be created", required = true, content = @Content(schema = @Schema(implementation = DeviceDTO.class))) @RequestBody @Valid DeviceDTO object) {
        log.debug("Received request to insert device: {}", object);
        final Long id = service.insert(object);
        log.debug("Device inserted with ID: {}", id);
        return ResponseEntity.created(URI.create("/api/v1/devices/%s".formatted(id))).body(id);
    }

    @Operation(summary = "Find device by ID", description = "Fetches a device by its ID and returns a summarized DTO representation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceListDTO.class))),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeviceListDTO> getById(
            @Parameter(description = "ID of the device to be fetched", required = true, example = "1") @PathVariable long id) {

        DeviceListDTO dto = service.getDTOById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Update an existing device", description = "Updates the details of an existing device by its ID. If the device is in use, certain fields cannot be updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Update conflict due to validation rules", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Parameter(description = "ID of the device to be updated", required = true, example = "1") @PathVariable long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated device data", required = true, content = @Content(schema = @Schema(implementation = DeviceDTO.class))) @Valid @RequestBody DeviceDTO dto) {

        service.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Partially update an existing device", description = "Updates only the non-null fields of an existing device by its ID. Fields that are null in the DTO are ignored. If the device is in use, certain fields cannot be updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device partially updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Update conflict due to validation rules", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePartial(
            @Parameter(description = "ID of the device to be partially updated", required = true, example = "1") @PathVariable long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Partial device data. Only non-null fields will be updated.", required = true, content = @Content(schema = @Schema(implementation = DeviceDTO.class))) @Validated(PartialChecks.class) @RequestBody DeviceDTO dto) {

        service.updatePartial(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get a paginated list of devices", description = "Fetches a paginated list of devices based on filter criteria. Supports pagination, sorting, and various filter fields.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of devices fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<DeviceListDTO>> getAll(
            @ParameterObject @Valid DeviceFilterDTO filter) {
        Page<DeviceListDTO> result = service.getAll(filter);
        return ResponseEntity.ok(result);
    }
}
