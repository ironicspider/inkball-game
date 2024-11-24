package inkball;

import processing.core.PVector;
import java.util.ArrayList;
import processing.core.PApplet;

public class Line implements Drawable{
    public PApplet app;
    public ArrayList<PVector> points;
    

    public Line(PApplet app) {
        points = new ArrayList<>();
        this.app = app;
    }
    
    public void addPoint(float x, float y) {
        points.add(new PVector(x, y));
    }

    
    public void draw() {
        if (points.size() > 1) {
            this.app.stroke(0);
            this.app.strokeWeight(10); 

            for (int i = 0; i < points.size() - 1; i++) {
                PVector p1 = points.get(i);
                PVector p2 = points.get(i + 1);
                this.app.line(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }
    public ArrayList<PVector> getPoints() {
        return points;
    }
}
