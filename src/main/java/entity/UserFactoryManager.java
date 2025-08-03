package entity;

import entity.dto.UserDto;
/**
 * A manager that returns the correct factory for each UserType.
 */
public class UserFactoryManager {

    /**
     * @param type  which kind of UserFactory to return
     * @return      a UserFactory whose generic parameter extends UserDto
     */
    public UserFactory<? extends UserDto> getFactory(UserType type) {
        switch (type) {
            case COMMON:
                return new CommonUserFactory();
            case SECURITY:
                return new SecurityUserFactory();
            default:
                throw new IllegalArgumentException("Unsupported UserType: " + type);
        }
    }
}

