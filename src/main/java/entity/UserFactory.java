package entity;

import entity.dto.UserDto;

public interface UserFactory<T extends UserDto> {
    /**
     * Creates a concrete {@link entity.User} instance from the supplied data-transfer object.
     *
     * @param dto the DTO carrying all information required to build a user;
     *            must not be {@code null}
     * @return a fully initialised {@link entity.User}
     */
    User create(T dto);
}
