package interface_adapter.studysession;

public final class StudySessionState {
    private String word = "Welcome";
    private String pagenumber = "0";
    private String totalpage;
    private String username = "";
    private boolean reachlast;
    private boolean reachfirst = true;

    public StudySessionState(String word, String pagenumber,
                             String totalpage, String username,
                             boolean reachlast, boolean reachfirst) {
        this.word = word;
        this.pagenumber = pagenumber;
        this.totalpage = totalpage;
        this.username = username;
        this.reachlast = reachlast;
        this.reachfirst = reachfirst;
    }

    public StudySessionState() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(String pagenumber) {
        this.pagenumber = pagenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(String totalpage) {
        this.totalpage = totalpage;
    }

    public boolean isReachlast() {
        return reachlast;
    }

    public void setReachlast(boolean reachlast) {
        this.reachlast = reachlast;
    }

    public boolean isReachfirst() {
        return reachfirst;
    }

    public void setReachfirst(boolean reachfirst) {
        this.reachfirst = reachfirst;
    }
}
