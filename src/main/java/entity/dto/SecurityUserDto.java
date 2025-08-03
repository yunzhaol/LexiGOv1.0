package entity.dto;

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
