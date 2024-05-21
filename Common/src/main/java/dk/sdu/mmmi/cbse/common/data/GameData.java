package dk.sdu.mmmi.cbse.common.data;

public class GameData {

    private int displayWidth  = 800 ;
    private int displayHeight = 800;
    private final GameKeys keys = new GameKeys();

    private long delta = 0;
    private long frame = 0;

    private int score = 0;

    public GameKeys getKeys() {
        return keys;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    public long getDelta() {
        return delta;
    }

    public void setFrame(long frame) {
        this.frame = frame;
    }

    public long getFrame() {
        return frame;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore(int count){
        this.score += count;
    }
}
