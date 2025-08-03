package entity;

import entity.dto.UserDto;

public interface UserFactory<T extends UserDto> {
    /**
     * Create a User from the provided DTO.
     */
    User create(T dto);
}
