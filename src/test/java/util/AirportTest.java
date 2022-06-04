package util;

import org.junit.jupiter.api.Test;
import pathfinding.Node;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AirportTest {

    @Test
    void getNearAirports() {
        //Given//
        World world = new World();
        Airport airport = new Airport(1, 1, "Airport", world);
        world.addAirport(airport.getX(), airport.getY(), airport.getName());
        Airport airport1 = new Airport(2, 2, "Airport1", world);
        world.addAirport(airport1.getX(), airport1.getY(), airport1.getName());
        Airport airport2 = new Airport(1, 6, "Airport2", world);
        world.addAirport(airport2.getX(), airport2.getY(), airport2.getName());
        Airport airport3 = new Airport(10, 10, "Airport3", world);
        world.addAirport(airport3.getX(), airport3.getY(), airport3.getName());
        //When//
        ArrayList<Airport> nearAirports = airport.getNearAirports(5);
        //Then//
        assertAll(
                () -> assertEquals(2, nearAirports.size()),
                () -> assertTrue(nearAirports.contains(airport1)),
                () -> assertTrue(nearAirports.contains(airport2)),
                () -> assertFalse(nearAirports.contains(airport3))
        );
    }

    @Test
    void getNodes() {
        //Given//
        World world = new World();
        Airport airport = new Airport(1, 1, "Airport", world);
        world.addAirport(airport.getX(), airport.getY(), airport.getName());
        Airport airport1 = new Airport(2, 2, "Airport1", world);
        world.addAirport(airport1.getX(), airport1.getY(), airport1.getName());
        Airport airport2 = new Airport(1, 6, "Airport2", world);
        world.addAirport(airport2.getX(), airport2.getY(), airport2.getName());
        Airport airport3 = new Airport(10, 10, "Airport3", world);
        world.addAirport(airport3.getX(), airport3.getY(), airport3.getName());
        //When//
        ArrayList<Node> airportNodes = airport.getNodes(5);
        //Then//
        assertAll(
                () -> assertEquals(2, airportNodes.size()),
                () -> assertEquals(airport, airportNodes.get(0).getInitialAirport()),
                () -> assertEquals(airport1, airportNodes.get(0).getFinalAirport()),
                () -> assertEquals(airport, airportNodes.get(1).getInitialAirport()),
                () -> assertEquals(airport2, airportNodes.get(1).getFinalAirport())
        );
    }

    @Test
    void getSortedNodes() {
        //Given//
        World world = new World();
        Airport airport = new Airport(1, 1, "Airport", world);
        world.addAirport(airport.getX(), airport.getY(), airport.getName());
        Airport airport1 = new Airport(2, 2, "Airport1", world);
        world.addAirport(airport1.getX(), airport1.getY(), airport1.getName());
        Airport airport2 = new Airport(1, 6, "Airport2", world);
        world.addAirport(airport2.getX(), airport2.getY(), airport2.getName());
        Airport airport3 = new Airport(10, 10, "Airport3", world);
        world.addAirport(airport3.getX(), airport3.getY(), airport3.getName());
        Airport airport4 = new Airport(1, 0, "Airport4", world);
        world.addAirport(airport4.getX(), airport4.getY(), airport4.getName());
        //When//
        ArrayList<Node> airportNodes = airport.getSortedNodes(5);
        //Then//
        assertAll(
                () -> assertEquals(3, airportNodes.size()),
                () -> assertEquals(airport, airportNodes.get(0).getInitialAirport()),
                () -> assertEquals(airport4, airportNodes.get(0).getFinalAirport()),
                () -> assertEquals(airport, airportNodes.get(1).getInitialAirport()),
                () -> assertEquals(airport1, airportNodes.get(1).getFinalAirport()),
                () -> assertEquals(airport, airportNodes.get(2).getInitialAirport()),
                () -> assertEquals(airport2, airportNodes.get(2).getFinalAirport())
        );
    }
}