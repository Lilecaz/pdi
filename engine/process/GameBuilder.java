package engine.process;

import javax.swing.JPanel;
import java.io.IOException;
import java.util.List;

import engine.map.Block;
import engine.map.TestMap;
import engine.mobile.Plane;
import engine.mobile.Airport;

public class GameBuilder extends JPanel {

    private List<Block> usualAirportPlace;

    public static TestMap buildMap() {
        return new TestMap(400, 400);
    }

    public static MobileElementManager buildMobileElementManager(TestMap map, int nbPlanes, int nbAirports) throws IOException {
        MobileElementManager manager = new MobileElementManager(map);
        initializeAirports(manager);
        initializePlanes(manager);

        return manager;
    }

    private static void initializePlanes(MobileElementManager manager) throws IOException {

        List<Airport> airports = manager.getAirports();
        manager.addPlane(new Plane("Plane n°1", airports.get(0), airports.get(2), 15));
        manager.addPlane(new Plane("Plane n°2", airports.get(2), airports.get(1), 30));
        manager.addPlane(new Plane("Plane n°3", airports.get(3), airports.get(2), 40));
        manager.addPlane(new Plane("Plane n°4", airports.get(1), airports.get(2), 30));
        manager.addPlane(new Plane("Plane n°5", airports.get(0), airports.get(1), 30));
        manager.addPlane(new Plane("Plane n°6", airports.get(3), airports.get(2), 30));

    }

    private static void initializeAirports(MobileElementManager manager) {

        manager.addAirport(new Airport(new Block(345, 345), 5, "Airport 1"));
        manager.addAirport(new Airport(new Block(0, 345), 10, "Airport 2"));
        manager.addAirport(new Airport(new Block(345, 0), 1, "Airport 3"));
        manager.addAirport(new Airport(new Block(13, 11), 10, "Airport 4"));
        // add airport at the midde of the map
        manager.addAirport(new Airport(new Block(200, 200), 15, "Airport 5"));

    }

}
