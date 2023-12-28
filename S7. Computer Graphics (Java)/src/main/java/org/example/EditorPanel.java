package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditorPanel extends JPanel {
    private ViewportPanel viewportPanel;

    EditorPanel(int w, int h, ViewportPanel vp) {
        this.viewportPanel = vp;
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));

        JCheckBox gridCheckBox = new JCheckBox("Grid");
        gridCheckBox.setPreferredSize(new Dimension(50, 15));
        gridCheckBox.setForeground(Color.WHITE);
        gridCheckBox.setOpaque(false);
        gridCheckBox.setFocusable(false);

        gridCheckBox.setSelected(true);
        this.add(gridCheckBox);

        JCheckBox gridLabelsCheckBox = new JCheckBox("Grid Labels");
        gridLabelsCheckBox.setPreferredSize(new Dimension(100, 15));
        gridLabelsCheckBox.setForeground(Color.WHITE);
        gridLabelsCheckBox.setOpaque(false);
        gridLabelsCheckBox.setFocusable(false);

        gridLabelsCheckBox.setSelected(true);
        this.add(gridLabelsCheckBox);

        JCheckBox pointLabelsCheckBox = new JCheckBox("Pt. Labels");
        pointLabelsCheckBox.setPreferredSize(new Dimension(90, 15));
        pointLabelsCheckBox.setForeground(Color.WHITE);
        pointLabelsCheckBox.setOpaque(false);
        pointLabelsCheckBox.setFocusable(false);

        pointLabelsCheckBox.setSelected(true);
        this.add(pointLabelsCheckBox);

        JCheckBox pointCoordsCheckBox = new JCheckBox("Pt. Coords");
        pointCoordsCheckBox.setPreferredSize(new Dimension(90, 15));
        pointCoordsCheckBox.setForeground(Color.WHITE);
        pointCoordsCheckBox.setOpaque(false);
        pointCoordsCheckBox.setFocusable(false);

        pointCoordsCheckBox.setSelected(false);
        this.add(pointCoordsCheckBox);

        JCheckBox polygonPointsCheckBox = new JCheckBox("Polygon Points");
        polygonPointsCheckBox.setPreferredSize(new Dimension(170, 15));
        polygonPointsCheckBox.setForeground(Color.WHITE);
        polygonPointsCheckBox.setOpaque(false);
        polygonPointsCheckBox.setFocusable(false);

        polygonPointsCheckBox.setSelected(false);
        this.add(polygonPointsCheckBox);

        JLabel dividerLabel = new JLabel();
        dividerLabel.setPreferredSize(new Dimension(170, 15));
        dividerLabel.setFocusable(false);
        this.add(dividerLabel);

        JTextField stepTextField = new JTextField("10");
        stepTextField.setPreferredSize(new Dimension(50, 20));
        stepTextField.setForeground(Color.WHITE);
        stepTextField.setBackground(Color.DARK_GRAY);
        stepTextField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.add(stepTextField);

        JCheckBox useStepTextFieldCheckBox = new JCheckBox("Use field");
        useStepTextFieldCheckBox.setPreferredSize(new Dimension(90, 15));
        useStepTextFieldCheckBox.setForeground(Color.WHITE);
        useStepTextFieldCheckBox.setOpaque(false);
        useStepTextFieldCheckBox.setFocusable(false);

        useStepTextFieldCheckBox.setSelected(false);
        this.add(useStepTextFieldCheckBox);

        JSlider stepSlider = new JSlider(0, 90, 10);
        stepSlider.setPreferredSize(new Dimension(170, 50));
        stepSlider.setForeground(Color.WHITE);
        stepSlider.setOpaque(false);
        stepSlider.setFocusable(false);

        stepSlider.setPaintTicks(true);
        stepSlider.setPaintLabels(true);
        stepSlider.setMajorTickSpacing(10);
        stepSlider.setOrientation(SwingConstants.HORIZONTAL);
        stepSlider.addChangeListener(e -> {
            viewportPanel.setStep(Math.max(stepSlider.getValue(), 1));
            viewportPanel.repaint();
        });
        this.add(stepSlider);

        JLabel dividerLabel2 = new JLabel();
        dividerLabel2.setPreferredSize(new Dimension(170, 15));
        dividerLabel2.setFocusable(false);
        this.add(dividerLabel2);

        JLabel x0SliderLabel = new JLabel("X slider");
        x0SliderLabel.setPreferredSize(new Dimension(170, 15));
        x0SliderLabel.setForeground(Color.WHITE);
        x0SliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(x0SliderLabel);

        JSlider x0Slider = new JSlider(-1000, 1000, 0);
        x0Slider.setPreferredSize(new Dimension(170, 15));
        x0Slider.setForeground(Color.WHITE);
        x0Slider.setOpaque(false);
        x0Slider.setFocusable(false);

        x0Slider.setOrientation(SwingConstants.HORIZONTAL);
        x0Slider.addChangeListener(e -> {
            viewportPanel.setX0(x0Slider.getValue() + vp.x0a);
            viewportPanel.repaint();
        });
        this.add(x0Slider);

        JLabel y0SliderLabel = new JLabel("Y slider");
        y0SliderLabel.setPreferredSize(new Dimension(170, 15));
        y0SliderLabel.setForeground(Color.WHITE);
        y0SliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(y0SliderLabel);

        JSlider y0Slider = new JSlider(-1000, 1000, 0);
        y0Slider.setPreferredSize(new Dimension(170, 15));
        y0Slider.setForeground(Color.WHITE);
        y0Slider.setOpaque(false);
        y0Slider.setFocusable(false);

        y0Slider.setOrientation(SwingConstants.HORIZONTAL);
        y0Slider.addChangeListener(e -> {
            viewportPanel.setY0(y0Slider.getValue() + vp.y0a);
            viewportPanel.repaint();
        });
        this.add(y0Slider);

        JLabel dividerLabel3 = new JLabel();
        dividerLabel3.setPreferredSize(new Dimension(170, 15));
        dividerLabel3.setFocusable(false);
        this.add(dividerLabel3);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(80, 30));
        refreshButton.setBackground(Color.DARK_GRAY);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        refreshButton.setFocusable(false);

        refreshButton.addActionListener(e -> {
            if (useStepTextFieldCheckBox.isSelected())
                viewportPanel.setStep(Math.max(Integer.parseInt(stepTextField.getText()), 1));
            viewportPanel.setGrid(gridCheckBox.isSelected());
            viewportPanel.setGridLabels(gridLabelsCheckBox.isSelected());
            viewportPanel.setPointLabels(pointLabelsCheckBox.isSelected());
            viewportPanel.setPointCoords(pointCoordsCheckBox.isSelected());
            viewportPanel.setPolygonPoints(polygonPointsCheckBox.isSelected());
            viewportPanel.repaint();
        });
        this.add(refreshButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(80, 30));
        resetButton.setBackground(Color.DARK_GRAY);
        resetButton.setForeground(Color.WHITE);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        resetButton.setFocusable(false);

        resetButton.addActionListener(e -> {
            gridCheckBox.setSelected(true);
            gridLabelsCheckBox.setSelected(true);
            pointLabelsCheckBox.setSelected(true);
            pointCoordsCheckBox.setSelected(false);
            stepTextField.setText("10");
            useStepTextFieldCheckBox.setSelected(false);
            stepSlider.setValue(10);
            x0Slider.setValue(0);
            y0Slider.setValue(0);
            viewportPanel.setStep(10);
            viewportPanel.setGrid(true);
            viewportPanel.setGridLabels(true);
            viewportPanel.setPointLabels(true);
            viewportPanel.setPointCoords(false);
            viewportPanel.setPolygonPoints(false);
            viewportPanel.setX0(vp.x0a);
            viewportPanel.setY0(vp.y0a);
            viewportPanel.repaint();
        });
        this.add(resetButton);

        JLabel dividerLabel4 = new JLabel();
        dividerLabel4.setPreferredSize(new Dimension(170, 15));
        dividerLabel4.setFocusable(false);
        this.add(dividerLabel4);

        JButton pointsManagerButton = new JButton("Points Manager");
        pointsManagerButton.setPreferredSize(new Dimension(150, 30));
        pointsManagerButton.setHorizontalAlignment(SwingConstants.CENTER);
        pointsManagerButton.setBackground(Color.DARK_GRAY);
        pointsManagerButton.setForeground(Color.WHITE);
        pointsManagerButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        pointsManagerButton.setFocusable(false);

        pointsManagerButton.addActionListener(e -> {
            new PointsManagerFrame(viewportPanel);
        });
        this.add(pointsManagerButton);

        JButton polygonsManagerButton = new JButton("Polygons Manager");
        polygonsManagerButton.setPreferredSize(new Dimension(150, 30));
        polygonsManagerButton.setHorizontalAlignment(SwingConstants.CENTER);
        polygonsManagerButton.setBackground(Color.DARK_GRAY);
        polygonsManagerButton.setForeground(Color.WHITE);
        polygonsManagerButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        polygonsManagerButton.setFocusable(false);

        polygonsManagerButton.addActionListener(e -> {
            new PolygonsManagerFrame(viewportPanel);
        });
        this.add(polygonsManagerButton);

    }
}
