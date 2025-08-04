package interface_adapter.view_history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import use_case.viewhistory.ViewHistoryEntryData;

public class ViewHistoryState {

    private String currentUser = "";
    private List<ViewHistoryEntryData> sessions = Collections.synchronizedList(new ArrayList<>());
    private int totalSessions;
    private int totalWords;
    private String errorMessage;

    // Getters
    public String getCurrentUser() {
        return currentUser;
    }

    public List<ViewHistoryEntryData> getSessions() {
        return sessions;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // Setters
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Replaces the current session list with the supplied one, defaulting to an
     * empty list when {@code sessions} is {@code null}.
     *
     * @param sessions a list of {@link ViewHistoryEntryData} representing the
     *                 user’s study-history sessions; may be {@code null}
     */
    public void setSessions(List<ViewHistoryEntryData> sessions) {
        if (sessions != null) {
            this.sessions = sessions;
        }
        else {
            this.sessions = new ArrayList<>();
        }
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
