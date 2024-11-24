package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class blanktile implements Drawable{
    public PImage[] images_tm;
    public PApplet app;
    public int x;
    public int y;

public blanktile(int x, int y, PImage[] images_tm, PApplet app){
        this.x = x;
        this.y = y;
        this.app = app;
        this.images_tm = images_tm;
    }
    @Override
    public void draw(){
        this.app.image(this.images_tm[12], x , y);


    }
}