import javax.swing.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        AirplanesGUI gui = new AirplanesGUI();
        //ArrayList<Airport> airportsArrayList = new ArrayList<>();

        World world = new World();

        Airport airportWarsaw = new Airport(449, 232, "Warsaw", gui, world);
        world.airportsArrayList.add(airportWarsaw);

        Airport airportZG = new Airport(93, 262, "Zielona Góra", gui, world);
        world.airportsArrayList.add(airportZG);

        Airport airportGdansk = new Airport(284, 43, "Gdansk", gui, world);
        world.airportsArrayList.add(airportGdansk);

        Airport airportSzczecin = new Airport(70, 142, "Szczecin", gui, world);
        world.airportsArrayList.add(airportSzczecin);

        Airport airportBydgoszcz = new Airport(275, 156, "Bydgoszcz", gui, world);
        world.airportsArrayList.add(airportBydgoszcz);

        Airport airportWroclaw = new Airport(201, 366, "Wroclaw", gui, world);
        world.airportsArrayList.add(airportWroclaw);

        Airport airportOpole = new Airport(266, 406, "Opole", gui, world);
        world.airportsArrayList.add(airportOpole);

        Airport airportKatowice = new Airport(321, 443, "Katowice", gui, world);
        world.airportsArrayList.add(airportKatowice);

        Airport airportLodz = new Airport(339, 282, "Lodz", gui, world);
        world.airportsArrayList.add(airportLodz);

        Airport airportKielce = new Airport(422, 385, "Kielce", gui, world);
        world.airportsArrayList.add(airportKielce);

        /*Airport airport1 = new Airport(50, 300, "Airport 1", gui, world);
        world.airportsArrayList.add(airport1);
        Airport airport2 = new Airport(150, 300, "2", gui, world);
        world.airportsArrayList.add(airport2);
        Airport airport3 = new Airport(250, 300, "3", gui, world);
        world.airportsArrayList.add(airport3);
        Airport airport4 = new Airport(350, 300, "4", gui, world);
        world.airportsArrayList.add(airport4);
        Airport airport5 = new Airport(450, 300, "5", gui, world);
        world.airportsArrayList.add(airport5);
        Airport airport6 = new Airport(550, 300, "6", gui, world);
        world.airportsArrayList.add(airport6);
        Airport airport7 = new Airport(650, 300, "7", gui, world);
        world.airportsArrayList.add(airport7);
        Airport airport8 = new Airport(50, 200, "8", gui, world);
        world.airportsArrayList.add(airport8);
        Airport airport9 = new Airport(50, 100, "9", gui, world);
        world.airportsArrayList.add(airport9);
        Airport airport10 = new Airport(150, 200, "10", gui, world);
        world.airportsArrayList.add(airport10);
        Airport airport11 = new Airport(250, 200, "11", gui, world);
        world.airportsArrayList.add(airport11);
        Airport airport12 = new Airport(350, 200, "12", gui, world);
        world.airportsArrayList.add(airport12);
        Airport airport13 = new Airport(450, 200, "13", gui, world);
        world.airportsArrayList.add(airport13);
        Airport airport14 = new Airport(550, 200, "14", gui, world);
        world.airportsArrayList.add(airport14);
        Airport airport15 = new Airport(650, 200, "15", gui, world);
        world.airportsArrayList.add(airport15);*/

        //Airplane airplane1 = new Airplane(airport1, "Airplane_1", 100, gui, world);
        //Airplane airplane2 = new Airplane(airport7, "Airplane_2", 500, gui, world);

        Airplane airplane1 = new Airplane(airportZG, "Airplane_1", 200, gui, world);
        //Airplane airplane2 = new Airplane(airportKatowice, "Airplane_2", 500, gui, world);

        world.airplanesArrayList.add(airplane1);
        world.availableAirplanesArrayList.add(airplane1);

        //world.airplanesArrayList.add(airplane2);
        //world.availableAirplanesArrayList.add(airplane2);

        Thread airplane1Thread = new Thread(airplane1);
        airplane1Thread.start();
        //Thread airplane2Thread = new Thread(airplane2);
        //airplane2Thread.start();
    }

}
