package me.utku.netnetbe.dto;

import me.utku.netnetbe.enums.Role;

import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link me.utku.netnetbe.model.User}
 */
public record UserDto(UUID id, String username, Set<Role> authorities) {
}