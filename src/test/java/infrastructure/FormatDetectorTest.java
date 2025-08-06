package infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FormatDetectorTest {
    FormatDetector fd;

    @BeforeEach
    public void setUp() {
        fd = new FormatDetector();
    }

    @Test
    public void test_FormatDetector() {
        assertFalse(fd.execute("xxx"));

    }
}
