package use_case.profile.profile_set;

public interface ProfileSetOutputBoundary {
    void prepareSuccessView(ProfileSetOutputData outputData);

    /**
     * Prepares the failure view for the Signup Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
