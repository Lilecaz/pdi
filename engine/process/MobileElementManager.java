package engine.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.CopyOnWriteArrayList;

import engine.map.Block;
import engine.map.TestMap;
import engine.mobile.Airport;
import engine.mobile.Plane;

public class MobileElementManager {
    private TestMap map;

    private List<Plane> planes = new ArrayList<Plane>();
    private List<Airport> airports = new ArrayList<Airport>();

    private Lock lock;
    HashMap<Plane, ThreadAvion> threadAvions = new HashMap<Plane, ThreadAvion>();

    public MobileElementManager() {
        airports = new ArrayList<Airport>();
        planes = new CopyOnWriteArrayList<Plane>(); // remplacer ArrayList par CopyOnWriteArrayList
    }

    public MobileElementManager(TestMap map) {
        this.map = map;
        lock = new ReentrantLock();
    }

    public void addPlane(Plane plane) {
        lock.lock();
        try {
            planes.add(plane);
        } finally {
            lock.unlock();
        }

    }

    public void flight(Plane plane) {
        lock.lock();
        try {
            ThreadAvion thread = new ThreadAvion(plane, this);
            threadAvions.put(plane, thread);
            thread.start();
        } finally {
            lock.unlock();
        }

    }

    public boolean isCloseAndRaiseAltitude(Plane plane1, Plane plane2) {
        // Calculate the distance between the two planes
        int distance = Math.abs(plane1.getPosition().getX() - plane2.getPosition().getX())
                + Math.abs(plane1.getPosition().getY() - plane2.getPosition().getY());

        // If the distance is less than or equal to 100 blocks, raise the altitude of
        // the first plane
        if (distance <= 100) {
            int currentAltitude = plane1.getAltitude();
            plane1.setAltitude(currentAltitude + 100);
            return true; // Return true to indicate that the planes are close
        }

        return false; // Return false to indicate that the planes are not close
    }

