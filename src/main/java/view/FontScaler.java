package view;

import java.awt.Font;
import java.awt.Window;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * Utility for initializing and scaling the application default font via
 * {@link UIManager}. This class is not instantiable.
 *
 * <p>
 * The scaler caches a base font from the current {@code UIManager} defaults and
 * applies a scale factor to update the global {@code "defaultFont"} key. It
 * also refreshes all existing windows so the new font takes effect immediately.
 * </p>
 */
public final class FontScaler {

    /** Fallback font size used when no UI default is available. */
    private static final int FALLBACK_FONT_SIZE = 19;

    /** Epsilon used to compare scale values. */
    private static final float SCALE_EPSILON = 0.01f;

    /** Cached base font used as the source for scaled fonts. */
    private static FontUIResource baseFont;

    /** The currently-applied scale value. */
    private static float currentScale = 1.0f;

    /** Prevents instantiation. */
    private FontScaler() {
        // utility class
    }

    /**
     * Initializes the cached base font from {@link UIManager} if it has not been
     * initialized yet. The method is idempotent.
     */
    public static void initBaseFonts() {
        if (baseFont == null) {
            Font f = UIManager.getFont("defaultFont");
            if (f == null) {
                f = UIManager.getFont("Label.font");
            }
            if (f == null) {
                f = new Font("SansSerif", Font.PLAIN, FALLBACK_FONT_SIZE);
            }
            baseFont = new FontUIResource(f);
        }
    }

    /**
     * Applies a new scale to the global default font and refreshes all open
     * windows so that the change takes effect immediately.
     *
     * @param scale the new scale factor to apply
     */
    public static void applyScale(final float scale) {
        if (baseFont == null) {
            initBaseFonts();
        }
        if (Math.abs(scale - currentScale) >= SCALE_EPSILON) {
            currentScale = scale;

            final FontUIResource scaled = new FontUIResource(
                    baseFont.deriveFont(baseFont.getSize2D() * scale)
            );
            UIManager.put("defaultFont", scaled);

            for (final Window w : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(w);
                w.invalidate();
                w.validate();
                w.repaint();
            }
        }
    }

    /**
     * Returns the scale value currently applied to the default font.
     *
     * @return the current scale factor
     */
    public static float getCurrentScale() {
        return currentScale;
    }
}
