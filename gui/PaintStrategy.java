package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import engine.map.Block;
import engine.mobile.Airport;
import engine.mobile.Plane;

public class PaintStrategy {
    
    public PaintStrategy() {}

    public void paint(Graphics g, Plane plane, int panelW, int panelH, int mapW, int mapH) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Block pos = plane.getPosition();
        float scaleX = (float) panelW / mapW;
        float scaleY = (float) panelH / mapH;

        int x = (int) (pos.getY() * scaleX);
        int y = (int) (pos.getX() * scaleY);
        int size = (int) (25 * Math.min(scaleX, scaleY)); 
        size = Math.max(25, size); // Un peu plus gros pour bien voir

        // 1. Trajectoire
        if (!plane.isLanded() && plane.getDestination() != null) {
            int destX = (int) (plane.getDestination().getY() * scaleX);
            int destY = (int) (plane.getDestination().getX() * scaleY);
            g2.setColor(new Color(255, 255, 255, 50));
            g2.drawLine(x + size/2, y + size/2, destX + size/2, destY + size/2);
        }

        // 2. Avion
        BufferedImage img = plane.getPlanePic();
        if (img != null) {
            if (plane.isEmergency()) {
                g2.setColor(new Color(255, 0, 0, 150)); 
                g2.fillOval(x - 5, y - 5, size + 10, size + 10);
            }
            g2.drawImage(img, x, y, size, size, null);
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(x, y, size, size);
        }

        // 3. AFFICHAGE ALTITUDE & NOM
        // On dessine une étiquette propre
        String name = plane.getName();
        String alt = plane.getAltitude() + "m";
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int wName = fm.stringWidth(name);
        int wAlt = fm.stringWidth(alt);
        int wBox = Math.max(wName, wAlt) + 10;

        // Boite de fond semi-transparente
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(x + size, y - 10, wBox, 35, 5, 5);

        // Texte Nom (Blanc)
        g2.setColor(Color.WHITE);
        g2.drawString(name, x + size + 5, y + 5);

        // Texte Altitude (Cyan pour bien ressortir)
        // Si l'avion change d'altitude (évitement), on le met en Jaune
        if (plane.getAltitude() > 350 || plane.getAltitude() < 250 && !plane.isLanded()) {
             g2.setColor(Color.YELLOW); // Changement d'altitude actif !
        } else {
             g2.setColor(Color.CYAN);
        }
        g2.drawString(alt, x + size + 5, y + 20);
    }

    public void paint(Graphics g, Airport airport, int panelW, int panelH, int mapW, int mapH) {
        float scaleX = (float) panelW / mapW;
        float scaleY = (float) panelH / mapH;
        int x = (int) (airport.getPosition().getY() * scaleX);
        int y = (int) (airport.getPosition().getX() * scaleY);
        
        g.setColor(new Color(46, 204, 113)); 
        g.fillOval(x - 5, y - 5, 10, 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g.drawString(airport.getName(), x - 15, y + 20);
    }
}