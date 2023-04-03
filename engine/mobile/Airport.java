package engine.mobile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import engine.map.Block;

public class Airport extends MobileElement {
    private String name;
    private int capacity;
    private int currentCapacity;
    private List<Plane> planes;
    private boolean isLanding;
    private boolean planeHasLanded = false;
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
        this.planes = new LinkedList<>();
        this.isLanding = false;
        new Object();
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
        planes.add(plane);
    }

    public void removePlane(Plane plane) {
        currentCapacity--;
        planes.remove(plane);
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

    /**
     * Demande à un avion de se poser sur l'aéroport.
     * Si la piste est libre, l'avion est autorisé à atterrir immédiatement.
     * Sinon, l'avion attend que la piste se libère.
     * 
     * @param plane l'avion qui demande à atterrir
     * @return true si l'avion a pu atterrir, false sinon
     */
    public synchronized void land() throws InterruptedException {
        while (currentCapacity <= 0 || planeHasLanded) {
            wait();
        }
        currentCapacity--;
        planeHasLanded = true;
        notifyAll();
    }

    public synchronized void endLanding() {
        currentCapacity++;
        planeHasLanded = false;
        notifyAll();
    }

    public boolean isLanding() {
        return isLanding;
    }

    public void setLanding(boolean isLanding) {
        this.isLanding = isLanding;
    }

}
