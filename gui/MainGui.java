package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import engine.map.TestMap;
import engine.process.GameBuilder;
import engine.process.MobileElementManager;

public class MainGui extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;
    private TestMap map;
    private final static int WIDTH = 1200;
    private final static int HEIGHT = 850;
    private GameDisplay dashboard;
    private InformationBoard infoboard;
    private MobileElementManager manager;
    private int userinput;
    private int ui2;
    private boolean paused = false;

    public MainGui(String title, int userinput, int ui2) throws IOException {
        super(title);
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.userinput = userinput;
        this.ui2 = ui2;

        init();
    }

    private void init() throws IOException {
        Container contentPane = this.getContentPane();

        map = GameBuilder.buildMap();
        manager = GameBuilder.buildMobileElementManager(map, userinput, ui2);
        dashboard = new GameDisplay(map, manager);
        infoboard = new InformationBoard(manager);

        // Create the pause/resume button
        JButton pauseResumeButton = new JButton("Pause/Resume");
        pauseResumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paused = !paused;
            }
        });

        contentPane.add(pauseResumeButton, "North");
        contentPane.add(dashboard, "Center");
        contentPane.add(infoboard, "East");

        dashboard.setPreferredSize(new Dimension(800, 800));
        infoboard.setPreferredSize(new Dimension(400, 800));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setResizable(true);

    }

    @Override
    public void run() {
        boolean start = true;
        infoboard.init();
        while (true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Pause/resume all threads
            while (paused) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            List<engine.mobile.Plane> planes = manager.getPlanes();
            if (start == true) {
                for (engine.mobile.Plane plane : planes) {
                    manager.flight(plane);
                }
                start = false;
            }
            infoboard.updateInfos();
            infoboard.update();
            // manager.movePlanes();
            dashboard.repaint();
        }
    }
}
