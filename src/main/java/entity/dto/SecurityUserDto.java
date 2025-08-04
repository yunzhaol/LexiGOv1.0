package entity.dto;

import java.util.Objects;

/**
 * DTO representing a user that stores a security question / answer pair
 * in addition to standard credentials.
 *
 * <p>Instances are value-objects: equality and hash-code depend only on
 * {@code name}, {@code password}, {@code securityQuestion} and
 * {@code securityAnswer}.</p>
 */
public final class SecurityUserDto extends UserDto {

    private final String securityQuestion;
    private final String securityAnswer;

    /**
     * Creates a fully initialised {@code SecurityUserDto}.
     *
     * @param name             the user name (never {@code null})
     * @param password         the plain-text password (never {@code null})
     * @param securityQuestion the security question (never {@code null})
     * @param securityAnswer   the security answer   (never {@code null})
     */
    public SecurityUserDto(
            String name,
            String password,
            String securityQuestion,
            String securityAnswer) {
        super(name, password);
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (this == obj) {
            isEqual = true;
        }
        else if (obj instanceof SecurityUserDto) {
            final SecurityUserDto that = (SecurityUserDto) obj;
            isEqual =
                    Objects.equals(getName(), that.getName())
                            && Objects.equals(getPassword(), that.getPassword())
                            && Objects.equals(getSecurityQuestion(), that.getSecurityQuestion())
                            && Objects.equals(getSecurityAnswer(), that.getSecurityAnswer());
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getName(),
                getPassword(),
                getSecurityQuestion(),
                getSecurityAnswer());
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * Starts a fluent builder for {@code SecurityUserDto}.
     *
     * @return a new {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Fluent builder for {@link SecurityUserDto}. */
    public static final class Builder {

        private String name;
        private String password;
        private String question;
        private String answer;

        /**
         * Sets the user name.
         *
         * @param userName the name to set
         * @return this builder for chaining
         */
        public Builder name(String userName) {
            this.name = userName;
            return this;
        }

        /**
         * Sets the password.
         *
         * @param userPassword the password to set
         * @return this builder for chaining
         */
        public Builder password(String userPassword) {
            this.password = userPassword;
            return this;
        }

        /**
         * Sets the security question.
         *
         * @param secQuestion the question to set
         * @return this builder for chaining
         */
        public Builder question(String secQuestion) {
            this.question = secQuestion;
            return this;
        }

        /**
         * Sets the security answer.
         *
         * @param secAnswer the answer to set
         * @return this builder for chaining
         */
        public Builder answer(String secAnswer) {
            this.answer = secAnswer;
            return this;
        }

        /**
         * Builds an immutable {@link SecurityUserDto}.
         *
         * @return a fully-initialised DTO
         */
        public SecurityUserDto build() {
            return new SecurityUserDto(name, password, question, answer);
        }
    }
}
