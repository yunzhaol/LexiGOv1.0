package use_case.profile;

public interface ProfileInputBoundary {
    /**
     * Executes the profile retrieval use case with the given input data.
     *
     * @param profileInputData the input data containing the user's identifier;
     *                         must not be {@code null}
     * @throws NullPointerException if {@code profileInputData} is {@code null}
     */
    void execute(ProfileInputData profileInputData);
}
