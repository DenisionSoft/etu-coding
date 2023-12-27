package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class PolygonEditorFrame extends JFrame {
    final int WIDTH = 500;
    final int HEIGHT = 300;
    JPanel pointsPanel;
    JScrollPane scrollPane;

    private void drawPointList(ViewportPanel vp, GPolygon polygon) {
        DecimalFormat df = new DecimalFormat("#.##");
        for (GPoint p : polygon.getPoints()) {
            JPanel pointPanel = new JPanel();
            pointPanel.setPreferredSize(new Dimension(WIDTH - 40, 30));
            pointPanel.setBackground(Color.DARK_GRAY);

            pointPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

            if (p.getLabel() != null) {
                JLabel labelLabel = new JLabel(p.getLabel());
                labelLabel.setPreferredSize(new Dimension(100, 30));
                labelLabel.setForeground(Color.WHITE);
                labelLabel.setHorizontalAlignment(JLabel.CENTER);
                pointPanel.add(labelLabel);
            }

            JLabel coordsLabel = new JLabel("[" + df.format(p.getX()) + ", " + df.format(p.getY()) + "] ");
            coordsLabel.setPreferredSize(new Dimension(100, 30));
            coordsLabel.setForeground(Color.WHITE);
            coordsLabel.setHorizontalAlignment(JLabel.CENTER);
            pointPanel.add(coordsLabel);

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
            pointPanel.add(colorButton);

            JButton upButton = new JButton("/\\");
            upButton.setPreferredSize(new Dimension(30, 30));
            upButton.setForeground(Color.WHITE);
            upButton.setBackground(Color.DARK_GRAY);
            upButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            upButton.addActionListener(e -> {
                int index = polygon.getPoints().indexOf(p);
                if (index == 0) return;
                polygon.getPoints().set(index, polygon.getPoints().get(index - 1));
                polygon.getPoints().set(index - 1, p);
                vp.repaint();
                pointsPanel.removeAll();
                drawPointList(vp, polygon);
                pointsPanel.revalidate();
                pointsPanel.repaint();
            });
            pointPanel.add(upButton);

            JButton downButton = new JButton("\\/");
            downButton.setPreferredSize(new Dimension(30, 30));
            downButton.setForeground(Color.WHITE);
            downButton.setBackground(Color.DARK_GRAY);
            downButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            downButton.addActionListener(e -> {
                int index = polygon.getPoints().indexOf(p);
                if (index == polygon.getPoints().size() - 1) return;
                polygon.getPoints().set(index, polygon.getPoints().get(index + 1));
                polygon.getPoints().set(index + 1, p);
                vp.repaint();
                pointsPanel.removeAll();
                drawPointList(vp, polygon);
                pointsPanel.revalidate();
                pointsPanel.repaint();
            });
            pointPanel.add(downButton);

            JButton editButton = new JButton("E");
            editButton.setPreferredSize(new Dimension(30, 30));
            editButton.setForeground(Color.WHITE);
            editButton.setBackground(Color.DARK_GRAY);
            editButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            editButton.addActionListener(e -> {
                String coordsString = JOptionPane.showInputDialog(null, "Enter new coordinates in the form \"x y\" or new label.");
                if (coordsString == null) return;

                String[] coordsStringArray = coordsString.split(" ");
                if (coordsStringArray.length == 2) {
                    try {
                        int x = Integer.parseInt(coordsStringArray[0]);
                        int y = Integer.parseInt(coordsStringArray[1]);
                        p.setCoords(x, y);
                    } catch (NumberFormatException ex) {
                        p.setLabel(coordsString);
                    }
                } else {
                    p.setLabel(coordsString);
                }
                vp.repaint();
                pointsPanel.removeAll();
                drawPointList(vp, polygon);
                pointsPanel.revalidate();
                pointsPanel.repaint();
            });
            pointPanel.add(editButton);

            JButton transformButton = new JButton("T");
            transformButton.setPreferredSize(new Dimension(30, 30));
            transformButton.setForeground(Color.WHITE);
            transformButton.setBackground(Color.DARK_GRAY);
            transformButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            transformButton.addActionListener(e -> {

                String matrixString = JOptionPane.showInputDialog(null, "Enter a 2x2 matrix in the form \"a b c d\" to transform the point by.");
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
                pointsPanel.removeAll();
                drawPointList(vp, polygon);
                pointsPanel.revalidate();
                pointsPanel.repaint();
            });
            pointPanel.add(transformButton);

            JButton deleteButton = new JButton("X");
            deleteButton.setPreferredSize(new Dimension(30, 30));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.DARK_GRAY);
            deleteButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            deleteButton.addActionListener(e -> {
                if (polygon.getPoints().size() <= 2) {
                    JOptionPane.showMessageDialog(null, "A polygon must have at least 2 points.");
                    return;
                }
                polygon.removePoint(p);
                pointsPanel.remove(pointPanel);
                pointsPanel.revalidate();
                pointsPanel.repaint();
                vp.repaint();
            });
            pointPanel.add(deleteButton);

            pointsPanel.add(pointPanel);
        }
    }

    PolygonEditorFrame(ViewportPanel vp, GPolygon polygon) {
        this.setTitle("Polygon Editor");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);

        pointsPanel = new JPanel();
        pointsPanel.setPreferredSize(new Dimension(WIDTH - 40, HEIGHT - 50));
        pointsPanel.setBackground(Color.BLACK);
        pointsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 3));

        JScrollPane scrollPane = new JScrollPane(pointsPanel);
        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane, BorderLayout.CENTER);
        this.scrollPane = scrollPane;

        drawPointList(vp, polygon);

        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(WIDTH - 40, 30));
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));

        JTextField xField = new JTextField();
        xField.setPreferredSize(new Dimension(30, 30));
        xField.setForeground(Color.WHITE);
        xField.setBackground(Color.DARK_GRAY);
        xField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        xField.setHorizontalAlignment(JTextField.CENTER);
        headerPanel.add(xField);

        JTextField yField = new JTextField();
        yField.setPreferredSize(new Dimension(30, 30));
        yField.setForeground(Color.WHITE);
        yField.setBackground(Color.DARK_GRAY);
        yField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        yField.setHorizontalAlignment(JTextField.CENTER);
        headerPanel.add(yField);

        JTextField labelField = new JTextField();
        labelField.setPreferredSize(new Dimension(50, 30));
        labelField.setForeground(Color.WHITE);
        labelField.setBackground(Color.DARK_GRAY);
        labelField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        labelField.setHorizontalAlignment(JTextField.CENTER);
        headerPanel.add(labelField);

        JButton colorButton = new JButton();
        colorButton.setPreferredSize(new Dimension(30, 30));
        colorButton.setBackground(Color.WHITE);
        colorButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
            if (newColor != null) {
                colorButton.setBackground(newColor);
            }
        });
        headerPanel.add(colorButton);

        JButton addButton = new JButton("+");
        addButton.setPreferredSize(new Dimension(50, 30));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(Color.DARK_GRAY);
        addButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        addButton.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                polygon.addPoint(new GPoint(new double[]{x, y}, labelField.getText(), colorButton.getBackground()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid integers for x and y.");
            }
            vp.repaint();
            pointsPanel.removeAll();
            drawPointList(vp, polygon);
            pointsPanel.revalidate();
            pointsPanel.repaint();
        });
        headerPanel.add(addButton);

        JCheckBox fillCheckBox = new JCheckBox("Fill");
        fillCheckBox.setPreferredSize(new Dimension(90, 15));
        fillCheckBox.setForeground(Color.WHITE);
        fillCheckBox.setOpaque(false);
        fillCheckBox.setFocusable(false);
        fillCheckBox.setSelected(polygon.isFill());
        fillCheckBox.addActionListener(e -> {
            polygon.setFill(fillCheckBox.isSelected());
            vp.repaint();
        });
        headerPanel.add(fillCheckBox);

        this.add(headerPanel, BorderLayout.NORTH);

        this.setVisible(true);
    }
}
