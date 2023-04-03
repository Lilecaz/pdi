package engine.map;

public class Block {
    public int x;
    public int y;
    public int altrelief;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Block(int x, int y, int altrelief) {
        this.x = x;
        this.y = y;
        this.altrelief = altrelief;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAltrelief() {
        return altrelief;
    }

    public void setAltrelief(int altrelief) {
        this.altrelief = altrelief;
    }

    public String toString() {
        return "Block(" + x + ", " + y + ")";
    }

}
