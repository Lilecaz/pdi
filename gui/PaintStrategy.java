package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import engine.map.Block;
import engine.map.TestMap;
import engine.mobile.Plane;
import engine.mobile.Airport;

public class PaintStrategy {

    public static BufferedImage planePic, AirpPic;
    public int taille = 2;
    public GameDisplay gd;

    public PaintStrategy(GameDisplay gameDisplay) {
        this.gd = gameDisplay;
    }

    public void paint(Graphics g, TestMap map) {
        Block[][] blocks = map.getBlocks();
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                Block block = blocks[i][j];
                g.setColor(Color.BLACK);
                g.drawRect(block.getX() * taille, block.getY() * taille, taille, taille);
            }
        }
    }

    public void paint(Graphics g, Plane plane) {
        if (plane == null) {
            return;
        }
        planePic = plane.getPlanePic();
        Block block = plane.getPosition();
        g.drawImage(planePic, block.getX() * taille, block.getY() * taille, gd);
        // add the altitude of the plane to the image
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(plane.getAltitude()), block.getX() * taille, block.getY() * taille);
        // g.fillRect(block.getLine() * 50, block.getColumn() * 50, 50, 50);
    }

    public void paintEmergency(Graphics g, Plane plane) {
        if (plane == null) {
            return;
        }
        try {
            planePic = ImageIO.read(new File("images/emergencyPlane.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Block block = plane.getPosition();
        g.drawImage(planePic, block.getX() * taille, block.getY() * taille, gd);
        // add the altitude of the plane to the image
        g.setColor(Color.RED);
        g.drawString(Integer.toString(plane.getAltitude()), block.getX() * taille, block.getY() * taille);
        // add a red cross to the image
        g.setColor(Color.RED);
        g.drawLine(block.getX() * taille, block.getY() * taille, block.getX() * taille + taille,
                block.getY() * taille + taille);
        g.drawLine(block.getX() * taille, block.getY() * taille + taille, block.getX() * taille + taille,
                block.getY() * taille);

    }

    public void paint(Graphics g, List<Plane> planes) {
        for (Plane plane : planes) {
            paint(g, plane);
        }
    }

    public void paint(Graphics g, Airport airport) {
        if (airport == null) {
            return;
        }
        AirpPic = airport.getAirpPic();
        Block block = airport.getPosition();
        g.drawImage(AirpPic, block.getX() * taille, block.getY() * taille, gd);
        // add the capacity of the airport to the image
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(airport.getCapacity() - airport.getCurrentCapacity()), block.getX() * taille,
                block.getY() * taille);
        // g.fillRect(block.getLine() * taille, block.getColumn() * taille, taille,
        // taille);
    }
}
