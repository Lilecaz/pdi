package engine.process;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import engine.map.Block;
import engine.map.TestMap;
import engine.mobile.Airport;
import engine.mobile.Plane;

public class MobileElementManager {
    private TestMap map;
    private List<Plane> planes;
    private List<Airport> airports;
    
    // NOUVEAU : Liste des logs (Thread-safe)
    private List<String> logs = new CopyOnWriteArrayList<>();
    
    private HashMap<Plane, ThreadAvion> threadAvions = new HashMap<>();
    private boolean isPaused = false;
    private final Object pauseLock = new Object();

    public MobileElementManager(TestMap map) {
        this.map = map;
        this.airports = new ArrayList<>();
        this.planes = new CopyOnWriteArrayList<>();
    }

    public void addPlane(Plane plane) { planes.add(plane); }
    public void addAirport(Airport a) { airports.add(a); }
    
    public List<Plane> getPlanes() { return planes; }
    public List<Airport> getAirports() { return airports; }
    
    // NOUVEAU : Getter pour les logs
    public List<String> getLogs() { return logs; }

    // Ajoute un log en évitant les doublons consécutifs (Anti-spam)
    public void addLog(String message) {
        if (logs.isEmpty() || !logs.get(logs.size() - 1).equals(message)) {
            logs.add(message);
            // On garde seulement les 10 derniers logs pour pas saturer l'écran
            if (logs.size() > 10) logs.remove(0);
        }
    }

    public void flight(Plane plane) {
        ThreadAvion thread = new ThreadAvion(plane, this);
        threadAvions.put(plane, thread);
        thread.start();
    }
    
    public void setPaused(boolean paused) {
        synchronized (pauseLock) {
            this.isPaused = paused;
            if (!paused) pauseLock.notifyAll();
        }
    }

    public void checkPause() throws InterruptedException {
        synchronized (pauseLock) {
            while (isPaused) pauseLock.wait();
        }
    }

    // --- Mouvements ---

    public void movePlane(Plane plane) {
        if (plane.isLanded()) {
            handleLanding(plane);
            return;
        }

        Airport dest = plane.getDestAirport();
        if (!dest.isFull()) {
            moveStandard(plane);
        } else {
            moveHoldingPattern(plane);
        }
        handleCollisions(plane);
    }

    private void moveStandard(Plane plane) {
        Block current = plane.getPosition();
        Block target = plane.getDestination();
        
        int nextX = current.getX();
        int nextY = current.getY();
        if (current.getX() < target.getX()) nextX++;
        else if (current.getX() > target.getX()) nextX--;
        if (current.getY() < target.getY()) nextY++;
        else if (current.getY() > target.getY()) nextY--;

        updatePlanePosition(plane, nextX, nextY);

        if (plane.isOnPosition(target)) {
            plane.setLanded(true);
            plane.setAltitude(0);
            plane.getDestAirport().addPlane(plane);
            addLog(plane.getName() + " a atterri à " + plane.getDestAirport().getName());
        }
    }

    private void moveHoldingPattern(Plane plane) {
        if (!plane.isBoucle()) initBoucle(plane);

        int iter = plane.getIterBoucle();
        if (iter >= 120) {
            plane.setBoucle(false);
            plane.setIterBoucle(0);
        } else {
            TrajectBoucle traject = new TrajectBoucle(plane.getPosition(), map, iter);
            Block nextPos = plane.getPosition();
            switch(plane.getTrajBlc()) {
                case 1: nextPos = traject.T1(); break;
                case 2: nextPos = traject.T2(); break;
                case 3: nextPos = traject.T3(); break;
                case 4: nextPos = traject.T4(); break;
            }
            plane.setPosition(nextPos);
            plane.setAltitude(300 + nextPos.getAltrelief());
            plane.setIterBoucle(iter + 1);
        }
    }

    private void handleLanding(Plane plane) {
        Airport currentAirport = plane.getDestAirport();
        currentAirport.endLanding(); 
        currentAirport.removePlane(plane);
        
        Airport newDest = currentAirport;
        while (newDest == currentAirport) {
            newDest = airports.get((int) (Math.random() * airports.size()));
        }
        
        plane.setDestAirport(newDest);
        plane.setLanded(false);
        addLog(plane.getName() + " décolle vers " + newDest.getName());
    }

    private void updatePlanePosition(Plane plane, int x, int y) {
        Block nextBlock = map.getBlock(x, y);
        plane.setPosition(nextBlock);
        plane.setAltitude(300 + nextBlock.getAltrelief());
    }
    
    private void initBoucle(Plane plane) {
        Block pos = plane.getPosition();
        Block dest = plane.getDestination();
        int type = 4;
        if (pos.x <= dest.x && pos.y <= dest.y) type = 1;
        else if (pos.x > dest.x && pos.y >= dest.y) type = 2;
        else if (pos.x < dest.x && pos.y > dest.y) type = 3;
        
        plane.setTrajBlc(type);
        plane.setBoucle(true);
        plane.setIterBoucle(0);
    }

    private void handleCollisions(Plane plane) {
        for (Plane other : planes) {
            if (other == plane) continue;
            
            double dist = Math.sqrt(Math.pow(plane.getPosition().x - other.getPosition().x, 2) + 
                                    Math.pow(plane.getPosition().y - other.getPosition().y, 2));

            if (dist < 10) {
                ThreadAvion t1 = threadAvions.get(plane);
                ThreadAvion t2 = threadAvions.get(other);
                
                if (t1 != null && t2 != null) {
                    if (t1.getPriorityflight() > t2.getPriorityflight()) {
                        plane.setAltitude(Math.min(1000, plane.getAltitude() + 50));
                    } else {
                        plane.setAltitude(Math.max(100, plane.getAltitude() - 50));
                    }
                }
            } else {
                int targetAlt = 300 + plane.getPosition().getAltrelief();
                if (Math.abs(plane.getAltitude() - targetAlt) > 5) {
                    if (plane.getAltitude() > targetAlt) plane.setAltitude(plane.getAltitude() - 2);
                    else plane.setAltitude(plane.getAltitude() + 2);
                }
            }
        }
    }

    public Plane getPlanebyName(String planeName) {
        for (Plane plane : planes) {
            if (plane.getName().equals(planeName)) return plane;
        }
        return null;
    }
}