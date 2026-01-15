package gui;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import engine.map.TestMap;
import engine.process.GameBuilder;
import engine.process.MobileElementManager;

public class MainGui extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 900;

    // Couleurs du Thème
    public static final Color COLOR_BG = new Color(30, 30, 30);       // Gris foncé
    public static final Color COLOR_PANEL = new Color(45, 45, 45);    // Gris un peu plus clair
    public static final Color COLOR_TEXT = new Color(230, 230, 230);  // Blanc cassé
    public static final Color COLOR_ACCENT = new Color(52, 152, 219); // Bleu
    public static final Font FONT_UI = new Font("Segoe UI", Font.PLAIN, 14);

    private TestMap map;
    private GameDisplay dashboard;
    private InformationBoard infoboard;
    private MobileElementManager manager;
    private int userinput;
    private int ui2;
    private boolean paused = false;

    public MainGui(String title, int userinput, int ui2) throws IOException {
        super(title);
        this.userinput = userinput;
        this.ui2 = ui2;
        init();
    }

    private void init() throws IOException {
        // Configuration Fenêtre
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Layout Principal
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(COLOR_BG);
        setContentPane(contentPane);

        // 1. Initialisation Moteur
        map = GameBuilder.buildMap();
        manager = GameBuilder.buildMobileElementManager(map, userinput, ui2);
        
        // 2. Création des Panneaux
        dashboard = new GameDisplay(map, manager);
        infoboard = new InformationBoard(manager);
        
        // Stylisation du panneau d'info
        infoboard.setPreferredSize(new Dimension(300, HEIGHT));
        infoboard.setBackground(COLOR_PANEL);

        // 3. Barre d'outils (Haut)
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(COLOR_PANEL);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        JButton btnPause = createStyledButton("Pause / Reprendre");
        btnPause.addActionListener(e -> {
            paused = !paused;
            manager.setPaused(paused);
            btnPause.setText(paused ? "Reprendre" : "Pause");
            btnPause.setBackground(paused ? new Color(231, 76, 60) : COLOR_ACCENT);
        });
        toolbar.add(btnPause);

        // Assemblage
        contentPane.add(toolbar, BorderLayout.NORTH);
        contentPane.add(dashboard, BorderLayout.CENTER);
        contentPane.add(infoboard, BorderLayout.EAST);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    public void run() {
        boolean start = true;
        
        while (true) {
            try {
                Thread.sleep(16); // ~60 FPS pour l'affichage
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Démarrage initial des avions
            if (start) {
                // On lance les threads avions
                manager.getPlanes().forEach(manager::flight);
                start = false;
            }

            // Rafraîchissement graphique
            // Note: On repaint tout le Gui, Swing gère le double buffering
            dashboard.repaint();
            infoboard.repaint(); 
        }
    }
}