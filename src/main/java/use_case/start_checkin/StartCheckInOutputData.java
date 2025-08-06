package use_case.start_checkin;

public final class StartCheckInOutputData {
    private final boolean useCaseFailed;
    private final String totalpage;
    private final String username;

    public StartCheckInOutputData(String totalpage, boolean useCaseFailed, String username) {
        this.useCaseFailed = useCaseFailed;
        this.totalpage = totalpage;
        this.username = username;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    public String getTotalpage() {
        return totalpage;
    }

    public String getUsername() {
        return username;
    }
}
