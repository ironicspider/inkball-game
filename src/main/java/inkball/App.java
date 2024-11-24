package inkball;

import processing.core.PApplet;
import processing.core.PImage;

import processing.data.JSONObject;
import processing.data.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int CELLHEIGHT = 32;
    public JSONObject config;
    public JSONArray levels;
    public static int WIDTH = 576;
    public static int HEIGHT = 640;
    public static final int BOARD_WIDTH = WIDTH / CELLSIZE;
    public static final int BOARD_HEIGHT = (HEIGHT - 64) / CELLHEIGHT;
    private PImage[] images_tm = new PImage[22];
    public int BOARD_OFFSET_Y = 64;

    public int currentTime;
    public double scoreIncreaseModifier;
    public double scoreDecreaseModifier;
    public HashMap<String, Integer> calcinc = new HashMap<>();
    public HashMap<String, Integer> calcdec = new HashMap<>();

    private ArrayList<spawner> spawners = new ArrayList<>();
    private long lastSpawnTime = 0;
    private int spawn_interwal;  

    public ArrayList<PImage> display_balls_ontop = new ArrayList<>();
    public ArrayList<Integer> colour_ball_int = new ArrayList<>();

    public int levelTime;
    public int remainingtime;
    public long startTime;

    public int ball_bar_height = 44;
    public int ball_bar_width = 166;
    public int TOPBAR = 64;

    private int score = 0;

    public boolean time_up = false;

    public static final int FPS = 30;

    public String configPath;

    public ArrayList<String> level1 = new ArrayList<>();
    public ArrayList<String> level2 = new ArrayList<>();
    public ArrayList<String> level3 = new ArrayList<>();
    
    public ArrayList<String> currentLevel;

    public ArrayList<hole> holes = new ArrayList<>();
    public ArrayList<balls> b = new ArrayList<>();

    private int animation_row1 = 0, animation_col1 = 0;
    private int animation_row2 = BOARD_HEIGHT - 1, animation_col2 = BOARD_WIDTH - 1;

  
    private long tiletime = 0;
    private final int TILE_MOVE_INTERVAL = 67;
    private boolean level_end_ani = false;

    private boolean animation_over = false;
    private ArrayList<balls> ballsToRemove = new ArrayList<>();

    private int present_level = -1;

    private boolean paused = false;

    private Line currentLine;  
    public ArrayList<Line> lines = new ArrayList<>(); 

    public Drawable[][] Game_Board;

    public HashMap<String, PImage> ballcolor = new HashMap<>();

    public static Random random = new Random();

    public void images_load() {
        images_tm[0] = loadImage("inkball/ball0.png");
        images_tm[1] = loadImage("inkball/ball1.png");
        images_tm[2] = loadImage("inkball/ball2.png");
        images_tm[3] = loadImage("inkball/ball3.png");
        images_tm[4] = loadImage("inkball/ball4.png");
        images_tm[5] = loadImage("inkball/entrypoint.png");
        images_tm[6] = loadImage("inkball/hole0.png");
        images_tm[7] = loadImage("inkball/hole1.png");
        images_tm[8] = loadImage("inkball/hole2.png");
        images_tm[9] = loadImage("inkball/hole3.png");
        images_tm[10] = loadImage("inkball/hole4.png");
        images_tm[11] = loadImage("inkball/inkball_spritesheet.png");
        images_tm[12] = loadImage("inkball/tile.png");
        images_tm[13] = loadImage("inkball/wall0.png");
        images_tm[14] = loadImage("inkball/wall1.png");
        images_tm[15] = loadImage("inkball/wall2.png"); 
        images_tm[16] = loadImage("inkball/wall3.png"); 
        images_tm[17] = loadImage("inkball/wall4.png");
        images_tm[18] = loadImage("inkball/accup.png");
        images_tm[19] = loadImage("inkball/accdown.png"); 
        images_tm[20] = loadImage("inkball/accright.png"); 
        images_tm[21] = loadImage("inkball/accleft.png");
    }

    public App() {
        this.configPath = "config.json";
    }

    public void nextLevel() {
        present_level++;
        if (present_level >= levels.size()) {
            game_end();
        } else {
            loadLevelConfig();
            load_level();
            animation_over = false;
        }
    }

    public void initializeballcolor() {
        ballcolor.put("grey", images_tm[0]);
        ballcolor.put("orange", images_tm[1]);
        ballcolor.put("blue", images_tm[2]);
        ballcolor.put("green", images_tm[3]);
        ballcolor.put("yellow", images_tm[4]);
    }

    public void loadConfig() {
        config = loadJSONObject("config.json");
        levels = config.getJSONArray("levels");

        JSONObject increase = config.getJSONObject("score_increase_from_hole_capture");
        JSONObject decrease = config.getJSONObject("score_decrease_from_wrong_hole");

        for (Object key : increase.keys()) {
            String strKey = (String) key;
            calcinc.put(strKey, increase.getInt(strKey));
        }

        for (Object key : decrease.keys()) {
            String strKey = (String) key;
            calcdec.put(strKey, decrease.getInt(strKey));
        }
    }

    public void loadLevelConfig() {
        if (present_level < levels.size()) {
            JSONObject clg = levels.getJSONObject(present_level);
            scoreIncreaseModifier = clg.getFloat("score_increase_from_hole_capture_modifier"); 
            scoreDecreaseModifier = clg.getFloat("score_decrease_from_wrong_hole_modifier"); 
        }
    } 

    public void load_level() {
        if (present_level < 0 || present_level >= levels.size()) {
            present_level = 0;
        }
        b.clear();
        holes.clear();
        lines.clear();
        display_balls_ontop.clear();
        colour_ball_int.clear();
        spawners.clear();
        time_up = false;
        paused = false;
        level_end_ani = false;
        animation_over = false;

        JSONObject clg = levels.getJSONObject(present_level);
        levelTime = clg.getInt("time");
        remainingtime = levelTime;
        startTime = System.currentTimeMillis();

        spawn_interwal = clg.getInt("spawn_interval") * 1000;

        JSONArray baaaaaals_aray = clg.getJSONArray("balls");

        for (int i = 0; i < baaaaaals_aray.size(); i++) {
            String color = baaaaaals_aray.getString(i);
            PImage ballImage = ballcolor.get(color);
            colour_ball_int.add(get_ball_colour(color));
            display_balls_ontop.add(ballImage);
        }

        switch (present_level) {
            case 0:
                currentLevel = new ArrayList<>(Arrays.asList(loadStrings("level1.txt")));
                break;
            case 1:
                currentLevel = new ArrayList<>(Arrays.asList(loadStrings("level2.txt")));
                break;
            case 2:
                currentLevel = new ArrayList<>(Arrays.asList(loadStrings("level3.txt")));
                break;
            default:
                System.err.println("Invalid level index: " + present_level);
                present_level = 0;
                currentLevel = new ArrayList<>(Arrays.asList(loadStrings("level1.txt")));
                break;
        }

        int r = currentLevel.size();
        int c = currentLevel.get(0).length();
        Game_Board = new Drawable[r][c];

       
        for (int row = 0; row < r; row++) {
            String line = currentLevel.get(row);

            for (int col = 0; col < line.length(); ) {
                char tile = line.charAt(col);

                if ((tile == 'H' || tile == 'B') && col + 1 < line.length() && Character.isDigit(line.charAt(col + 1))) {
                    char num = line.charAt(col + 1);
                    int tilenum = Character.getNumericValue(num);
                    int x = col * CELLSIZE;
                    int y = row * CELLHEIGHT + BOARD_OFFSET_Y; 

                    switch (tile) {
                        case 'H':
                            hole newHole = new hole(x, y, images_tm, this, tilenum);
                            holes.add(newHole);
                            col += 2; 
                            break;
                        case 'B':
                            balls newb = new balls(x, y, images_tm[0], this, tilenum, images_tm, lines);
                            b.add(newb);
                            col += 2;
                            break;
                        default:
                            col++;
                            break;
                    }
                } else if (Character.isDigit(tile)) {
                    int tilenum = Character.getNumericValue(tile);
                    int x = col * CELLSIZE;
                    int y = row * CELLHEIGHT + BOARD_OFFSET_Y;

                    int type = tilenum; 
                    Game_Board[row][col] = new colouredtiles(x, y, images_tm[13 + tilenum], this, type); 
                    col++; 
                } else {
                    int x = col * CELLSIZE;
                    int y = row * CELLHEIGHT + BOARD_OFFSET_Y; 

                    switch (tile) {
                        case 'X':
                            Game_Board[row][col] = new wall(x, y, images_tm[13], this); 
                            break;
                        case 'S':
                            spawner newSpawner = new spawner(x, y, images_tm, this);
                            spawners.add(newSpawner);
                            Game_Board[row][col] = newSpawner;
                            break;
                        case 'U':
                            Game_Board[row][col] = new AccTile(x, y, images_tm[18], 0, -1, this); 
                            break;
                        case 'D':
                            Game_Board[row][col] = new AccTile(x, y, images_tm[19], 0, 1, this); 
                            break;
                        case 'R':
                            Game_Board[row][col] = new AccTile(x, y, images_tm[20], 1, 0, this);
                            break;
                        case 'L':
                            Game_Board[row][col] = new AccTile(x, y, images_tm[21], -1, 0, this);
                            break;
                        default:
                            break;
                    }
                    col++;
                }
            }
        }

        animation_row1 = 0;
        animation_col1 = 0;
        animation_row2 = BOARD_HEIGHT - 1;
        animation_col2 = BOARD_WIDTH - 1;
        tiletime = 0;
    }

    public int get_ball_colour(String color) {
        switch (color) {
            case "orange": return 1;
            case "blue": return 2;
            case "green": return 3;
            case "yellow": return 4;
            default: return 0; 
        }
    }

    public void draw_topbar() {
        noStroke();
        fill(200);
        rect(0, 0, width, TOPBAR);
    
        fill(0);
        rect(10, 10, ball_bar_width, ball_bar_height);  
    
        int jk = 12;  
        int gap = 5;  
        int balls_on_topbar = (ball_bar_width - gap) / (27 + gap); 

        fill(0); 
        textSize(20);
        textAlign(RIGHT, BOTTOM);
        text("Score: " + score, width - 20, TOPBAR / 2);
    
        for (int i = 0; i < display_balls_ontop.size() && i < balls_on_topbar; i++) {
            image(display_balls_ontop.get(i), jk, 17, 27, 27); 
            jk += 27 + gap;  
        }
        fill(0); 
        textSize(20);  
        textAlign(RIGHT, TOP);
        text("Time: " + remainingtime + "s", width - 20, TOPBAR / 2);
        
        float next_spawn_seconds = get_next_spawn_seconds();
        String spawn_timer = getFormattedTime(next_spawn_seconds);
        
        textAlign(LEFT, CENTER);
        text(spawn_timer, 180, TOPBAR / 2);

        if(paused) {
            fill(0);
            textSize(22);
            textAlign(CENTER,CENTER);
            text("***Paused***", width / 2, TOPBAR / 2);
            noLoop();
        }
    }

    public float get_next_spawn_seconds() {
        if (paused || time_up) {
            return spawn_interwal / 1000.0f;
        }
        
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTime = currentTimeMillis - lastSpawnTime;
        long next_spawn_seconds = spawn_interwal - elapsedTime;
        
        if (next_spawn_seconds <= 0) {
            return 0;
        }
        
        return next_spawn_seconds / 1000.0f;
    }

    public String getFormattedTime(float time) {
        return nf(time, 1, 1) + "s";
    }
    
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(FPS);
        images_load();
        initializeballcolor();
        loadConfig();
        nextLevel();
    }

    public ArrayList<Line> getLines() {
        return lines;
    }
    
    public void markBallForRemoval(balls ball) {
        ballsToRemove.add(ball);
    }

    @Override
    public void draw() {
        background(255);

        int r = Game_Board.length;
        int c = Game_Board[0].length;

        for (int row = 0; row < r; row++) {
            for (int col = 0; col < c; col++) {
                int x = col * CELLSIZE;
                int y = row * CELLHEIGHT + BOARD_OFFSET_Y; 

                image(images_tm[12], x, y, CELLSIZE, CELLHEIGHT); 
            }
        }

        for (int row = 0; row < r; row++) {
            for (int col = 0; col < c; col++) {
                if (Game_Board[row][col] != null) {
                    Game_Board[row][col].draw();
                }
            }
        }
        
        for (hole h : holes) {
            h.draw();
        }

        for (balls ball : b) {
            int col = (int) (ball.getX() / CELLSIZE);
            int row = (int) ((ball.getY() - BOARD_OFFSET_Y) / CELLHEIGHT);

            if (row >= 0 && row < Game_Board.length && col >= 0 && col < Game_Board[0].length) {
                if (Game_Board[row][col] instanceof AccTile) {
                    ((AccTile) Game_Board[row][col]).applyAcceleration(ball);
                }
            }
            ball.draw();
        }

        for (Line li : lines) {
            li.draw();
        }
        if (currentLine != null) {
            currentLine.draw();
        }

        draw_topbar();

        if(!paused){
            long elapsedMillis = System.currentTimeMillis() - startTime;
            remainingtime = levelTime - (int) (elapsedMillis / 1000); 
        }

        if (remainingtime <= 0) {
            remainingtime = 0;
            time_up = true; 
        }

        if (!paused && !time_up && System.currentTimeMillis() - lastSpawnTime >= spawn_interwal) {
            spawnRandomBall();
            lastSpawnTime = System.currentTimeMillis();
        }

        b.removeAll(ballsToRemove);
        ballsToRemove.clear();

        if (b.isEmpty() && !level_end_ani && !animation_over && display_balls_ontop.isEmpty()) {
            level_end_ani = true; 
            tiletime = System.currentTimeMillis();
        }

        if (level_end_ani) {
            animation_starting();
            animation_draw();

            if (remainingtime > 0) {
                score += 1;
                remainingtime -= 0.067;
            }

            if (animation_row1 == 0 && animation_col1 == 0 && 
               animation_row2 == BOARD_HEIGHT - 1 && animation_col2 == BOARD_WIDTH - 1 && animation_over) {
                level_end_ani = false;
                nextLevel(); 
            }
        }

        if (paused || time_up) {
            return;
        }
    }

    public void animation_starting() {
        if (System.currentTimeMillis() - tiletime >= TILE_MOVE_INTERVAL) {
    
            if (animation_row1 == 0 && animation_col1 < BOARD_WIDTH - 1){
                animation_col1++;

            }else if (animation_col1 == BOARD_WIDTH - 1 && animation_row1 < BOARD_HEIGHT - 1){
                animation_row1++;

            }else if (animation_row1 == BOARD_HEIGHT - 1 && animation_col1 > 0) {
                animation_col1--;

            }else if (animation_col1 == 0 && animation_row1 > 0) 
                animation_row1--;
        
                

            if (animation_row2 == BOARD_HEIGHT - 1 && animation_col2 > 0) {
                animation_col2--;

            }else if (animation_col2 == 0 && animation_row2 > 0){
                 animation_row2--;

            }else if (animation_row2 == 0 && animation_col2 < BOARD_WIDTH - 1){
                 animation_col2++;

            }else if (animation_col2 == BOARD_WIDTH - 1 && animation_row2 < BOARD_HEIGHT - 1) {
                animation_row2++;
            }

            if (animation_row1 == 0 && animation_col1 == 0 && animation_row2 == BOARD_HEIGHT - 1 && animation_col2 == BOARD_WIDTH - 1) {
                animation_over = true;
            }

            tiletime = System.currentTimeMillis();
        }
    }

    public void animation_draw() {
        image(images_tm[17], animation_col1 * CELLSIZE, animation_row1 * CELLHEIGHT + BOARD_OFFSET_Y, CELLSIZE, CELLHEIGHT);
        image(images_tm[17], animation_col2 * CELLSIZE, animation_row2 * CELLHEIGHT + BOARD_OFFSET_Y, CELLSIZE, CELLHEIGHT);
    }

    public void game_end() {
        fill(0);
        textSize(20);
        textAlign(CENTER, CENTER);
        text("=== GAME ENDED ===", 336, TOPBAR / 2);
        noLoop();
    }

    public void ballcaptured(String ballColor) {
        int baseScore = calcinc.get(ballColor);
        int finalScore = (int) (baseScore * scoreIncreaseModifier);
        score += finalScore;
    }
    public void ballnotcaptured(String ballColor) {
        int basePenalty = calcdec.get(ballColor);
        int finalPenalty = (int) (basePenalty * scoreDecreaseModifier);
        score -= finalPenalty;
    }

    public void time_bonus() {
        int bonusScore = (int) (remainingtime * 15);
        score += bonusScore;
    }
 
    public boolean ret_pause(){
        return paused;
    }

    public void spawnRandomBall() {
        if (!display_balls_ontop.isEmpty()) {
            int spawnerIndex = random.nextInt(spawners.size());
            spawner selectedSpawner = spawners.get(spawnerIndex);
            PImage nextBallImage = display_balls_ontop.remove(0); 
            int nextColor = colour_ball_int.remove(0);
            balls newBall = new balls(selectedSpawner.ret_x_loc(), selectedSpawner.ret_y_loc(), nextBallImage, this, nextColor, images_tm, lines);
            b.add(newBall);
        }
    }

    public void restartGame() {
        present_level = -1;
        b.clear();
        lines.clear();
        holes.clear();
        spawners.clear();
        display_balls_ontop.clear();
        colour_ball_int.clear();
        score = 0;
        time_up = false;
        level_end_ani = false;
        animation_over = false;
        nextLevel();
    }
    public PImage[] getImagesTM() {
        return images_tm;
    }
    
    public ArrayList<balls> getBallsToRemove() {
        return ballsToRemove;
    }
    
    

    public int getScore() {
        return score;
    }

    public int getPresentLevel() {
        return present_level;
    }

    public ArrayList<balls> getBalls() {
        return b;
    }

    public boolean isPaused() {
        return paused;
    }

    public ArrayList<PImage> getDisplayBallsOnTop() {
        return display_balls_ontop;
    }


    public void keyPressed() {
        loop();
        if (key == 'r' || key == 'R') {
            restartGame();
        }
        if (key == ' ') {
            paused = !paused;
        }
    }

    @Override
    public void mousePressed() {
        if(!time_up && !level_end_ani){
            if (mouseButton == LEFT) {
                currentLine = new Line(this);
                currentLine.addPoint(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseDragged() {
        if(!time_up && !level_end_ani){
            if (currentLine != null) {
                currentLine.addPoint(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseReleased() {
        if(!time_up && !level_end_ani){
            if (currentLine != null) {
                lines.add(currentLine);
                currentLine = null;
            }
        }
    }

    public void line_remover(Line line_to_remove){
        lines.remove(line_to_remove);
    }

    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }
}
