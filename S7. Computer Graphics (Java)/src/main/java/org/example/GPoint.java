package org.example;

import java.awt.Color;

public class GPoint {
    private double[] coords;
    private String label;
    private Color color;

    public GPoint(double[] coords) {
        this.coords = coords;
        this.color = Color.WHITE;
    }

    public GPoint(double[] coords, String label) {
        this.coords = coords;
        this.label = label;
        this.color = Color.WHITE;
    }

    public GPoint(double[] coords, String label, Color color) {
        this.coords = coords;
        this.label = label;
        this.color = color;
    }

    public double[] getCoords() {
        return coords;
    }

    public double getX() {
        return coords[0];
    }

    public double getY() {
        return coords[1];
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    public void setCoords(double[] coords) {
        this.coords = coords;
    }

    public void setCoords(double x, double y) {
        this.coords = new double[]{x, y};
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    public void transform(double[][] matrix) {
        double[] newCoords = new double[2];
        for (int i = 0; i < 2; i++) {
            newCoords[i] = matrix[0][i] * coords[0] + matrix[1][i] * coords[1];
        }
        coords = newCoords;
    }

    @Override
    public String toString() {
        return "[" + coords[0] + ", " + coords[1] + "] " + label;
    }
}
