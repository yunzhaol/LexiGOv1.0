package entity;

import entity.dto.UserDto;

/**
 * Returns the appropriate {@link UserFactory} implementation for each supported {@link UserType}.
 */
public class UserFactoryManager {

    /**
     * Returns a concrete {@link UserFactory} that can build users of the specified type.
     *
     * @param type which kind of factory to retrieve; must not be {@code null}
     * @return a {@code UserFactory} whose generic parameter extends {@link UserDto}
     * @throws IllegalArgumentException if the supplied {@code type} is unsupported
     */
    public UserFactory<? extends UserDto> getFactory(UserType type) {
        UserFactory<? extends UserDto> factory = null;
        switch (type) {
            case COMMON:
                factory = new CommonUserFactory();
                break;
            case SECURITY:
                factory = new SecurityUserFactory();
                break;
            default:
                throw new IllegalArgumentException("Unsupported UserType: " + type);
        }
        return factory;
    }
}

