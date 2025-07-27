package use_case.start_checkin;

public interface StartCheckInOutputBoundary {
    void prepareSuccessView(StartCheckInOutputData outputData);

    void prepareFailView(String errorMessage);

    void switchToDeckView();
}
