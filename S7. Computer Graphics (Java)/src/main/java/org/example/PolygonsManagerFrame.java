package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

class RotationFrame extends JFrame {
    final int WIDTH = 400;
    final int HEIGHT = 200;

    RotationFrame(ViewportPanel vp, GPolygon p) {
        this.setTitle("Rotate Polygon");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

        JLabel angleLabel = new JLabel("Angle:");
        angleLabel.setPreferredSize(new Dimension(50, 30));
        angleLabel.setForeground(Color.WHITE);
        angleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(angleLabel);

        JTextField angleField = new JTextField();
        angleField.setPreferredSize(new Dimension(50, 30));
        angleField.setForeground(Color.WHITE);
        angleField.setBackground(Color.DARK_GRAY);
        angleField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        angleField.setHorizontalAlignment(JTextField.CENTER);
        mainPanel.add(angleField);

        JCheckBox useExistingPointCheckBox = new JCheckBox("Use Existing Point");
        useExistingPointCheckBox.setPreferredSize(new Dimension(150, 30));
        useExistingPointCheckBox.setForeground(Color.WHITE);
        useExistingPointCheckBox.setOpaque(false);
        useExistingPointCheckBox.setFocusable(false);
        mainPanel.add(useExistingPointCheckBox);

        JComboBox<GPoint> existingPointsComboBox = new JComboBox<>();
        existingPointsComboBox.setPreferredSize(new Dimension(150, 30));
        existingPointsComboBox.setForeground(Color.WHITE);
        existingPointsComboBox.setBackground(Color.DARK_GRAY);
        existingPointsComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        existingPointsComboBox.setFocusable(false);
        for (GPoint point : vp.points) {
            existingPointsComboBox.addItem(point);
        }
        mainPanel.add(existingPointsComboBox);

        JButton rotateButton = new JButton("Rotate");
        rotateButton.setPreferredSize(new Dimension(100, 30));
        rotateButton.setForeground(Color.WHITE);
        rotateButton.setBackground(Color.DARK_GRAY);
        rotateButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        rotateButton.addActionListener(e -> {
            double angle;
            try {
                angle = Double.parseDouble(angleField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for the angle.");
                return;
            }

            if (useExistingPointCheckBox.isSelected() && existingPointsComboBox.getSelectedItem() != null) {
                GPoint point = (GPoint) existingPointsComboBox.getSelectedItem();
                p.rotate(angle, point);
            } else {
                p.rotate(angle);
            }

            vp.repaint();
        });
        mainPanel.add(rotateButton);

        this.add(mainPanel);

        this.setVisible(true);
    }
}

public class PolygonsManagerFrame extends JFrame {
    final int WIDTH = 900;
    final int HEIGHT = 300;
    JPanel polygonsPanel;

    private void drawPolygonList(ViewportPanel vp) {
        DecimalFormat df = new DecimalFormat("#.##");
        for (GPolygon p : vp.polygons) {
            JPanel polygonPanel = new JPanel();
            polygonPanel.setPreferredSize(new Dimension(WIDTH - 40, 30));
            polygonPanel.setBackground(Color.DARK_GRAY);

            polygonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

            StringBuilder coordsLabelText = new StringBuilder();
            for (int i = 0, j = p.getPoints().size(); i < 15; i++) {
                coordsLabelText.append("[").append(df.format(p.getPoints().get(i).getX())).append(", ").append(df.format(p.getPoints().get(i).getY())).append("] ");
                j--;
                if (j == 0) break;
            }
            JLabel coordsLabel = new JLabel(coordsLabelText.toString());
            coordsLabel.setForeground(Color.WHITE);
            coordsLabel.setHorizontalAlignment(JLabel.CENTER);
            polygonPanel.add(coordsLabel);

            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setBackground(p.getColor());
            colorButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            colorButton.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(null, "Choose a color", p.getColor());
                if (newColor != null) {
                    colorButton.setBackground(newColor);
                    p.setColor(newColor);
                    vp.repaint();
                }
            });
            polygonPanel.add(colorButton);

            JButton editButton = new JButton("E");
            editButton.setPreferredSize(new Dimension(30, 30));
            editButton.setForeground(Color.WHITE);
            editButton.setBackground(Color.DARK_GRAY);
            editButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            editButton.addActionListener(e -> {
                PolygonsManagerFrame.this.dispose();
                new PolygonEditorFrame(vp, p);
            });
            polygonPanel.add(editButton);

            JButton transformButton = new JButton("T");
            transformButton.setPreferredSize(new Dimension(30, 30));
            transformButton.setForeground(Color.WHITE);
            transformButton.setBackground(Color.DARK_GRAY);
            transformButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            transformButton.addActionListener(e -> {

                String matrixString = JOptionPane.showInputDialog(null, "Enter a 2x2 matrix in the form \"a b c d\" to transform the polygon");
                if (matrixString == null) return;

                String[] matrixStringArray = matrixString.split(" ");
                if (matrixStringArray.length != 4) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid 2x2 matrix.");
                    return;
                }

                double[][] matrix = new double[2][2];
                try {
                    matrix[0][0] = Double.parseDouble(matrixStringArray[0]);
                    matrix[0][1] = Double.parseDouble(matrixStringArray[1]);
                    matrix[1][0] = Double.parseDouble(matrixStringArray[2]);
                    matrix[1][1] = Double.parseDouble(matrixStringArray[3]);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid integers for the matrix.");
                    return;
                }

                p.transform(matrix);

                vp.repaint();
                polygonsPanel.removeAll();
                drawPolygonList(vp);
                polygonsPanel.revalidate();
                polygonsPanel.repaint();
            });
            polygonPanel.add(transformButton);

