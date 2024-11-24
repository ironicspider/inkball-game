package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class spawner implements Drawable{
    public PImage[] images_tm;
    public PApplet app;
    public int x;
    public int y;

    public spawner(int x, int y, PImage[] images_tm, PApplet app){
        this.x = x;
        this.y = y;
        this.app = app;
        this.images_tm = images_tm;
    }
    @Override
    public void draw(){
        this.app.image(this.images_tm[5], x , y);
    }
    public int ret_x_loc(){
        return this.x;
    }
    public balls spawnBall(String color) {
        PImage ballImage = ((App) app).ballcolor.get(color);
        return new balls(x, y, ballImage, app, 0, this.images_tm, ((App) app).lines);
    }
    public int ret_y_loc(){
        return this.y;
    }
}