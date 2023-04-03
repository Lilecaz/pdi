package engine.mobile;

import engine.map.Block;

public class MobileElement {
    protected Block position;
    protected int speed;
    protected Block destination;

    public MobileElement(Block position) {
        this.position = position;
    }

    public MobileElement(String name, Block position) {
        this.position = position;
    }

    public MobileElement(Block position, Block destination) {
        this.position = position;
        this.destination = destination;
    }

    public Block getPosition() {
        return position;
    }

    public void setPosition(Block position) {
        this.position = position;
    }
}
