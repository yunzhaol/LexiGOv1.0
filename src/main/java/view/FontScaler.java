package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.FontUIResource;


public final class FontScaler {
    private static FontUIResource baseFont;   // 记录基线字体
    private static float currentScale = 1.0f;

    private FontScaler() {}

    /** 在设置好 LAF 后、构建任何界面之前 调用一次 */
    public static void initBaseFonts() {
        if (baseFont != null) return;

        // FlatLaf 支持从 "defaultFont" 读/写全局默认字体；若取不到则退到 Label.font
        Font f = UIManager.getFont("defaultFont");
        if (f == null) f = UIManager.getFont("Label.font");
        if (f == null) f = new Font("SansSerif", Font.PLAIN, 19); // 兜底
        baseFont = new FontUIResource(f);
    }

    /** 将全局默认字体按比例缩放，并刷新所有已打开窗口 */
    public static void applyScale(float scale) {
        if (baseFont == null) initBaseFonts();
        if (Math.abs(scale - currentScale) < 0.01f) return;
        currentScale = scale;

        FontUIResource scaled = new FontUIResource(
                baseFont.deriveFont(baseFont.getSize2D() * scale)
        );
        UIManager.put("defaultFont", scaled); // FlatLaf 会把它应用到所有组件

        for (Window w : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(w);
            w.invalidate();
            w.validate();
            w.repaint();
        }
    }

    public static float getCurrentScale() {
        return currentScale;
    }
}
