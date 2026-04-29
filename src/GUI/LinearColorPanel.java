package GUI;

import javax.swing.*;
import java.awt.*;

public class LinearColorPanel extends JPanel {

    private Color startColor = Color.BLUE;
    private Color endColor = Color.WHITE;
    private boolean vertical = true;

    public void setStartColor(Color c) {
        startColor = c;
    }

    public void setEndColor(Color c) {
        endColor = c;
    }

    public void setGradientDirection(boolean vertical) {
        this.vertical = vertical;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gp = vertical
                ? new GradientPaint(0, 0, startColor, 0, getHeight(), endColor)
                : new GradientPaint(0, 0, startColor, getWidth(), 0, endColor);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}