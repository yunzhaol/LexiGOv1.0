package use_case.rank;

public interface RankInputBoundary {
    /**
     * Executes the ranking use case with the provided input data.
     *
     * @param rankInputData the input data containing parameters
     *                      such as the target username and result limit;
     *                      must not be {@code null}
     * @throws NullPointerException     if {@code rankInputData} is {@code null}
     * @throws IllegalArgumentException if any field of {@code rankInputData}
     *                                  is invalid (e.g., blank username or negative limit)
     */
    void execute(RankInputData rankInputData);
}
