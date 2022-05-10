package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    @Test
    void addAirplanes() {
        //Given//
        World world = new World();
        //When//
        world.addAirport(1, 1, "Airport");
        world.addAirplanes(10, 200, 200, "DFS");
        //Then//
        assertEquals(10, world.getAirplanesArrayList().size());
    }

    @Test
    void addAirport() {
        //Given//
        World world = new World();
        //When//
        world.addAirport(1, 1, "Airport");
        //Then//
        assertEquals(1, world.getAirportsArrayList().size());
    }

    @Test
    void checkDistance() {
        //Given//
        World world = new World();
        Airport initialAirport = new Airport(2, 2, "initialAirport", world);
        Airport finalAirport = new Airport(2, 1, "finalAirport", world);
        //When//
        double distance = world.checkDistance(initialAirport, finalAirport);
        //Then//
        assertEquals(1, distance);
    }

}