package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class wall implements Drawable {
    private int x;
    private int y;
    private PImage image;
    private PApplet app;

    public wall(int x, int y, PImage image, PApplet app) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.app = app;
    }

    @Override
    public void draw() {
        app.image(image, x, y, App.CELLSIZE, App.CELLHEIGHT);
    }
}