            JButton rotateButton = new JButton("R");
            rotateButton.setPreferredSize(new Dimension(30, 30));
            rotateButton.setForeground(Color.WHITE);
            rotateButton.setBackground(Color.DARK_GRAY);
            rotateButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            rotateButton.addActionListener(e -> {
                PolygonsManagerFrame.this.dispose();
                new RotationFrame(vp, p);
            });
            polygonPanel.add(rotateButton);

            JButton deleteButton = new JButton("X");
            deleteButton.setPreferredSize(new Dimension(30, 30));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.DARK_GRAY);
            deleteButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            deleteButton.addActionListener(e -> {
                vp.polygons.remove(p);
                polygonsPanel.remove(polygonPanel);
                polygonsPanel.revalidate();
                polygonsPanel.repaint();
                vp.repaint();
            });
            polygonPanel.add(deleteButton);

            polygonsPanel.add(polygonPanel);
        }
    }

    PolygonsManagerFrame(ViewportPanel vp) {
        this.setTitle("Polygons Manager");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);

        polygonsPanel = new JPanel();
        polygonsPanel.setPreferredSize(new Dimension(WIDTH - 40, HEIGHT - 50));
        polygonsPanel.setBackground(Color.BLACK);
        polygonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 3));
        JScrollPane scrollPane = new JScrollPane(polygonsPanel);
        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane, BorderLayout.CENTER);

        drawPolygonList(vp);

        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(WIDTH - 40, 30));
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));

        JTextField x1Field = new JTextField();
        x1Field.setPreferredSize(new Dimension(30, 30));
        x1Field.setForeground(Color.WHITE);
        x1Field.setBackground(Color.DARK_GRAY);
        x1Field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        x1Field.setHorizontalAlignment(JTextField.CENTER);
        headerPanel.add(x1Field);

        JTextField y1Field = new JTextField();
        y1Field.setPreferredSize(new Dimension(30, 30));
        y1Field.setForeground(Color.WHITE);
        y1Field.setBackground(Color.DARK_GRAY);
        y1Field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        y1Field.setHorizontalAlignment(JTextField.CENTER);
        headerPanel.add(y1Field);

        JTextField label1Field = new JTextField();
        label1Field.setPreferredSize(new Dimension(50, 30));
        label1Field.setForeground(Color.WHITE);
        label1Field.setBackground(Color.DARK_GRAY);
        label1Field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        label1Field.setHorizontalAlignment(JTextField.CENTER);
        headerPanel.add(label1Field);

        JButton color1Button = new JButton();
        color1Button.setPreferredSize(new Dimension(30, 30));
        color1Button.setBackground(Color.WHITE);
        color1Button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        color1Button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
            if (newColor != null) {
                color1Button.setBackground(newColor);
            }
        });
        headerPanel.add(color1Button);

        JTextField x2Field = new JTextField();
        x2Field.setPreferredSize(new Dimension(30, 30));
        x2Field.setForeground(Color.WHITE);
        x2Field.setBackground(Color.DARK_GRAY);
        x2Field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        x2Field.setHorizontalAlignment(JTextField.CENTER);
        headerPanel.add(x2Field);

        JTextField y2Field = new JTextField();
        y2Field.setPreferredSize(new Dimension(30, 30));
        y2Field.setForeground(Color.WHITE);
        y2Field.setBackground(Color.DARK_GRAY);
        y2Field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        y2Field.setHorizontalAlignment(JTextField.CENTER);
        headerPanel.add(y2Field);

        JTextField label2Field = new JTextField();
        label2Field.setPreferredSize(new Dimension(50, 30));
        label2Field.setForeground(Color.WHITE);
        label2Field.setBackground(Color.DARK_GRAY);
        label2Field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        label2Field.setHorizontalAlignment(JTextField.CENTER);
        headerPanel.add(label2Field);

        JButton color2Button = new JButton();
        color2Button.setPreferredSize(new Dimension(30, 30));
        color2Button.setBackground(Color.WHITE);
        color2Button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        color2Button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
            if (newColor != null) {
                color2Button.setBackground(newColor);
            }
        });
        headerPanel.add(color2Button);

        JButton addButton = new JButton("+");
        addButton.setPreferredSize(new Dimension(50, 30));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(Color.DARK_GRAY);
        addButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        addButton.addActionListener(e -> {
            try {
                int x1 = Integer.parseInt(x1Field.getText());
                int y1 = Integer.parseInt(y1Field.getText());
                int x2 = Integer.parseInt(x2Field.getText());
                int y2 = Integer.parseInt(y2Field.getText());
                vp.polygons.add(new GPolygon(new ArrayList<>(Arrays.asList(new GPoint(new double[]{x1, y1}, label1Field.getText(), color1Button.getBackground()), new GPoint(new double[]{x2, y2}, label2Field.getText(), color2Button.getBackground())))));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid integers for x and y.");
            }
            vp.repaint();
            polygonsPanel.removeAll();
            drawPolygonList(vp);
            polygonsPanel.revalidate();
            polygonsPanel.repaint();
        });
        headerPanel.add(addButton);

        this.add(headerPanel, BorderLayout.NORTH);

        this.setVisible(true);
    }
}
