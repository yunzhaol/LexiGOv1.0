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
import view.FontScaler;

/**
 * Application entry point: sets up FlatLaf and then builds the CA architecture.
 */
public class Main {

    public static final int VALUE = 999;
    public static final int RGB = 0x007AFF;
    public static final double VALUE1 = 1.5;
    public static final int G = 250;
    public static final int WIDTH = 841;
    public static final int HEIGHT = 476;
    public static final float FLOAT = 1.2f;
    public static final int LEFT = 8;
    public static final int TOP = 4;

    /**
     * Application entry-point.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Enables the FlatLaf&nbsp;Dark look-and-feel and marks Swing components as
     *       look-and-feel-decorated.</li>
     *   <li>Sets several global UI defaults via {@link javax.swing.UIManager#put(String, Object)}
     *       to achieve a rounded-corner style (arc) and consistent padding, border widths,
     *       focus colors, and background colours.</li>
     *   <li>Enqueues the call to {@code extracted()} on the Event Dispatch Thread (EDT)
     *       using {@link javax.swing.SwingUtilities#invokeLater(Runnable)} to ensure
     *       that all further UI construction occurs on the EDT.</li>
     * </ol>
     *
     * @param args command-line arguments (currently ignored)
     */
    public static void main(String[] args) {

        FlatDarkLaf.setup();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        UIManager.put("Component.focusColor", new Color(RGB));
        UIManager.put("Button.arc", VALUE);
        UIManager.put("Component.arc", VALUE);
        UIManager.put("ProgressBar.arc", VALUE);
        UIManager.put("TextComponent.arc", VALUE);
        UIManager.put("TextComponent.padding", new Insets(TOP, LEFT, TOP, LEFT));
        UIManager.put("Component.borderWidth", VALUE1);
        UIManager.put("Component.focusColor", new Color(RGB));
        UIManager.put("TextComponent.background", new Color(G, G, G));
        UIManager.put("Button.innerFocusWidth", 2);

        SwingUtilities.invokeLater(() -> {
            extracted();
        });
    }

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
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        final JMenuBar menuBar = new JMenuBar();
        final JToggleButton a11y = new JToggleButton("A+");
        a11y.setToolTipText("Large text mode");
        a11y.getAccessibleContext().setAccessibleName("Toggle large text mode");
        a11y.addActionListener(event -> {
            if (a11y.isSelected()) {
                FontScaler.applyScale(FLOAT);
            }
            else {
                FontScaler.applyScale(1.0f);
            }
        }

        );
        menuBar.add(a11y);
        application.setJMenuBar(menuBar);

        application.pack();
        application.setSize(WIDTH, HEIGHT);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
