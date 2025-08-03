package app;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.*;
import java.io.IOException;
import view.FontScaler;

/**
 * Application entry point: sets up FlatLaf and then builds the CA architecture.
 */
public class Main {

    public static void main(String[] args) {

        FlatDarkLaf.setup();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        UIManager.put("Component.focusColor", new Color(0x007AFF));
        UIManager.put("Button.arc", 999);
        UIManager.put("Component.arc", 999);
        UIManager.put("ProgressBar.arc", 999);
        UIManager.put("TextComponent.arc", 999);
        UIManager.put("TextComponent.padding", new Insets(4, 8, 4, 8));
        UIManager.put("Component.borderWidth", 1.5);
        UIManager.put("Component.focusColor", new Color(0x007ACC));
        UIManager.put("TextComponent.background", new Color(250, 250, 250));
        UIManager.put("Button.innerFocusWidth", 2);

        SwingUtilities.invokeLater(() -> {
            FontScaler.initBaseFonts();

            JFrame application;
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

            JMenuBar menuBar = new JMenuBar();
            JToggleButton a11y = new JToggleButton("A+");
            a11y.setToolTipText("Large text mode");
            a11y.getAccessibleContext().setAccessibleName("Toggle large text mode");
            a11y.addActionListener(e ->
                    FontScaler.applyScale(a11y.isSelected() ? 1.2f : 1.0f)
            );
            menuBar.add(a11y);
            application.setJMenuBar(menuBar);

            application.pack();
//            application.setSize(534, 330);
            application.setSize(841, 476);
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}