package entity.dto;

import java.util.Objects;

public class SecurityUserDto extends UserDto {
    private final String securityQuestion;
    private final String securityAnswer;

    public SecurityUserDto(
            String name,
            String password,
            String securityQuestion,
            String securityAnswer
    ) {
        super(name, password);
        this.securityQuestion = securityQuestion;
        this.securityAnswer   = securityAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityUserDto)) return false;
        SecurityUserDto that = (SecurityUserDto) o;
        return getName().equals(that.getName()) &&
                getPassword().equals(that.getPassword()) &&
                getSecurityQuestion().equals(that.getSecurityQuestion()) &&
                getSecurityAnswer().equals(that.getSecurityAnswer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPassword(), getSecurityQuestion(), getSecurityAnswer());
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String name, password, question, answer;
        public Builder name(String n)          { this.name = n; return this; }
        public Builder password(String p)      { this.password = p; return this; }
        public Builder question(String q)      { this.question = q; return this; }
        public Builder answer(String a)        { this.answer = a; return this; }
        public SecurityUserDto build() {
            return new SecurityUserDto(name, password, question, answer);
        }
    }
}
