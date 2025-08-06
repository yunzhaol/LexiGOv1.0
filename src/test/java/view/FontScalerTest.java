package view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FontScalerTest {

    @Test
    public void initAndApplyScale() {
        // These calls should be safe in headless CI environments.
        assertDoesNotThrow(() -> FontScaler.initBaseFonts());
        assertDoesNotThrow(() -> FontScaler.applyScale(1.0f));
        assertDoesNotThrow(() -> FontScaler.applyScale(1.2f));

        float s = FontScaler.getCurrentScale();
        // Scale should be > 0 after initialization
        assertTrue(s > 0.0f);
    }
}