package com.moraes.device_api.api.model;

import java.time.LocalDateTime;

import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SqlResultSetMapping(name = DeviceListDTO.DEVICE_LIST_DTO_MAPPING, classes = {
        @ConstructorResult(targetClass = DeviceListDTO.class, columns = {
                @ColumnResult(name = "id", type = Long.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "brand", type = String.class),
                @ColumnResult(name = "state", type = String.class),
                @ColumnResult(name = "creationTime", type = LocalDateTime.class)
        }) })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "device", schema = "device_api")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DeviceStateEnum state;

    @Column(name = "creation_time", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @PrePersist
    public void prePersist() {
        creationTime = LocalDateTime.now();
        state = state == null ? DeviceStateEnum.AVAILABLE : state;
    }
}
