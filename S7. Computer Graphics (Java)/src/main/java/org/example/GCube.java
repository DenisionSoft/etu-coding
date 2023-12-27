package org.example;

import java.awt.*;
import java.util.ArrayList;

public class GCube {
    private ArrayList<GPolygon3D> polygons;
    private boolean fill;

    public GCube(ArrayList<GPolygon3D> polygons) {
        this.polygons = polygons;
        this.fill = false;
    }

    public GCube(ArrayList<GPolygon3D> polygons, Color color) {
        this.polygons = polygons;
        this.fill = false;
    }

    public ArrayList<GPolygon3D> getPolygons() {
        return polygons;
    }

    public boolean isFill() {
        return fill;
    }

    public void setPolygons(ArrayList<GPolygon3D> polygons) {
        this.polygons = polygons;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public void transform(double[][] matrix) {
        for (GPolygon3D p : polygons) {
            p.transform(matrix);
        }
    }


    // rotate around its center
    public void rotate(double angle) {
        double x = 0;
        double y = 0;
        double z = 0;
        for (GPolygon3D p : polygons) {
            for (GPoint3D point : p.getPoints()) {
                x += point.getX();
                y += point.getY();
                z += point.getZ();
            }
        }
        x /= polygons.size();
        y /= polygons.size();
        z /= polygons.size();
        for (GPolygon3D p : polygons) {
            for (GPoint3D point : p.getPoints()) {
                point.setCoords(point.getX() - x, point.getY() - y, point.getZ() - z);
            }
        }
        rotate(angle);
        for (GPolygon3D p : polygons) {
            for (GPoint3D point : p.getPoints()) {
                point.setCoords(point.getX() + x, point.getY() + y, point.getZ() + z);
            }
        }
    }

    /*
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


     */
}
