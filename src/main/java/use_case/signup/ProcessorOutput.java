package use_case.signup;

public class ProcessorOutput {

    private final String errorMessage;
    private final boolean success;

    public ProcessorOutput(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }
}
