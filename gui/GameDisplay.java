package gui;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import engine.map.TestMap;
import engine.mobile.Plane;
import engine.process.MobileElementManager;
import engine.mobile.Airport;

public class GameDisplay extends JPanel {

    public static BufferedImage bgImage;
    static {
        try {
            bgImage = ImageIO.read(new File("images/map1.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private PaintStrategy paintStrategy = new PaintStrategy(this);
    private MobileElementManager manager;

    public GameDisplay(TestMap map, MobileElementManager manager) {
        this.manager = manager;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);

        // Create a copy of the list of planes to avoid a
        // ConcurrentModificationException
        List<Plane> planesCopy = new ArrayList<>(manager.getPlanes());
        List<Airport> airportsCopy = new ArrayList<>(manager.getAirports());
        // Iterate over the copy of the list of planes
        for (Plane plane : planesCopy) {
            if (plane.isEmergency()) {
                paintStrategy.paintEmergency(g, plane);
                plane.setDestAirport(airportsCopy.get(4));
            } else {
                paintStrategy.paint(g, plane);
            }
            if (plane.isLanded()) {
                plane.setEmergency(false);
            }
        }
        for (Airport airport : manager.getAirports()) {
            paintStrategy.paint(g, airport);
        }
    }

}
