// 2. Common user DTO
package entity.dto;

import java.util.Objects;

public class CommonUserDto extends UserDto {
    public CommonUserDto(String name, String password) {
        super(name, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommonUserDto)) return false;
        CommonUserDto that = (CommonUserDto) o;
        return Objects.equals(super.getName(), that.getName()) &&
                Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPassword());
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String name, password;
        public Builder name(String n)         { this.name = n; return this; }
        public Builder password(String pwd)   { this.password = pwd; return this; }
        public CommonUserDto build() {
            return new CommonUserDto(name, password);
        }
    }
}
