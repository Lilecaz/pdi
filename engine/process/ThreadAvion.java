package engine.process;

import engine.mobile.Plane;

public class ThreadAvion extends Thread {
    private Plane plane;
    private double priorityflight;
    private MobileElementManager manager;

    public ThreadAvion(Plane plane, MobileElementManager manager) {
        this.plane = plane;
        this.manager = manager;
        this.priorityflight = (Math.random() * 50);
    }

    public double getPriorityflight() {
        return priorityflight;
    }

    public void setPriorityflight(double priorityflight) {
        this.priorityflight = priorityflight;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(plane.getSpeed());
                if (plane.isLanded()) {
                    Thread.sleep(3000);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            manager.movePlane(plane);
        }
    }
}
