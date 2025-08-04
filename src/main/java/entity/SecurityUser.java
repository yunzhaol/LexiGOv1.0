package entity;

/**
 * A simple implementation of the User interface.
 */
public class SecurityUser implements User {

    private final String name;
    private final String password;
    private final String securityQuestion;
    private final String securityAnswer;

    public SecurityUser(String name, String password,
                        String securityQuestion, String securityAnswer) {
        this.name = name;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }
}
