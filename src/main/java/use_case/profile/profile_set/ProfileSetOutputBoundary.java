package use_case.profile.profile_set;

public interface ProfileSetOutputBoundary {
    /**
     * Prepares the view for a successful profile update.
     *
     * @param outputData the output data containing confirmation or updated profile details;
     *                   must not be {@code null}
     * @throws NullPointerException if {@code outputData} is {@code null}
     */
    void prepareSuccessView(ProfileSetOutputData outputData);

    /**
     * Prepares the failure view for the Signup Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
