package use_case.profile;

public interface ProfileOutputBoundary {
    /**
     * Prepares the view for a successful profile retrieval.
     *
     * @param outputData the output data containing the user's profile information;
     *                   must not be {@code null}
     * @throws NullPointerException if {@code outputData} is {@code null}
     */
    void prepareSuccessView(ProfileOutputData outputData);
}
