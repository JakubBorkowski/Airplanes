
public class Main {

    public static void main(String[] args) {
        AirplanesGUI gui = new AirplanesGUI();
        World world = new World();

        //Creating airports//
        world.airportsArrayList.add(new Airport(101, 319, "Zielona Góra", gui, world));
        //world.airportsArrayList.add(new Airport(81, 238, "Gorzów Wielokopolski", gui, world));
        world.airportsArrayList.add(new Airport(37, 170, "Szczecin", gui, world));
        world.airportsArrayList.add(new Airport(301, 71, "Gdańsk", gui, world));
        world.airportsArrayList.add(new Airport(413, 138, "Olsztyn", gui, world));
        world.airportsArrayList.add(new Airport(578, 203, "Białystok", gui, world));
        world.airportsArrayList.add(new Airport(448, 293, "Warszawa", gui, world));
        world.airportsArrayList.add(new Airport(259, 198, "Bydgoszcz", gui, world));
        //world.airportsArrayList.add(new Airport(298, 213, "Toruń", gui, world));
        world.airportsArrayList.add(new Airport(184, 274, "Poznań", gui, world));
        world.airportsArrayList.add(new Airport(348, 346, "Łódź", gui, world));
        world.airportsArrayList.add(new Airport(195, 410, "Wrocław", gui, world));
        world.airportsArrayList.add(new Airport(252, 455, "Opole", gui, world));
        //world.airportsArrayList.add(new Airport(319, 498, "Katowice", gui, world));
        world.airportsArrayList.add(new Airport(378, 517, "Kraków", gui, world));
        world.airportsArrayList.add(new Airport(516, 517, "Rzeszów", gui, world));
        //world.airportsArrayList.add(new Airport(422, 385, "Kielce", gui, world));
        world.airportsArrayList.add(new Airport(545, 393, "Lublin", gui, world));
        //Creating airplanes//
        Airplane airplane1 = new Airplane(world.airportsArrayList.get(0), "Airplane_1", 200, gui, world);
        Airplane airplane2 = new Airplane(world.airportsArrayList.get(1), "Airplane_2", 500, gui, world);

        world.airplanesArrayList.add(airplane1);

        Thread airplane1Thread = new Thread(airplane1);
        airplane1Thread.start();
        Thread airplane2Thread = new Thread(airplane2);
        airplane2Thread.start();
    }

}
