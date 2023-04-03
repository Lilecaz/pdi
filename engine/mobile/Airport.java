package engine.mobile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import engine.map.Block;

public class Airport extends MobileElement {
    private String name;
    private int capacity;
    private int currentCapacity;
    private List<Plane> planes;
    public static BufferedImage AirpPic;

    static {
        try {
            AirpPic = ImageIO.read(new File("images/airport.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Airport(Block position, int capacity, String name) {
        super(position);
        this.capacity = capacity;
        this.name = name;
        this.currentCapacity = 0;
    }

    public BufferedImage getAirpPic() {
        return AirpPic;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public boolean isFull() {
        return currentCapacity == capacity;
    }

    public void addPlane(Plane plane) {
        currentCapacity++;

        // planes.add(plane);
    }

    public void removePlane(Plane plane) {
        currentCapacity--;
    }

    public boolean isOnAirport(Block block) {
        return block.getX() == position.getX()
                && block.getY() == position.getY();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
