package use_case.profile.profile_set;

public interface ProfileSetInputBoundary {
    /**
     * Executes the profile update use case with the given input data.
     *
     * @param profileSetInputData the input data containing the user's new profile
     *                            settings; must not be {@code null}
     * @throws NullPointerException if {@code profileSetInputData} is {@code null}
     */
    void execute(ProfileSetInputData profileSetInputData);
}
