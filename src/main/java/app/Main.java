package app;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;      // You can also use FlatDarkLaf

import java.awt.*;
import java.io.IOException;


/**
 * Application entry point: sets up FlatLaf and then builds the CA architecture.
 */
public class Main {

    public static void main(String[] args) {

        FlatDarkLaf.setup();                             // Use the light theme
        // On Linux/Mac, to enable FlatLaf's custom window decorations:
        JFrame.setDefaultLookAndFeelDecorated(true);      // Use FlatLaf decorations for JFrame
        JDialog.setDefaultLookAndFeelDecorated(true);     // Use FlatLaf decorations for JDialog

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

        SwingUtilities.invokeLater(() -> {
            JFrame application = null;
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            application.pack();
            application.setSize(534,330);
            // application.setSize(437, 270);
            application.setLocationRelativeTo(null);
            application.setVisible(true);

        });
    }
}

