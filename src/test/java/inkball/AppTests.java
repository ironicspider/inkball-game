package inkball;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

public class AppTests {

    App app;  

   
    @BeforeEach
    public void setup() {
        app = new App(); 
        PApplet.runSketch(new String[]{"App"}, app); 
        app.setup();
    }

    @Test
    public void testAppInitialization() {
        assertNotNull(app.configPath, "Config path should not be null after setup.");
    }

    @Test
    public void testLoadConfig() {
        app.loadConfig();
        assertNotNull(app.levels, "Levels should be loaded from the config.");
        assertTrue(app.levels.size() > 0, "At least one level should be loaded.");
    }

    @Test
    public void testInitializeBallColor() {
        app.initializeballcolor();
        assertEquals(5, app.ballcolor.size(), "There should be exactly 5 ball colors initialized.");
    }

    @Test
    public void testNextLevel() {
        app.loadConfig();
        app.nextLevel();
        assertNotNull(app.currentLevel, "Current level should not be null after loading.");
        assertTrue(app.getPresentLevel() >= 0, "The present level should be non-negative.");
    }

    @Test
    public void testSpawnBall() {
        app.loadConfig();
        app.initializeballcolor();
        app.nextLevel();

        int initialBallCount = app.getBalls().size();
        app.spawnRandomBall();
        assertTrue(app.getBalls().size() > initialBallCount, "A new ball should be added to the game.");
    }


    @Test
    public void testBallCaptured() {
        app.loadConfig();
        app.initializeballcolor();
        app.nextLevel();

        int initialScore = app.getScore(); 
        app.ballcaptured("orange");
        assertTrue(app.getScore() > initialScore, "Score should increase after capturing a ball.");
    }

    @Test
    public void testBallNotCaptured() {
        app.loadConfig();
        app.initializeballcolor();
        app.nextLevel();

        int initialScore = app.getScore(); 
        app.ballnotcaptured("grey");
        assertTrue(app.getScore() < initialScore, "Score should decrease for capturing the wrong ball.");
    }

    @Test
    public void testPauseFunctionality() {
        assertFalse(app.isPaused(), "Game should not be paused initially.");
        app.keyPressed(); 
        assertTrue(app.isPaused(), "Game should be paused after pressing space.");
    }

    @Test
    public void testDrawTopBar() {
        app.draw_topbar();
        assertTrue(true, "Drawing the top bar should not cause any exceptions.");
    }

    @Test
    public void testRestartGame() {
        app.loadConfig();
        app.initializeballcolor();
        app.nextLevel();

        app.restartGame();
        assertEquals(-1, app.getPresentLevel(), "Level index should be reset after restarting.");
        assertTrue(app.getBalls().isEmpty(), "Balls list should be cleared after restarting.");
    }

    @Test
    public void testDisplayBallsOnTop() {
        app.loadConfig();
        app.initializeballcolor();
        app.nextLevel();

        assertFalse(app.getDisplayBallsOnTop().isEmpty(), "Display balls on top should not be empty.");
    }
}
