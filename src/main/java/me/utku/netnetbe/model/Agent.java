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

    public Agent(String name, String hardwareId) {
        this.name = name;
        this.hardwareId = hardwareId;
    }

    public Agent() {
    }
}
