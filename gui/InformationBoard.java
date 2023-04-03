package gui;

import engine.mobile.Airport;
import engine.mobile.Plane;
import engine.process.MobileElementManager;

import java.awt.*;

import javax.swing.*;

public class InformationBoard extends JPanel {
    private MobileElementManager manager;
    private JLabel label;
    private JComboBox<String> comboBox;
    private int counter;

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 600;

    private JPanel topPanel; // Panel for displaying text from init()
    private JPanel bottomPanel; // Panel for displaying text from updateInfos()

    public InformationBoard(MobileElementManager manager) {
        this.manager = manager;
        this.counter = 0;

        // Initialize the two panels
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        // Create a split pane to divide the InformationBoard panel into two halves
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setResizeWeight(0.15); // Set the initial size of the two panels

        // Add the split pane to the InformationBoard panel
        this.setLayout(new BorderLayout());
        this.add(splitPane, BorderLayout.CENTER);
    }

    public void init() {
        this.label = new JLabel("Information Board");
        this.setSize(WIDTH, HEIGHT);

        // Add the label to the top panel
        topPanel.add(label);

        // Create the combobox and populate it with plane names
        comboBox = new JComboBox<>();
        for (Plane plane : manager.getPlanes()) {
            if (plane == null) {
                continue;
            }
            String planeName = plane.getName();
            comboBox.addItem(planeName);
        }

        // Add the combobox to the top panel
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.add(new JLabel("Select plane: "));
        comboBoxPanel.add(comboBox);
        topPanel.add(comboBoxPanel);

        // Add the panel for displaying plane information to the top panel
        JPanel planeInfoPanel = new JPanel();
        planeInfoPanel.setLayout(new BoxLayout(planeInfoPanel, BoxLayout.Y_AXIS));
        topPanel.add(planeInfoPanel);

        // Add an action listener to the combobox
        comboBox.addActionListener(e -> {
            String planeName = (String) comboBox.getSelectedItem();
            Plane plane = manager.getPlanebyName(planeName);
            if (plane == null) {
                return;
            }
            // Update the plane information panel
            plane.setEmergency(true);
            if (plane.isLanded()) {
                planeInfoPanel.removeAll();
                plane.setEmergency(false);
                planeInfoPanel.add(new JLabel("Selected plane: " + plane.getName()));
                planeInfoPanel.revalidate();
                planeInfoPanel.repaint();
            } else {
                planeInfoPanel.removeAll();
                planeInfoPanel.add(new JLabel("Selected plane: " + plane.getName()));
                planeInfoPanel.add(new JLabel("Altitude: " + plane.getAltitude() + " m"));
                planeInfoPanel.revalidate();
                planeInfoPanel.repaint();
            }
        });
    }

    public void updateInfos() {
        // Update information about planes in the panel

        for (Plane plane : manager.getPlanes()) {
            if (plane == null) {
                continue;
            } else {
                if (plane.CloseTo(plane.getDestination())) {
                    this.bottomPanel
                            .add(new JLabel(plane.getName() + " has arrived at " + plane.getDestAirport().getName()));
                    this.counter++;
                }
                for (Airport airport : manager.getAirports()) {
                    if (plane.CloseTo(airport.getPosition()) && airport != plane.getDestAirport()) {
                        this.bottomPanel.add(
                                new JLabel(plane.getName() + " is now heading to " + plane.getDestAirport().getName()));
                        this.counter++;
                    }
                }
            }
        }
        if (this.counter == 30) {
            this.bottomPanel.removeAll();
            this.counter = 0;

        }

    }

    public void update() {
        this.add(label);
        this.revalidate();
        this.repaint();
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public JLabel getLabel() {
        return label;
    }

    public MobileElementManager getManager() {
        return manager;
    }
}
