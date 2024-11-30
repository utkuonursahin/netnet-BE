package me.utku.netnetbe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Agent extends BaseEntity {
    private String name;
    private UUID hardwareId;
    @ManyToOne
    private User owner;

    public Agent(String name, UUID hardwareId, User owner) {
        this.name = name;
        this.hardwareId = hardwareId;
        this.owner = owner;
    }

    public Agent() {
    }
}
