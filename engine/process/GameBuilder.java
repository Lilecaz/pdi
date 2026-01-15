package engine.process;

import java.io.IOException;
import java.util.List;
import engine.map.Block;
import engine.map.TestMap;
import engine.mobile.Plane;
import engine.mobile.Airport;

public class GameBuilder {

    public static TestMap buildMap() {
        return new TestMap(400, 400);
    }

    public static MobileElementManager buildMobileElementManager(TestMap map, int nbPlanes, int nbAirports) throws IOException {
        MobileElementManager manager = new MobileElementManager(map);
        initializeAirports(manager);
        initializePlanes(manager);
        return manager;
    }

    private static void initializeAirports(MobileElementManager manager) {
        // Capacités augmentées (20 ou 50) pour éviter le mode "Donut" immédiat
        manager.addAirport(new Airport(new Block(345, 345), 50, "Airport 1"));
        manager.addAirport(new Airport(new Block(0, 345), 50, "Airport 2"));
        manager.addAirport(new Airport(new Block(345, 0), 20, "Airport 3"));
        manager.addAirport(new Airport(new Block(13, 11), 50, "Airport 4"));
        manager.addAirport(new Airport(new Block(200, 200), 50, "Airport Central"));
    }

    private static void initializePlanes(MobileElementManager manager) throws IOException {
        List<Airport> airports = manager.getAirports();
        
        manager.addPlane(new Plane("AF 001", airports.get(0), airports.get(3), 15)); 
        manager.addPlane(new Plane("BA 002", airports.get(3), airports.get(0), 15)); 
        
        manager.addPlane(new Plane("LH 003", airports.get(1), airports.get(2), 15)); 
        manager.addPlane(new Plane("RY 004", airports.get(2), airports.get(1), 15)); 
        
        manager.addPlane(new Plane("EZ 005", airports.get(4), airports.get(0), 15)); 
        manager.addPlane(new Plane("AF 006", airports.get(4), airports.get(2), 15));
    }
}