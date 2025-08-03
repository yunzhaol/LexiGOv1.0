// 2. Common user DTO
package entity.dto;

public class CommonUserDto extends UserDto {
    public CommonUserDto(String name, String password) {
        super(name, password);
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
