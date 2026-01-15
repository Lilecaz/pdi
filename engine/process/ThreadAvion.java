package engine.process;

import engine.mobile.Plane;

public class ThreadAvion extends Thread {
    private Plane plane;
    private MobileElementManager manager;
    private boolean running = true;
    
    // Priorité pour la gestion des collisions (plus c'est haut, plus il est prioritaire)
    private double priorityflight; 

    public ThreadAvion(Plane plane, MobileElementManager manager) {
        this.plane = plane;
        this.manager = manager;
        this.priorityflight = Math.random() * 100;
    }

    public double getPriorityflight() { return priorityflight; }

    @Override
    public void run() {
        while (running) {
            try {
                // 1. Gestion de la Pause (Synchronisation)
                manager.checkPause(); 

                // 2. Vitesse de simulation
                Thread.sleep(plane.getSpeed());

                // 3. Action : On demande au manager de nous bouger
                // C'est ici que la magie opère
                if (plane.isLanded()) {
                     // Temps d'escale
                    Thread.sleep(2000);
                }
                
                manager.movePlane(plane);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void stopThread() {
        this.running = false;
    }
}