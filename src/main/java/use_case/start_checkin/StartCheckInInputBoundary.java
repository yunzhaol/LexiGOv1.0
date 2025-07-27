package use_case.start_checkin;

public interface StartCheckInInputBoundary {
    void execute(StartCheckInInputData inputData);

    void switchToDeckView();
}
