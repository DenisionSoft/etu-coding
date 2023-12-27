package org.example;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ViewportPanel extends JPanel {
    int x0, y0, z0, xmax, ymax, zmax;
    int x0a, y0a, z0a;
    int f;
    ArrayList<GPoint> points = new ArrayList<>();
    ArrayList<GPolygon> polygons = new ArrayList<>();
    ArrayList<GPolygon3D> polygons3D = new ArrayList<>();
    ArrayList<GCube> cubes = new ArrayList<>();
    int step;
    private boolean isGrid = true;
    private boolean isGridLabels = true;
    private boolean isPointLabels = true;
    private boolean isPointCoords = false;
    private boolean isPolygonPoints = false;
    ViewportPanel(int w, int h, int step) {
        this.x0 = w / 2;
        this.y0 = h / 2;
        this.z0 = 0;
        this.x0a = w / 2;
        this.y0a = h / 2;
        this.z0a = 0;
        this.f = 30; // fov
        this.xmax = w;
        this.ymax = h;
        this.zmax = 0;
        this.step = step;

        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(Color.BLACK);


    }

    public void paint(Graphics gold) {
        super.paint(gold);
        Graphics2D g = (Graphics2D) gold;

        if (isGrid) {
            g.setPaint(Color.DARK_GRAY);
            for (int x = x0 % step; x < xmax; x += step) {
                g.drawLine(x, 0, x, ymax);
            }
            for (int y = y0 % step; y < ymax; y += step) {
                g.drawLine(0, y, xmax, y);
            }
        }

        g.setPaint(Color.GRAY);

        g.drawLine(x0, 0, x0, ymax);
        g.drawLine(0, y0, xmax, y0);

        if (isGridLabels) {
            // draw grid labels every 5 steps
            g.setPaint(Color.WHITE);
            for (int x = x0 % (step * 5), y = y0 % (step * 5); x < xmax || y < ymax; x += step * 5, y += step * 5) {
                g.drawString(String.valueOf((x - x0) / step), x, y0 + 15);
                g.drawString(String.valueOf((y0 - y) / step), x0 + 5, y);

                g.drawLine(x, y0 - 2, x, y0 + 2);
                g.drawLine(x0 - 2, y, x0 + 2, y);
            }
        }

        pointDraw(g, points);

        for (GPolygon polygon : polygons) {
            g.setPaint(polygon.getColor());
            g.setStroke(new BasicStroke(2));

            ArrayList<GPoint> polygonPoints = polygon.getPoints();
            int polygonSize = polygonPoints.size();

            int[] xPoints = new int[polygonSize];
            int[] yPoints = new int[polygonSize];
            for (int i = 0; i < polygonSize; i++) {
                xPoints[i] = (int)Math.round(x0 + polygonPoints.get(i).getX() * step);
                yPoints[i] = (int)Math.round(y0 - polygonPoints.get(i).getY() * step);
            }
            g.drawPolygon(xPoints, yPoints, polygonSize);

            if (polygon.isFill()) {
                g.fillPolygon(xPoints, yPoints, polygonSize);
            }

            if (isPolygonPoints)
                pointDraw(g, polygonPoints);
        }

        // draw 3D polygons by using projection of X = X * (F / Z) , Y = Y * (F / Z)
        for (GPolygon3D polygon : polygons3D) {
            g.setPaint(polygon.getColor());
            g.setStroke(new BasicStroke(2));

            ArrayList<GPoint3D> polygonPoints = polygon.getPoints();
            int polygonSize = polygonPoints.size();

            int[] xPoints = new int[polygonSize];
            int[] yPoints = new int[polygonSize];
            for (int i = 0; i < polygonSize; i++) {
                xPoints[i] = (int)Math.round(x0 + polygonPoints.get(i).getX() * step * (f / polygonPoints.get(i).getZ()));
                yPoints[i] = (int)Math.round(y0 - polygonPoints.get(i).getY() * step * (f / polygonPoints.get(i).getZ()));
            }
            g.drawPolygon(xPoints, yPoints, polygonSize);

            if (polygon.isFill()) {
                g.fillPolygon(xPoints, yPoints, polygonSize);
            }

            /*
            if (isPolygonPoints)
                pointDraw(g, polygonPoints);

             */
        }

        // draw cubes by drawing its polygons3d, respecting their color and using the same projection method as above
        for (GCube cube : cubes) {
            for (GPolygon3D polygon : cube.getPolygons()) {
                g.setPaint(polygon.getColor());
                g.setStroke(new BasicStroke(2));

                ArrayList<GPoint3D> polygonPoints = polygon.getPoints();
                int polygonSize = polygonPoints.size();

                int[] xPoints = new int[polygonSize];
                int[] yPoints = new int[polygonSize];
                for (int i = 0; i < polygonSize; i++) {
                    xPoints[i] = (int)Math.round(x0 + polygonPoints.get(i).getX() * step * (f / polygonPoints.get(i).getZ()));
                    yPoints[i] = (int)Math.round(y0 - polygonPoints.get(i).getY() * step * (f / polygonPoints.get(i).getZ()));
                }
                g.drawPolygon(xPoints, yPoints, polygonSize);

                if (polygon.isFill()) {
                    g.fillPolygon(xPoints, yPoints, polygonSize);
                }

                /*
                if (isPolygonPoints)
                    pointDraw(g, polygonPoints);

                 */
            }
        }

        g.setPaint(Color.WHITE);
        g.drawString("(" + (x0 * 2 - 1000) / step / 2 * -1 + ", " + (y0 * 2 - 1000) / step / 2 + ")", 20, y0a*2 - 50);
    }

    private void pointDraw(Graphics2D g, ArrayList<GPoint> polygonPoints) {
        DecimalFormat df = new DecimalFormat("#.##");
        for (GPoint point : polygonPoints) {
            g.setPaint(point.getColor());
            g.setStroke(new BasicStroke(3));
            g.drawLine((int)Math.round(x0 + point.getX() * step), (int)Math.round(y0 - point.getY() * step), (int)Math.round(x0 + point.getX() * step), (int)Math.round(y0 - point.getY() * step));

            if (isPointLabels && point.getLabel() != null)
                g.drawString(point.getLabel(), (int)Math.round(x0 + point.getX() * step), (int)Math.round(y0 - point.getY() * step - 5));

            if (isPointCoords)
                g.drawString("(" + df.format(point.getX()) + ", " + df.format(point.getY()) + ")", (int)Math.round(x0 + point.getX() * step), (int)Math.round(y0 - point.getY() * step + 15));
        }
    }

    public void setGrid(boolean isGrid) {
        this.isGrid = isGrid;
    }

    public void setGridLabels(boolean isGridLabels) {
        this.isGridLabels = isGridLabels;
    }

    public void setPointLabels(boolean isPointLabels) {
        this.isPointLabels = isPointLabels;
    }

    public void setPointCoords(boolean isPointCoords) {
        this.isPointCoords = isPointCoords;
    }

    public void setPolygonPoints(boolean isPolygonPoints) {
        this.isPolygonPoints = isPolygonPoints;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setX0(int x0) {
        this.x0 = x0;
    }

    public void setY0(int y0) {
        this.y0 = y0;
    }

    public void setZ0(int z0) {
        this.z0 = z0;
    }

    public void addPoint(GPoint point) {
        points.add(point);
    }
}
