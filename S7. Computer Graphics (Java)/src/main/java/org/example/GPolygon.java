package org.example;

import java.awt.Color;
import java.util.ArrayList;

public class GPolygon {
    private ArrayList<GPoint> points;
    private Color color;
    private boolean fill;

    public GPolygon(ArrayList<GPoint> points) {
        this.points = points;
        this.color = Color.WHITE;
        this.fill = false;
    }

    public GPolygon(ArrayList<GPoint> points, Color color) {
        this.points = points;
        this.color = color;
        this.fill = false;
    }

    public ArrayList<GPoint> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public boolean isFill() {
        return fill;
    }

    public void setPoints(ArrayList<GPoint> points) {
        this.points = points;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public void addPoint(GPoint p) {
        points.add(p);
    }

    public void removePoint(GPoint p) {
        points.remove(p);
        if (points.isEmpty()) {
            points.add(new GPoint(new double[]{0, 0}));
        }
    }

    public void transform(double[][] matrix) {
        for (GPoint p : points) {
            p.transform(matrix);
        }
    }

    public void rotate(double angle) {
        angle = Math.toRadians(angle);
        double[][] rotationMatrix = new double[][]{
                {Math.cos(angle), Math.sin(angle)},
                {-Math.sin(angle), Math.cos(angle)}
        };
        transform(rotationMatrix);
    }

    public void rotate(double angle, GPoint point) {
        double x = point.getX();
        double y = point.getY();
        for (GPoint p : points) {
            p.setCoords(p.getX() - x, p.getY() - y);
        }
        rotate(angle);
        for (GPoint p : points) {
            p.setCoords(p.getX() + x, p.getY() + y);
        }
    }

}
