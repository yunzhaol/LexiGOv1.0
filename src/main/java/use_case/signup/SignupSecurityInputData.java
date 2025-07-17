package use_case.signup;

public class SignupSecurityInputData extends SignupInputData {

    private final String securityQuestion;
    private final String securityAnswer;

    public SignupSecurityInputData(String username,
                           String password,
                           String repeatPassword,
                           String securityQuestion,
                           String securityAnswer
    ) {
        super(username, password, repeatPassword);
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }
}
