package inkball;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class balls implements Drawable {
    private float x;
    private float y;
    private float ball_vel_x;
    private float ball_vel_y;
    private float original_velocityx;
    private float original_velocityy;
    private PImage image;
    private PImage[] images_tm;
    private PApplet app;
    private int type;
    private boolean col_x = false;
    private boolean col_y = false;
    private int size = 32;
    private boolean captured = false;
    private ArrayList<Line> lines = new ArrayList<>();
    private hole targetHole = null;
    private boolean isAccelerating = false;
    private long accstart = 0;
    private long accelerationDuration = 500;


    public balls(int x, int y, PImage images, PApplet app, int type, PImage[] images_tm, ArrayList<Line> lines) {
        this.x = x;
        this.y = y;
        this.app = app;
        this.type = type;
        this.images_tm = images_tm;
        this.image = images;
        this.lines = lines;

        if (App.random.nextBoolean()) {
            this.ball_vel_x = 2;
        } else {
            this.ball_vel_x = -2;
        }

        if (App.random.nextBoolean()) {
            this.ball_vel_y = 2;
        } else {
            this.ball_vel_y = -2;
        }
        this.original_velocityx = this.ball_vel_x;
        this.original_velocityy = this.ball_vel_y;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getVelocityX(){
        return this.ball_vel_x;
    }

    public float getVelocityY(){
        return this.ball_vel_y;
    }

    public void setVelocity(float x, float y){
        this.ball_vel_x = x;
        this.ball_vel_y = y;
    }

    public void startAcceleration(float accelX, float accelY) {
        if (!captured) {
            if (!isAccelerating) {
                original_velocityx = this.ball_vel_x;
                original_velocityy = this.ball_vel_y;
            }
            isAccelerating = true;
            accstart = app.millis();
    
            this.ball_vel_x += accelX;
            this.ball_vel_y += accelY;
        }
    }
    

    public void collision_tile_colour_change(colouredtiles tile) {
        int tileColourType = tile.getType();
        if (tileColourType == 1) {
            this.image = images_tm[1];
        } else if (tileColourType == 2) {
            this.image = images_tm[2];
        } else if (tileColourType == 3) {
            this.image = images_tm[3];
        } else if (tileColourType == 4) {
            this.image = images_tm[4];
        } else {
            this.image = images_tm[0];
        }
        this.type = tileColourType;
    }

    public void move() {
        if (!((App) app).ret_pause() == true) { 
            if (isAccelerating) {
                if (app.millis() - accstart >= accelerationDuration) {
                    this.ball_vel_x = original_velocityx;
                    this.ball_vel_y = original_velocityy;
                    isAccelerating = false;
                }
            }
            if (!captured) {
                applyAttractionForce();
                
                float nextX = x + ball_vel_x;
                float nextY = y + ball_vel_y;
                col_x = false;
                col_y = false;

                if (ball_vel_x > 0) {
                    int colRight1 = (int) ((nextX + App.CELLSIZE - 1) / App.CELLSIZE);
                    int rowTop1 = (int) (y - 64) / App.CELLHEIGHT;
                    int rowBottom1 = (int) ((y + App.CELLHEIGHT - 64 - 1) / App.CELLHEIGHT);

                    for (int row1 = rowTop1; row1 <= rowBottom1; row1++) {
                        if (colRight1 >= 0 && colRight1 < App.BOARD_WIDTH && row1 >= 0 && row1 < App.BOARD_HEIGHT) {
                            Drawable tile = ((App) app).Game_Board[row1][colRight1];
                            if (tile instanceof wall) {
                                col_x = true;
                                ball_vel_x = -ball_vel_x;
                                nextX = colRight1 * App.CELLSIZE - App.CELLSIZE;
                                break;
                            } else if (tile instanceof colouredtiles) {
                                this.collision_tile_colour_change((colouredtiles) tile);
                                col_x = true;
                                ball_vel_x = -ball_vel_x;
                                nextX = colRight1 * App.CELLSIZE - App.CELLSIZE;
                                break;
                            }
                        }
                    }
                    checkLineCollisions();
                } else if (ball_vel_x < 0) {
                    int colLeft1 = (int) nextX / App.CELLSIZE;
                    int rowTop2 = (int) (y - 64) / App.CELLHEIGHT;
                    int rowBottom2 = (int) ((y + App.CELLHEIGHT - 64 - 1) / App.CELLHEIGHT);

                    for (int row2 = rowTop2; row2 <= rowBottom2; row2++) {
                        if (colLeft1 >= 0 && colLeft1 < App.BOARD_WIDTH && row2 >= 0 && row2 < App.BOARD_HEIGHT) {
                            Drawable tile = ((App) app).Game_Board[row2][colLeft1];
                            if (tile instanceof wall) {
                                col_x = true;
                                ball_vel_x = -ball_vel_x;
                                nextX = (colLeft1 + 1) * App.CELLSIZE;
                                break;
                            } else if (tile instanceof colouredtiles) {
                                this.collision_tile_colour_change((colouredtiles) tile);
                                col_x = true;
                                ball_vel_x = -ball_vel_x;
                                nextX = (colLeft1 + 1) * App.CELLSIZE;
                                break;
                            }
                        }
                    }
                    checkLineCollisions();
                }

                if (ball_vel_y > 0) {
                    int rowBottom3 = (int) ((nextY + App.CELLHEIGHT - 64 - 1) / App.CELLHEIGHT);
                    int colLeft2 = (int) nextX / App.CELLSIZE;
                    int collRight2 = (int) ((nextX + App.CELLSIZE - 1) / App.CELLSIZE);

                    for (int col1 = colLeft2; col1 <= collRight2; col1++) {
                        if (col1 >= 0 && col1 < App.BOARD_WIDTH && rowBottom3 >= 0 && rowBottom3 < App.BOARD_HEIGHT) {
                            Drawable tile = ((App) app).Game_Board[rowBottom3][col1];
                            if (tile instanceof wall) {
                                col_y = true;
                                ball_vel_y = -ball_vel_y;
                                nextY = rowBottom3 * App.CELLHEIGHT + 64 - App.CELLHEIGHT;
                                break;
                            } else if (tile instanceof colouredtiles) {
                                this.collision_tile_colour_change((colouredtiles) tile);
                                col_y = true;
                                ball_vel_y = -ball_vel_y;
                                nextY = rowBottom3 * App.CELLHEIGHT + 64 - App.CELLHEIGHT;
                                break;
                            }
                        }
                    }
                    checkLineCollisions();
                } else if (ball_vel_y < 0) {
                    int rowTop3 = (int) (nextY - 64) / App.CELLHEIGHT;
                    int colLeft3 = (int) nextX / App.CELLSIZE;
                    int colRight3 = (int) ((nextX + App.CELLSIZE - 1) / App.CELLSIZE);

                    for (int col2 = colLeft3; col2 <= colRight3; col2++) {
                        if (col2 >= 0 && col2 < App.BOARD_WIDTH && rowTop3 >= 0 && rowTop3 < App.BOARD_HEIGHT) {
                            Drawable tile = ((App) app).Game_Board[rowTop3][col2];
                            if (tile instanceof wall) {
                                col_y = true;
                                ball_vel_y = -ball_vel_y;
                                nextY = (rowTop3 + 1) * App.CELLHEIGHT + 64;
                                break;
                            } else if (tile instanceof colouredtiles) {
                                this.collision_tile_colour_change((colouredtiles) tile);
                                col_y = true;
                                ball_vel_y = -ball_vel_y;
                                nextY = (rowTop3 + 1) * App.CELLHEIGHT + 64;
                                break;
                            }
                        }
                    }
                    checkLineCollisions();
                }
                x = nextX;
                y = nextY;

                if (x <= 0) {
                    x = 0;
                    ball_vel_x = -ball_vel_x;
                } else if (x + App.CELLSIZE >= app.width) {
                    x = app.width - App.CELLSIZE;
                    ball_vel_x = -ball_vel_x;
                }

                if (y <= 64) {
                    y = 64;
                    ball_vel_y = -ball_vel_y;
                } else if (y + App.CELLHEIGHT >= app.height) {
                    y = app.height - App.CELLHEIGHT;
                    ball_vel_y = -ball_vel_y;
                }

                checkHoleCapture();
            } else {
                shrink();
            }
        }
    }
        public void applyAttractionForce() {
            for (hole h : ((App) app).holes) {
                float hx = h.getX() + App.CELLSIZE;
                float hy = h.getY() + App.CELLHEIGHT;

                float ballCenterX = x + size / 2;
                float ballCenterY = y + size / 2;

                float distance = PApplet.dist(ballCenterX, ballCenterY, hx, hy);
                if (distance < 50) { 
                    float fx = 0.008f * (hx - ballCenterX);
                    float fy = 0.008f * (hy - ballCenterY);

                    ball_vel_x += fx;
                    ball_vel_y += fy;
                }
            }
        }

    public void checkHoleCapture() {
        for (hole h : ((App) app).holes) {
            float hx = h.getX() + App.CELLSIZE;
            float hy = h.getY() + App.CELLHEIGHT;
    
            float bx = x + 32 / 2;
            float by = y + 32 / 2;
    
            float distance = PApplet.dist(bx, by, hx, hy);
            if (distance < App.CELLSIZE / 2) {
                captured = true;
                targetHole = h;
                ball_vel_x = 0; 
                ball_vel_y = 0;
                if (this.type == h.getType()){
                    ((App) app).ballcaptured(GetColor(type));
                }else{
                    ((App) app).ballnotcaptured(GetColor(type));
                }
                break;
            }
        }
    }

    private String GetColor(int type){
        String color;
        if (type == 1) {
            color = "orange";
        } else if (type == 2) {
            color = "blue";
        } else if (type == 3) {
            color = "green";
        } else if (type == 4) {
            color = "yellow";
        } else {
            color = "grey";
        }
        return color;
    }
    
    private void shrink() {
        if (targetHole != null) {
            float hx = targetHole.getX() + App.CELLSIZE;
            float hy = targetHole.getY() + App.CELLHEIGHT;
    
            float bx = x + 32 / 2;
            float by = y + 32 / 2;

            x += (hx - bx) * 0.1f;
            y += (hy - by) * 0.1f;

            size *= 0.9f;
    
            if (size < 5) {
                ((App) app).markBallForRemoval(this);
                }
            }
        }
    
    

    // private void moveTowardsHole() {
    //     if (targetHole != null) {
    //         float holeCenterX = targetHole.getX() + App.CELLSIZE;
    //         float holeCenterY = targetHole.getY() + App.CELLHEIGHT;
    
    //         float ballCenterX = x + size / 2;
    //         float ballCenterY = y + size / 2;
    
    //         float dx = holeCenterX - ballCenterX;
    //         float dy = holeCenterY - ballCenterY;
    
    //         // Apply the attraction force
    //         x += dx * 0.05;  // Adjust the multiplier as needed
    //         y += dy * 0.05;
    
    //         size *= 0.98;  
    //  
    //         if (PApplet.dist(ballCenterX, ballCenterY, holeCenterX, holeCenterY) < 2) {
    //             ((App) app).b.remove(this);
    //         }
    //     }
    // }

    public boolean isNearHole(hole h) {
        float hx = h.getX() + App.CELLSIZE;  
        float hy = h.getY() + App.CELLHEIGHT; 
    
        float bx = x + size / 2;  
        float by = y + size / 2;  
    
        float distance = PApplet.dist(bx, by, hx, hy);
        return distance <= 32;  
    }
    

    public boolean checkLineCollisions() {
            for (Line line : lines) {
                ArrayList<PVector> points = line.getPoints();
                for (int i = 0; i < points.size() - 1; i++) {
                    PVector p1 = points.get(i);
                    PVector p2 = points.get(i + 1);
        
                    if (PVector.dist(p1, p2) == 0){
                         continue;
                    }
                    if (ballIntersectsLineSegment(p1.x, p1.y, p2.x, p2.y)) {
                        ((App) app).line_remover(line);
                        reflect_velocity(p1, p2);
                        return true;
                    }
                }
            }
            return false;
        }
        

    public boolean ballIntersectsLineSegment(float i, float j, float k, float l) {
        float newx = x + App.CELLSIZE / 2;
        float newy = y + App.CELLHEIGHT / 2;
        float radius = App.CELLSIZE / 2;

        float dist = distance(newx, newy, i, j, k, l);
        return dist <= radius;
    }

     public float distance(float g, float u, float x1, float y1, float x2, float y2) {
        float segment_length = PApplet.sq(x2 - x1) + PApplet.sq(y2 - y1);
        if (segment_length == 0) {
            return PApplet.dist(g, u, x1, y1);  
        }
        float t = ((g - x1) * (x2 - x1) + (u - y1) * (y2 - y1)) / segment_length; //projecting factor
    
        t = PApplet.constrain(t, 0, 1);
    
        float new_x = x1 + t * (x2 - x1);
        float new_y = y1 + t * (y2 - y1);
    
        return PApplet.dist(g, u, new_x, new_y);
    }
    

    public void reflect_velocity(PVector p1, PVector p2) {
        PVector lineVector = PVector.sub(p2, p1).normalize(); // Calculating the direction vector of the line segment
        PVector normalVector = new PVector(-lineVector.y, lineVector.x); // Calculateing the normal vector perpendicular to this line
        PVector velocity = new PVector(ball_vel_x, ball_vel_y); // velocity vector from the ball’s current velocities
        float projection = PVector.dot(velocity, normalVector); // Projecting the velocity onto the normal using the dot product
        PVector reflection = PVector.sub(velocity, PVector.mult(normalVector, 2 * projection)); // Calculate the reflection by subtracting twice the projection of velocity along the normal

        ball_vel_x = reflection.x;
        ball_vel_y = reflection.y;
    
        float magnitude = reflection.mag();
        if (magnitude != 0) {
            ball_vel_x = (reflection.x / magnitude) * 2.828f; 
            ball_vel_y = (reflection.y / magnitude) * 2.828f;
        }
    }
    @Override
    public void draw() {
        move();
        app.image(image, x, y, size, size);
    }
}
