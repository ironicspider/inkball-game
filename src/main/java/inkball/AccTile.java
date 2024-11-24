package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class AccTile implements Drawable {

    private int x, y; 
    private PImage sprite;
    private int accelerationX; 
    private int accelerationY; 
    private PApplet app;

    public AccTile(int x, int y, PImage sprite, int accelX, int accelY, PApplet app) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.accelerationX = accelX;
        this.accelerationY = accelY;
        this.app = app;
    }

    public void applyAcceleration(balls ball) {
        ball.setVelocity(ball.getVelocityX() + accelerationX, 
                         ball.getVelocityY() + accelerationY);
    }

    @Override
    public void draw() {
        app.image(sprite, x, y, App.CELLSIZE, App.CELLHEIGHT);
    }
}
