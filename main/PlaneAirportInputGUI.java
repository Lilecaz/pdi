package main;

import javax.swing.*;

import gui.MainGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PlaneAirportInputGUI extends JFrame {
    private final JTextField planesInput;
    private final JTextField airportsInput;

    public PlaneAirportInputGUI() {
        // Set up the JFrame
        setTitle("Nombre d'avions et d'aéroports");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create input fields for number of planes and airports
        planesInput = new JTextField();
        airportsInput = new JTextField();

        // Create a button to submit the input
        JButton submitButton = new JButton("Au vol !");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numPlanes = Integer.parseInt(planesInput.getText());
                    int numAirports = Integer.parseInt(airportsInput.getText());
                    if (numPlanes <= 0 || numPlanes > 10) {
                        throw new IllegalArgumentException("Le nombre de avions doit être compris entre 1 et 10");
                    }
                    if (numAirports <= 0 || numAirports > 5) {
                        throw new IllegalArgumentException("Le nombre d'aéroports doit être compris entre 1 et 5");
                    }
                    MainGui gui = new MainGui("Air Traffic Control", numPlanes, numAirports);
                    Thread thread = new Thread(gui);
                    thread.start();
                    setVisible(false); // hide the input GUI after the OK button is clicked
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PlaneAirportInputGUI.this,
                            "Input invalide, veuillez entrer un nombre entier",
                            "Erreur input", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(PlaneAirportInputGUI.this,
                            ex.getMessage(),
                            "Erreur input", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Add components to the JFrame
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Nombre d'avions: "));
        inputPanel.add(planesInput);
        inputPanel.add(new JLabel("Nombre d'Aéroports: "));
        inputPanel.add(airportsInput);
        add(inputPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        // Show the JFrame
        setVisible(true);
    }

    public static void main(String[] args) {
        new PlaneAirportInputGUI();
    }
}
