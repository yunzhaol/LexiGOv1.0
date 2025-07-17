package app;

import javax.swing.JFrame;

import com.formdev.flatlaf.FlatDarkLaf;      // You can also use FlatDarkLaf
import javax.swing.*;
import java.awt.*;

/**
 * Application entry point: sets up FlatLaf and then builds the CA architecture.
 */
public class Main {

    public static void main(String[] args) {
        /* ---------- 1. Set up global Look-and-Feel ---------- */

        FlatDarkLaf.setup();                             // Use the light theme
        // On Linux/Mac, to enable FlatLaf's custom window decorations:
        JFrame.setDefaultLookAndFeelDecorated(true);      // Use FlatLaf decorations for JFrame
        JDialog.setDefaultLookAndFeelDecorated(true);     // Use FlatLaf decorations for JDialog

        /* ---------- 2. Optional UI defaults ---------- */
        UIManager.put("Component.focusColor", new Color(0x007AFF));
        UIManager.put( "Button.arc", 999 );
        UIManager.put( "Component.arc", 999 );
        UIManager.put( "ProgressBar.arc", 999 );
        UIManager.put( "TextComponent.arc", 999 );               // Set rounded corners (default is 5)
        UIManager.put("TextComponent.padding", new Insets(4, 8, 4, 8));
        UIManager.put("Component.borderWidth", 1.5);
        UIManager.put("Component.focusColor", new Color(0x007ACC));
        UIManager.put("TextComponent.background", new Color(250, 250, 250));
        UIManager.put("Button.innerFocusWidth", 2);

        /* ---------- 3. Build and display the main window on the EDT ---------- */
        SwingUtilities.invokeLater(() -> {
            JFrame application = new AppBuilder()
                    .addLoginView()
                    .addSignupView()
                    .addLoggedInView()
                    .addSignupUseCase()
                    .addLoginUseCase()
                    .addChangePasswordUseCase()
                    .addLogoutUseCase()
                    .build();

            application.pack();

            application.setSize(437, 270); // Center the window
            application.setLocationRelativeTo(null);
            application.setVisible(true);

        });
    }
}

