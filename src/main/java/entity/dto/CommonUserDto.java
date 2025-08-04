package entity.dto;

import java.util.Objects;

/**
 * DTO representing a common (non-security) user.
 *
 * <p>Instances are value-objects: equality and hash-code depend solely on
 * {@code name} and {@code password}.</p>
 */
public final class CommonUserDto extends UserDto {

    /**
     * Creates a new {@code CommonUserDto}.
     *
     * @param name     the user name; must not be {@code null}
     * @param password the plain text password; must not be {@code null}
     */
    public CommonUserDto(String name, String password) {
        super(name, password);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (this == o) {
            result = true;
        }
        else {
            final CommonUserDto that = (CommonUserDto) o;
            result = Objects.equals(getName(), that.getName())
                    && Objects.equals(getPassword(), that.getPassword());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPassword());
    }

    /**
     * Starts a fluent builder for {@code CommonUserDto}.
     *
     * @return a new {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Fluent builder for {@link CommonUserDto}. */
    public static final class Builder {
        private String name;
        private String password;

        /**
         * Sets the username.
         *
         * @param string the name
         * @return {@code this} builder for chaining
         */
        public Builder name(String string) {
            this.name = string;
            return this;
        }

        /**
         * Sets the password.
         *
         * @param pwd the password
         * @return {@code this} builder for chaining
         */
        public Builder password(String pwd) {
            this.password = pwd;
            return this;
        }

        /**
         * Builds a {@link CommonUserDto}.
         *
         * @return an immutable DTO
         */
        public CommonUserDto build() {
            return new CommonUserDto(name, password);
        }
    }
}
