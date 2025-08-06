package infrastructure;

public class FormatDetector implements use_case.start_checkin.FormatDetector {
    public boolean execute(String length) {

        boolean result = true;
        int requestedLength = 0;
        // range validation
        try {
            requestedLength = Integer.parseInt(length);
        }
        catch (NumberFormatException ex) {
            result = false;
        }

        if (requestedLength <= 0) {
            result = false;
        }
        return result;
    }
}
