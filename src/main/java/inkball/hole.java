package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class hole implements Drawable {
    private int x;
    private int y;
    private PImage image;
    private PApplet app;
    private int type;

    public hole(int x, int y, PImage[] images, PApplet app, int type) {
        this.x = x;
        this.y = y;
        this.app = app;
        this.type = type;
        this.image = images[6 + type];
    }

    @Override
    public void draw() {
        app.image(image, x, y, App.CELLSIZE * 2, App.CELLHEIGHT * 2);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }
}
