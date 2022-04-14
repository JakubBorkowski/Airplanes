import util.World;

import java.util.Locale;

public class App {

    /**
     * Checks if provided arguments can be applied.
     * @param args String array of provided by user arguments.
     */
    private static void checkArguments(String[] args){
        String errorMessage =
                "Please provide exactly 4 arguments in the following order:\n" +
                "-Number of airplanes\n" +
                "-Minimum fuel\n" +
                "-Maximum fuel\n" +
                "-Name of algorithm";
        if(!(args.length == 4)){
            System.err.println("Wrong number of arguments!");
            System.err.println("\n" + errorMessage);
            System.exit(1);
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.err.println("First argument is not a number!");
            System.err.println("\n" + errorMessage);
            System.exit(1);
        }
        try {
            Float.parseFloat(args[1]);
        } catch (NumberFormatException nfe) {
            System.err.println("Second argument is not a number!");
            System.err.println("\n" + errorMessage);
            System.exit(1);
        }
        try {
            Float.parseFloat(args[2]);
        } catch (NumberFormatException nfe) {
            System.err.println("Third argument is not a number!");
            System.err.println("\n" + errorMessage);
            System.exit(1);
        }
        if(Integer.parseInt(args[0]) < 0){
            System.err.println("First argument can not be a negative number!");
            System.err.println("\n" + errorMessage);
            System.exit(1);
        }
        if(Integer.parseInt(args[1]) < 0){
            System.err.println("Second argument can not be a negative number!");
            System.err.println("\n" + errorMessage);
            System.exit(1);
        }
        if(Integer.parseInt(args[2]) < 0){
            System.err.println("Third argument can not be a negative number!");
            System.err.println("\n" + errorMessage);
            System.exit(1);
        }
        if(Integer.parseInt(args[1]) > Integer.parseInt(args[2])){
            System.err.println("The maximum fuel level must be greater than or equal to the minimum fuel level!");
            System.err.println("\n" + errorMessage);
            System.exit(1);
        }
        if(!args[3].toUpperCase(Locale.ROOT).equals("BFS")){
            if(!args[3].toUpperCase(Locale.ROOT).equals("DFS")){
                if(!args[3].toUpperCase(Locale.ROOT).equals("DIJKSTRA")){
                    System.err.println("The fourth argument should be \"BFS\", \"DFS\" or \"Dijkstra\"!");
                    System.err.println("\n" + errorMessage);
                    System.exit(1);
                }
            }
        }
    }

    public static void main(String[] args) {
        //Checking if provided arguments can be applied.//
        checkArguments(args);
        //Initializing provided values//
        int numberOfAirplanes = Integer.parseInt(args[0]);
        int minFuel = Integer.parseInt(args[1]);
        int maxFuel = Integer.parseInt(args[2]);
        String algorithmName = args[3];
        //Displaying provided values//
        System.out.println();
        System.out.println("Number of airplanes: " + numberOfAirplanes);
        System.out.println("Minimum fuel       : " + minFuel);
        System.out.println("Maximum fuel       : " + maxFuel);
        System.out.println("Name of algorithm  : " + algorithmName);
        System.out.println();
        //Starting app GUI//
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
        world.addAirplanes(numberOfAirplanes, minFuel, maxFuel, algorithmName);
    }
}