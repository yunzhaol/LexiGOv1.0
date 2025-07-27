package use_case.rank;

public class RankEntryData {
    private int rank;

    private int score;
    private String username;

    public RankEntryData(int rank, String username, int score) {
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
