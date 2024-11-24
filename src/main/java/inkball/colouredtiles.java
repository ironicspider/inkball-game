package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class colouredtiles implements Drawable{
    public PApplet app;
    public int x;
    public int y;
    public PImage images_tm;
    public int type;

    public colouredtiles(int x, int y, PImage images_tm, PApplet app, int type){
        this.x = x;
        this.y = y;
        this.app = app;
        this.images_tm = images_tm;
        this.type = type;
    }
    public int getType() {
        return this.type;
           
    }
    @Override
    public void draw(){
        this.app.image(images_tm, x , y);
    }
}
    

