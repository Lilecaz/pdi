package engine.mobile;

import engine.map.Block;
import java.awt.image.BufferedImage;
import utils.ImageLoader;

public class Plane extends MobileElement {
    private String name;
    private int speed;
    private Airport destAirport;
    private Airport departureAirport;
    
    private int altitude;
    private boolean isLanded;
    private boolean emergency;
    
    // Logique Trajectoire
    private boolean boucle;
    private int iterBoucle;
    private int trajBlc;
    private int posCollision;

    // Images statiques
    public static BufferedImage PLANE_NORMAL = ImageLoader.getImage("images/plane.png");
    public static BufferedImage PLANE_UP = ImageLoader.getImage("images/planeup.png");
    public static BufferedImage PLANE_EMERGENCY = ImageLoader.getImage("images/emergencyPlane.png");

    public Plane(String name, Airport airport, Airport destination, int speed) {
        super(airport.getPosition(), destination.getPosition());
        this.name = name;
        this.departureAirport = airport;
        this.destAirport = destination;
        this.speed = speed;
        this.altitude = 0;
    }

    public BufferedImage getPlanePic() {
        if (emergency) return PLANE_EMERGENCY;
        return PLANE_NORMAL;
    }

    // --- Getters & Setters ---

    // [CORRECTION] Ajout de la méthode manquante
    public Block getDestination() {
        return destination;
    }

    public String getName() { return name; }
    public int getSpeed() { return speed; }
    
    public int getAltitude() { return altitude; }
    public void setAltitude(int altitude) { this.altitude = altitude; }
    
    public Airport getDestAirport() { return destAirport; }
    public void setDestAirport(Airport dest) { 
        this.destAirport = dest; 
        this.destination = dest.getPosition(); 
    }
    
    public boolean isLanded() { return isLanded; }
    public void setLanded(boolean isLanded) { this.isLanded = isLanded; }
    
    public boolean isEmergency() { return emergency; }
    public void setEmergency(boolean emergency) { this.emergency = emergency; }
    
    public boolean isBoucle() { return boucle; }
    public void setBoucle(boolean boucle) { this.boucle = boucle; }
    
    public int getIterBoucle() { return iterBoucle; }
    public void setIterBoucle(int iterBoucle) { this.iterBoucle = iterBoucle; }
    
    public int getTrajBlc() { return trajBlc; }
    public void setTrajBlc(int trajBlc) { this.trajBlc = trajBlc; }
    
    public int getPosCollision() { return posCollision; }
    public void setPosCollision(int posCollision) { this.posCollision = posCollision; }
    
    public boolean isOnPosition(Block block) {
        return block.getX() == position.getX() && block.getY() == position.getY();
    }

    // [CORRECTION] Ajout de la méthode de proximité pour l'InformationBoard
    public boolean CloseTo(Block block) {
        // Vérifie les 8 cases autour
        return (block.getX() == position.getX() && block.getY() == position.getY() + 1)
                || (block.getX() == position.getX() && block.getY() == position.getY() - 1)
                || (block.getX() == position.getX() + 1 && block.getY() == position.getY())
                || (block.getX() == position.getX() - 1 && block.getY() == position.getY())
                || (block.getX() == position.getX() + 1 && block.getY() == position.getY() + 1)
                || (block.getX() == position.getX() - 1 && block.getY() == position.getY() - 1)
                || (block.getX() == position.getX() + 1 && block.getY() == position.getY() - 1)
                || (block.getX() == position.getX() - 1 && block.getY() == position.getY() + 1);
    }
}