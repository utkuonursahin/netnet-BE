package me.utku.netnetbe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Agent extends BaseEntity {
    private String name;
    @Column(unique = true)
    private String hardwareId;
    private int cpuCores;
    private int ram;
}
