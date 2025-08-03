// 1. Abstract base DTO
package entity.dto;

public abstract class UserDto {
    private final String name;
    private final String password;

    protected UserDto(String name, String password) {
        if (name == null || password == null) {
            throw new IllegalArgumentException("name and password must be non-null");
        }
        this.name     = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


}
