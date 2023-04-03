package engine.mobile;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import engine.map.Block;

public class Plane extends MobileElement {

    private Block airport;
    private Block destination;
    private int trajBlc;
    private int currentAngle;
    private int iterBoucle;
    private boolean boucle;
    private int altitude;

    public int close;
    public BufferedImage p1 = ImageIO.read(new File("images/plane.png"));
    public BufferedImage p2 = ImageIO.read(new File("images/planeup.png"));
    private Airport destAirport;
    private int speed;
    public String path;
    private boolean isLanded;
    private String name;
    private boolean emergency;
    private int posCollision;
    public  BufferedImage planePic;
    public static BufferedImage AirpPic;

     static {
        try {
            AirpPic = ImageIO.read(new File("images/airport.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Plane(Block position) throws IOException {
        super(position);
    }

    public Plane(String name, Block airport, Block destination, int speed) throws IOException {
        super(airport, destination);
        this.name = name;
        this.airport = airport;
        this.destination = destination;
        this.speed = speed;
        this.boucle = false;
        this.iterBoucle = 0;
        close=0;
        this.planePic=p1;
    }

    public Plane(String name, Airport airport, Airport destination, int speed) throws IOException {
        super(airport.getPosition(), destination.getPosition());
        this.destAirport = destination;
        this.name = name;
        this.airport = airport.getPosition();
        this.destination = destination.getPosition();
        this.speed = speed;
        this.boucle = false;
        this.iterBoucle = 0;
        this.path = "images/plane.png";
        this.planePic=p1;

        close=0;
    }

    public BufferedImage getPlanePic() {
        return planePic;
    }

    public void setPlanePic(BufferedImage planePic) {
        this.planePic = planePic;
    }

    public void setLanded(boolean isLanded) {
        this.isLanded = isLanded;
    }

    public boolean isLanded() {
        return isLanded;
    }

    public void setDestination(Block destination) {
        this.destination = destination;
    }

    public Block getDestination() {
        return destination;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setAirport(Block airport) {
        this.airport = airport;
    }

    public Block getAirport() {
        return airport;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public boolean isOnPosition(Block block) {
        return block.getX() == position.getX()
                && block.getY() == position.getY();
    }

    public Airport getDestAirport() {
        return destAirport;
    }

    public void setDestAirport(Airport destAirport) {
        this.destAirport = destAirport;
        this.destination = destAirport.getPosition();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean CloseTo(Block block) {
        return (block.getX() == position.getX() && block.getY() == position.getY() + 1)
                || (block.getX() == position.getX() && block.getY() == position.getY() - 1)
                || (block.getX() == position.getX() + 1 && block.getY() == position.getY())
                || (block.getX() == position.getX() - 1 && block.getY() == position.getY())
                || (block.getX() == position.getX() + 1 && block.getY() == position.getY() + 1)
                || (block.getX() == position.getX() - 1 && block.getY() == position.getY() - 1)
                || (block.getX() == position.getX() + 1 && block.getY() == position.getY() - 1)
                || (block.getX() == position.getX() - 1 && block.getY() == position.getY() + 1);
    }

    public boolean CloseToPlane(List<Plane> planes, Plane plane) {
        for (Plane p : planes) {
            if (p != plane) {
                if (p != null) {
                    if (p.CloseTo(plane.getPosition())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getTrajBlc() {
        return trajBlc;
    }

    public void setTrajBlc(int trajBlc) {
        this.trajBlc = trajBlc;
    }

    public int getIterBoucle() {
        return iterBoucle;
    }

    public void setIterBoucle(int iterBoucle) {
        this.iterBoucle = iterBoucle;
    }

    public void setBoucle(boolean boucle) {
        this.boucle = boucle;
    }

    public boolean isBoucle() {
        return boucle;
    }

    public int getPosCollision() {
        return posCollision;
    }

    public void setPosCollision(int posCollision) {
        this.posCollision = posCollision;
    }
    public boolean isCloseToAnAirport(List<Airport> airports) {
        for (Airport airport : airports) {
            if (CloseTo(airport.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public int getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(int currentAngle) {
        this.currentAngle = currentAngle;
    }

    public void setAngle(int angle) {
        this.currentAngle = angle;
    }

    public void setAngle() {
        if (position.getX() < destination.getX()) {
            if (position.getY() < destination.getY()) {
                currentAngle = 45;
            } else if (position.getY() > destination.getY()) {
                currentAngle = 315;
            } else {
                currentAngle = 0;
            }
        } else if (position.getX() > destination.getX()) {
            if (position.getY() < destination.getY()) {
                currentAngle = 135;
            } else if (position.getY() > destination.getY()) {
                currentAngle = 225;
            } else {
                currentAngle = 180;
            }
        } else {
            if (position.getY() < destination.getY()) {
                currentAngle = 90;
            } else if (position.getY() > destination.getY()) {
                currentAngle = 270;
            }
        }
    }

    public Block getHitbox() {
        // the hitbox is bigger than the plane
        Block hitbox = new Block(position.getX(), position.getY());
        return hitbox;
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(position.getY(), position.getX(),
                1, 1);
    }

    public void setEmergency(boolean b) {
        this.emergency = b;
    }

    public boolean isEmergency() {
        return emergency;
    }

}
