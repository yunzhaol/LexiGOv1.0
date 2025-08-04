package app;

import java.awt.Color;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import view.FontScaler;

/**
 * Application entry point: sets up FlatLaf and then builds the CA architecture.
 */
public class Main {

    // ---- UI constants (eliminate magic numbers) ----
    private static final int ARC_VALUE = 999;
    private static final int ACCENT_RGB = 0x007AFF;
    private static final double BORDER_WIDTH = 1.5;
    private static final int GRAY_250 = 250;
    private static final int FRAME_WIDTH = 841;
    private static final int FRAME_HEIGHT = 476;
    private static final float LARGE_TEXT_SCALE = 1.2f;
    private static final float NORMAL_TEXT_SCALE = 1.0f;
    private static final int INSETS_LEFT_RIGHT = 8;
    private static final int INSETS_TOP_BOTTOM = 4;
    private static final int INNER_FOCUS_WIDTH = 2;

    /**
     * Application entry-point.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Enables the FlatLaf look-and-feel and marks Swing components as
     *       look-and-feel-decorated.</li>
     *   <li>Sets several global UI defaults via {@link UIManager#put(Object, Object)}
     *       to achieve rounded corners, consistent padding, border widths, and colors.</li>
     *   <li>Schedules {@link #extracted()} to run on the Event Dispatch Thread.</li>
     * </ol>
     *
     * @param args command-line arguments (unused)
     */
    public static void main(final String[] args) {
        FlatDarkLaf.setup();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        UIManager.put("Component.focusColor", new Color(ACCENT_RGB));
        UIManager.put("Button.arc", ARC_VALUE);
        UIManager.put("Component.arc", ARC_VALUE);
        UIManager.put("ProgressBar.arc", ARC_VALUE);
        UIManager.put("TextComponent.arc", ARC_VALUE);
        UIManager.put("TextComponent.padding",
                new Insets(INSETS_TOP_BOTTOM, INSETS_LEFT_RIGHT, INSETS_TOP_BOTTOM, INSETS_LEFT_RIGHT));
        UIManager.put("Component.borderWidth", BORDER_WIDTH);
        UIManager.put("Component.focusColor", new Color(ACCENT_RGB));
        UIManager.put("TextComponent.background", new Color(GRAY_250, GRAY_250, GRAY_250));
        UIManager.put("Button.innerFocusWidth", INNER_FOCUS_WIDTH);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                extracted();
            }
        });
    }

    /**
     * Builds the application frame and wires global menu actions.
     *
     * <p>All UI creation occurs on the EDT.</p>
     *
     * @throws RuntimeException if the application frame cannot be built due to an I/O error
     */
    private static void extracted() {
        FontScaler.initBaseFonts();

        final JFrame application;
        try {
            application = new AppBuilder()
                    .addSignupView()
                    .addLoginView()
                    .addLoggedInView()
                    .addStudySessionView()
                    .addWordDetailView()
                    .addSignupUseCase()
                    .addLoginUseCase()
                    .addLogoutUseCase()
                    .addStartCheckInUseCase()
                    .addStudySessionUseCase()
                    .addWordDetaiUsecase()
                    .addFinishCheckInUseCase()
                    .build();
        }
        catch (final IOException exception) {
            throw new RuntimeException(exception);
        }

        final JMenuBar menuBar = new JMenuBar();
        final JToggleButton a11y = new JToggleButton("A+");
        final JToggleButton a12y = new JToggleButton("Day/Night");
        a11y.setToolTipText("Large text mode");
        a12y.setToolTipText("Day Mode / Night Mode");
        a11y.getAccessibleContext().setAccessibleName("Toggle large text mode");
        a12y.getAccessibleContext().setAccessibleName("Toggle D/N mode");

        a11y.addActionListener(event -> {
            if (a11y.isSelected()) {
                FontScaler.applyScale(LARGE_TEXT_SCALE);
            }
            else {
                FontScaler.applyScale(NORMAL_TEXT_SCALE);
            }
        });

        a12y.addActionListener(event -> {
            toggleDarkMode(application);
        });

        menuBar.add(a11y);
        menuBar.add(a12y);
        application.setJMenuBar(menuBar);

        application.pack();
        application.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }

    /**
     * Toggles between light and dark FlatLaf themes and refreshes the UI.
     *
     * @param frame the top-level frame whose UI tree will be updated
     */
    private static void toggleDarkMode(final JFrame frame) {
        final boolean isCurrentlyDark = FlatLaf.isLafDark();
        if (isCurrentlyDark) {
            FlatLightLaf.setup();
        }
        else {
            FlatDarkLaf.setup();
        }
        SwingUtilities.updateComponentTreeUI(frame);
    }
}
