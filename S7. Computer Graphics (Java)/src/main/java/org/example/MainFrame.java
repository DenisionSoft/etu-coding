package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    final int WIDTH = 1200;
    final int HEIGHT = 1000;
    final int EDITOR_WIDTH = 200;
    final int VIEWPORT_WIDTH = 1000;
    final int STEP = 10;

    EditorPanel editorPanel;
    ViewportPanel viewportPanel;

    MainFrame() {
        this.setTitle("Graphics Viewer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);

        viewportPanel = new ViewportPanel(VIEWPORT_WIDTH, HEIGHT, STEP);
        this.add(viewportPanel, BorderLayout.EAST);

        editorPanel = new EditorPanel(EDITOR_WIDTH, HEIGHT, viewportPanel);
        this.add(editorPanel, BorderLayout.WEST);

        this.setVisible(true);
    }

}