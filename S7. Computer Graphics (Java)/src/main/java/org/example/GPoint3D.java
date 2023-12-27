package org.example;

import java.awt.*;

public class GPoint3D {
    private double[] coords;
    private String label;
    private Color color;

    public GPoint3D(double[] coords) {
        this.coords = coords;
        this.color = Color.WHITE;
    }

    public GPoint3D(double[] coords, String label) {
        this.coords = coords;
        this.label = label;
        this.color = Color.WHITE;
    }

    public GPoint3D(double[] coords, String label, Color color) {
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

    public double getZ() {
        return coords[2];
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

    public void setCoords(double x, double y, double z) {
        this.coords = new double[]{x, y, z};
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void transform(double[][] matrix) {
        double[] newCoords = new double[3];
        for (int i = 0; i < 3; i++) {
            newCoords[i] = 0;
            for (int j = 0; j < 3; j++) {
                newCoords[i] += matrix[i][j] * coords[j];
            }
        }
    }

    @Override
    public String toString() {
        return "[" + coords[0] + ", " + coords[1] + ", " + coords[2] + "]";
    }
}
