package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AirplaneTest {

    private final double polandMinFuel = 160.0;

    /**
     * @return World with all airports in Poland
     */
    private World poland() {
        World world = new World();
        world.addAirport(101, 319, "Zielona Góra");
        world.addAirport(81, 238, "Gorzów Wielokopolski");
        world.addAirport(44, 163, "Szczecin");
        world.addAirport(301, 71, "Gdańsk");
        world.addAirport(413, 138, "Olsztyn");
        world.addAirport(578, 203, "Białystok");
        world.addAirport(443, 286, "Warszawa");
        world.addAirport(253, 196, "Bydgoszcz");
        world.addAirport(299, 209, "Toruń");
        world.addAirport(189, 272, "Poznań");
        world.addAirport(348, 346, "Łódź");
        world.addAirport(200, 399, "Wrocław");
        world.addAirport(252, 455, "Opole");
        world.addAirport(319, 498, "Katowice");
        world.addAirport(376, 501, "Kraków");
        world.addAirport(516, 517, "Rzeszów");
        world.addAirport(422, 385, "Kielce");
        world.addAirport(545, 393, "Lublin");
        return world;
    }

    @Test
    void setTarget() {
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        Airplane airplane = new Airplane(initialAirport, "Airplane", polandMinFuel, "DIJKSTRA", world);
        //When//
        airplane.setTarget(finalAirport);
        //Then//
        assertEquals(finalAirport, airplane.getAirport());
    }

    @Test
    void run() {
        //Given//
        World world = poland();
        Airport airport = world.getAirportsArrayList().get(0);
        Airplane airplane = new Airplane(airport, "Airplane", polandMinFuel, "DIJKSTRA", world);
        Thread airplaneThread = new Thread(airplane);
        //When//
        airplaneThread.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        airplane.setGenerateNewTargets(false);
        //Then//
        assertNotNull(airplane.getTargetAirport());
    }
}