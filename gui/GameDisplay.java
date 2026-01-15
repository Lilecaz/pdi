package gui;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import engine.map.TestMap;
import engine.mobile.Plane;
import engine.process.MobileElementManager;
import engine.mobile.Airport;
import utils.ImageLoader;

public class GameDisplay extends JPanel {

    private static BufferedImage bgImage = ImageLoader.getImage("images/map1.jpg");
    private PaintStrategy paintStrategy = new PaintStrategy();
    private MobileElementManager manager;
    private TestMap map;

    public GameDisplay(TestMap map, MobileElementManager manager) {
        this.map = map;
        this.manager = manager;

        // --- AJOUT : GESTION DU CLIC POUR URGENCE ---
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    private void handleClick(int mouseX, int mouseY) {
        // Calcul de l'échelle (car la carte est étirée)
        int mapW = map.getWidth();
        int mapH = map.getHeight();
        if (mapW == 0) mapW = 400; 
        if (mapH == 0) mapH = 400;

        float scaleX = (float) getWidth() / mapW;
        float scaleY = (float) getHeight() / mapH;

        // On cherche un avion proche du clic
        List<Plane> planes = manager.getPlanes(); // Thread-safe car CopyOnWriteArrayList
        for (Plane plane : planes) {
            // Conversion pos avion -> pos écran
            int px = (int) (plane.getPosition().getY() * scaleX); // Y map = X écran
            int py = (int) (plane.getPosition().getX() * scaleY); // X map = Y écran
            
            // Zone de clic (carré de 40x40 pixels autour de l'avion)
            if (Math.abs(mouseX - px) < 40 && Math.abs(mouseY - py) < 40) {
                toggleEmergency(plane);
                break; // On ne sélectionne qu'un seul avion
            }
        }
    }

    private void toggleEmergency(Plane p) {
        boolean newState = !p.isEmergency();
        p.setEmergency(newState);
        
        System.out.println("🚨 " + p.getName() + " : URGENCE " + (newState ? "ACTIVÉE" : "DÉSACTIVÉE"));

        if (newState) {
            // En urgence, on redirige vers l'aéroport central (souvent l'index 4 ou celui du milieu)
            // On cherche un aéroport 'Airport 4' ou 'Airport 5' s'il existe, sinon le premier
            Airport emergencyAirport = manager.getAirports().stream()
                .filter(a -> a.getName().contains("4") || a.getName().contains("5"))
                .findFirst()
                .orElse(manager.getAirports().get(0));
            
            p.setDestAirport(emergencyAirport);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(new Color(50, 80, 120));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        int mapW = map.getWidth();
        int mapH = map.getHeight();
        if (mapW == 0) mapW = 400;
        if (mapH == 0) mapH = 400;

        for (Airport airport : manager.getAirports()) {
            paintStrategy.paint(g, airport, getWidth(), getHeight(), mapW, mapH);
        }

        var planes = new ArrayList<>(manager.getPlanes());
        for (Plane plane : planes) {
            paintStrategy.paint(g, plane, getWidth(), getHeight(), mapW, mapH);
        }
    }
}