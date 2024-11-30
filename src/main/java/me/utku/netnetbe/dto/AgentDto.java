package me.utku.netnetbe.dto;

import java.util.UUID;

/**
 * DTO for {@link me.utku.netnetbe.model.Agent}
 */
public record AgentDto(UUID id, String name, String hardwareId, UserDto owner) {
}