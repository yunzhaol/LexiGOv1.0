package use_case.studysession;

public class StudySessionOutputData {
    private String pagenumber;
    private String wordtext;
    private boolean reachlast;
    private boolean reachfirst;
    private String totalpage;

    public StudySessionOutputData(String pagenumber,
                                  String wordtext,
                                  boolean reachlast,
                                  boolean reachfirst,
                                  String totalpage) {
        this.pagenumber = pagenumber;
        this.wordtext = wordtext;
        this.reachlast = reachlast;
        this.reachfirst = reachfirst;
        this.totalpage = totalpage;
    }

    public String getPagenumber() {
        return pagenumber;
    }

    public String getWordtext() {
        return wordtext;
    }

    public boolean isReachlast() {
        return reachlast;
    }

    public boolean isReachfirst() {
        return reachfirst;
    }

    public String getTotalpage() {
        return totalpage;
    }

}
