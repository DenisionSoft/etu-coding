package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class CubeRotationFrame extends JFrame {
    final int WIDTH = 500;
    final int HEIGHT = 200;

    CubeRotationFrame() {
        this.setTitle("Cube Rotation");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);

        JSlider xSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
        xSlider.setMajorTickSpacing(90);
        xSlider.setMinorTickSpacing(30);
        xSlider.setPaintTicks(true);
        xSlider.setOpaque(false);
        xSlider.setFocusable(false);
        this.add(xSlider, BorderLayout.NORTH);

        JSlider ySlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
        ySlider.setMajorTickSpacing(90);
        ySlider.setMinorTickSpacing(30);
        ySlider.setPaintTicks(true);
        ySlider.setOpaque(false);
        ySlider.setFocusable(false);
        this.add(ySlider, BorderLayout.CENTER);

        JSlider zSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
        zSlider.setMajorTickSpacing(90);
        zSlider.setMinorTickSpacing(30);
        zSlider.setPaintTicks(true);
        zSlider.setOpaque(false);
        zSlider.setFocusable(false);
        this.add(zSlider, BorderLayout.SOUTH);

        this.setVisible(true);
    }

}