    public void movePlane(Plane plane) {
        lock.lock();
        int altitude = 300;
        try {
            Airport airDest = plane.getDestAirport();
            // The plane do a looping
            if (plane.isBoucle()) {
                int iter = plane.getIterBoucle();
                if (iter == 120) {
                    plane.setBoucle(false);
                    plane.setIterBoucle(0);
                } else {
                    // Get the current position of the plane
                    Block currentPosition = plane.getPosition();
                    Block newPosition = currentPosition;
                    TrajectBoucle trajectBoucle = new TrajectBoucle(currentPosition, map, iter);
                    if (plane.getTrajBlc() == 1) {
                        newPosition = trajectBoucle.T1();
                    } else if (plane.getTrajBlc() == 2) {
                        newPosition = trajectBoucle.T2();
                    } else if (plane.getTrajBlc() == 3) {
                        newPosition = trajectBoucle.T3();
                    } else if (plane.getTrajBlc() == 4) {
                        newPosition = trajectBoucle.T4();
                    }
                    // Set the new position of the plane
                    plane.setPosition(newPosition);
                    // Set the new altitude of the plane
                    int altRelief = newPosition.getAltrelief();
                    altitude = altitude + altRelief;
                    plane.setAltitude(altitude);
                    // Set iter of the Boucle
                    plane.setIterBoucle(plane.getIterBoucle() + 1);
                }
            }
            // Si l'avion est à destination
            else if (plane.isLanded()) {
                // utiliser les methodes land et endLanding de la classe Airport
                try {
                    airDest.land();
                    System.out.println("L'avion " + plane.getName() + " a decole de l'aeroport "
                            + airDest.getName() + " à la position " + airDest.getPosition()
                            + " et il a notifié les autres qu'il est parti !");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                airDest.endLanding();
                // Si l'avion est à l'atterrissage
                airDest.removePlane(plane);
                plane.setAltitude(0);
                Airport rdmAprt = airports.get((int) (Math.random() * airports.size()));
                while (rdmAprt == plane.getDestAirport()) {
                    rdmAprt = airports.get((int) (Math.random() * airports.size()));
                }
                plane.setDestAirport(rdmAprt);
                plane.setLanded(false);

            } else if (!plane.isLanded() && !airDest.isFull()) {
                // Get the current position of the plane
                Block currentPosition = plane.getPosition();
                // Get the destination airport of the plane
                Block destinationAirport = plane.getDestination();
                // Get the line and column of the current position
                int currentLine = currentPosition.getX();
                int currentColumn = currentPosition.getY();
                // Get the line and column of the destination airport
                int destinationLine = destinationAirport.getX();
                int destinationColumn = destinationAirport.getY();
                // Calculate the new line and column of the plane
                int newLine = currentLine;
                int newColumn = currentColumn;
                if (currentLine < destinationLine) {
                    newLine = currentLine + 1;
                } else if (currentLine > destinationLine) {
                    newLine = currentLine - 1;
                }
                if (currentColumn < destinationColumn) {
                    newColumn = currentColumn + 1;
                } else if (currentColumn > destinationColumn) {
                    newColumn = currentColumn - 1;
                }
                // Get the new position of the plane
                Block newPosition = map.getBlock(newLine, newColumn);
                // Set the new position of the plane
                plane.setPosition(newPosition);
                // Set the new altitude of the plane
                int altRelief = newPosition.getAltrelief();
                altitude = altitude + altRelief;
                plane.setAltitude(altitude);
                // If the plane is on the destination airport, set the plane
                // landed
                if (plane.isOnPosition(destinationAirport)) {
                    plane.setLanded(true);
                    plane.setAltitude(0);
                    airDest.addPlane(plane);
                }
            }
            // Traject of the plane if the airport is full
            else if (!plane.isLanded() && plane.getDestAirport().isFull()) {
                // Get the current position of the plane
                Block currentPosition = plane.getPosition();
                // Get the destination airport of the plane
                Block destinationAirport = plane.getDestination();
                //
                if (currentPosition.x <= destinationAirport.x && currentPosition.y <= destinationAirport.y) {
                    plane.setTrajBlc(1);
                } else if (currentPosition.x > destinationAirport.x && currentPosition.y >= destinationAirport.y) {
                    plane.setTrajBlc(2);
                } else if (currentPosition.x < destinationAirport.x && currentPosition.y > destinationAirport.y) {
                    plane.setTrajBlc(3);
                } else if (currentPosition.x >= destinationAirport.x && currentPosition.y < destinationAirport.y) {
                    plane.setTrajBlc(4);
                }
                plane.setIterBoucle(0);
                //
                plane.setBoucle(true);
            }

            // Verifier si il ya un croisement entre avion
            for (Plane plane1 : planes) {
                if (plane1 == plane) {
                    continue;
                }
                int dX = plane.getPosition().x - plane1.getPosition().x;
                int dY = plane.getPosition().y - plane1.getPosition().y;
                double dist = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
                // changement d'altitude en cas de proximité dangereuse
                if (dist < 50) {
                    while (true) {

                        if (threadAvions.get(plane).getPriorityflight() > threadAvions.get(plane1)
                                .getPriorityflight()) {
                            plane.setAltitude(plane.getAltitude() + 50);
                            plane1.setPosCollision(-1);
                            break;
                        } else if (threadAvions.get(plane).getPriorityflight() < threadAvions.get(plane1)
                                .getPriorityflight()) {
                            plane.setAltitude(plane.getAltitude() - 50);
                            plane1.setPosCollision(1);
                            break;
                        } else if (threadAvions.get(plane).getPriorityflight() == threadAvions.get(plane1)
                                .getPriorityflight()) {
                            threadAvions.get(plane).setPriorityflight((Math.random() * 50));
                            continue;
                        }
                    }
                } else if (dist >= 50) {
                    plane1.setPosCollision(0);
                }
            }
            for (int i = 0; i < planes.size(); i++) {
                for (int j = 0; j < i; j++) {
                    Plane plane1 = planes.get(i);
                    Plane plane2 = planes.get(j);
                    int distance = Math.abs(plane1.getPosition().getX() - plane2.getPosition().getX())
                            + Math.abs(plane1.getPosition().getY() - plane2.getPosition().getY());
                    // If the distance is less than or equal to 100 blocks, raise the altitude of
                    // the first plane
                    if (distance <= 10 && plane1.close == 0) {
                        // plane1.setPlanePic(plane1.p1);
                        // plane2.setPlanePic(plane1.p1);
                        plane1.close = 1;
                        plane1.setAltitude(plane1.getAltitude() + 100);
                        // Return true to indicate that the planes are close
                    }
                    if (distance > 10 && plane1.close == 1) {
                        // plane1.setPlanePic(plane1.p2);
                        // plane2.setPlanePic(plane1.p1);

                        plane1.close = 0;
                    }
                }
            }
        } finally {
            lock.unlock();
        }

    }

    public void addAirport(Airport airport) {
        airports.add(airport);
    }

    public void removePlane(Plane plane) {
        planes.remove(plane);
    }

    public void removeAirport(Airport airport) {
        airports.remove(airport);
    }

    public List<Plane> getPlanes() {
        return planes;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public TestMap getMap() {
        return map;
    }

    public void setMap(TestMap map) {
        this.map = map;
    }

    public Plane getPlanebyName(String planeName) {
        for (Plane plane : planes) {
            if (plane.getName().equals(planeName)) {
                return plane;
            }
        }
        return null;
    }

}
