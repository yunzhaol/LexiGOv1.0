package use_case.rank;

public final class UserScore {
    private final int rank;
    private final int score;
    private final String username;

    public UserScore(int rank, String username, int score) {
        this.rank = rank;
        this.username = username;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

}
