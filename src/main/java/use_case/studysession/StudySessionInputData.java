package use_case.studysession;

public class StudySessionInputData {
    private String pagenumber;
    private String totalpage;

    public StudySessionInputData(String pagenumber, String totalpage) {
        this.pagenumber = pagenumber;
        this.totalpage = totalpage;
    }

    public String getPagenumber() {
        return pagenumber;
    }

    public String getTotalpage() {
        return totalpage;
    }
}
