package gui;

import javax.swing.JPanel;
import java.awt.*;
import java.util.List;
import engine.mobile.Airport;
import engine.mobile.Plane;
import engine.process.MobileElementManager;

public class InformationBoard extends JPanel {

    private MobileElementManager manager;
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_ITEM = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_LOG = new Font("Monospaced", Font.PLAIN, 11);

    public InformationBoard(MobileElementManager manager) {
        this.manager = manager;
        this.setPreferredSize(new Dimension(300, 800));
        this.setOpaque(true); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- TITRE ---
        g2.setColor(Color.WHITE);
        g2.setFont(FONT_TITLE);
        g2.drawString("ÉTAT DES AÉROPORTS", 20, 30);
        g2.setColor(new Color(52, 152, 219));
        g2.fillRect(20, 35, 260, 2);

        // --- LISTE AÉROPORTS & AVIONS AU SOL ---
        int y = 60;
        List<Airport> airports = manager.getAirports();
        
        for (Airport airport : airports) {
            // Nom Aéroport
            g2.setColor(new Color(46, 204, 113)); // Vert
            g2.setFont(FONT_SUBTITLE);
            g2.drawString( airport.getName(), 20, y);
            y += 20;

            // Avions dans cet aéroport
            boolean hasPlane = false;
            g2.setColor(new Color(200, 200, 200)); // Gris clair
            g2.setFont(FONT_ITEM);

            for (Plane p : manager.getPlanes()) {
                // Si l'avion est atterri ET que sa destination était cet aéroport
                if (p.isLanded() && p.getDestAirport() == airport) {
                    g2.drawString(p.getName(), 20, y);
                    y += 15;
                    hasPlane = true;
                }
            }

            if (!hasPlane) {
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                g2.drawString("(Aucun avion au sol)", 20, y);
                y += 15;
            }
            
            y += 10; // Espace entre aéroports
        }

        // --- ZONE DE LOGS (COLLISIONS) ---
        // On dessine une boite en bas
        int logAreaHeight = 250;
        int logY = getHeight() - logAreaHeight - 20;
        
        g2.setColor(new Color(40, 40, 40));
        g2.fillRect(10, logY, 280, logAreaHeight);
        g2.setColor(Color.GRAY);
        g2.drawRect(10, logY, 280, logAreaHeight);

        // Titre Logs
        g2.setColor(Color.WHITE);
        g2.setFont(FONT_SUBTITLE);
        g2.drawString("HISTORIQUE TRAFIC", 20, logY - 10);

        // Affichage des messages
        g2.setFont(FONT_LOG);
        int textY = logY + 20;
        
        // On copie pour éviter les erreurs si la liste change pendant le dessin
        List<String> logs = new java.util.ArrayList<>(manager.getLogs());
        
        for (String log : logs) {
            // Couleur selon le type de message
            if (log.contains("Conflit")) g2.setColor(new Color(231, 76, 60)); // Rouge
            else if (log.contains("atterri")) g2.setColor(new Color(46, 204, 113)); // Vert
            else g2.setColor(Color.WHITE);

            g2.drawString(log, 15, textY);
            textY += 15;
            
            // Si on dépasse la boite, on arrête
            if (textY > getHeight() - 25) break;
        }
    }
}