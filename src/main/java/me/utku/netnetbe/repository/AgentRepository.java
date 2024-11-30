package me.utku.netnetbe.repository;

import me.utku.netnetbe.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AgentRepository extends JpaRepository<Agent, UUID> {
    Optional<Agent> findByHardwareId(String hardwareId);
}
