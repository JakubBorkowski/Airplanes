import java.util.Locale;

public class App {

    /**
     * Check if provided arguments can be applied.
     * @param args String array of provided by user arguments.
     */
    private static void checkArguments(String[] args){
        if(!(args.length == 4)){
            System.err.println("Wrong number of arguments!\nPlease provide exactly 4 arguments.");
            System.exit(1);
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.err.println("First argument is not a number!");
            System.exit(1);
        }
        try {
            Float.parseFloat(args[1]);
        } catch (NumberFormatException nfe) {
            System.err.println("Second argument is not a number!");
            System.exit(1);
        }
        try {
            Float.parseFloat(args[2]);
        } catch (NumberFormatException nfe) {
            System.err.println("Third argument is not a number!");
            System.exit(1);
        }
        if(Integer.parseInt(args[0]) < 0){
            System.err.println("First argument can not be a negative number!");
            System.exit(1);
        }
        if(Integer.parseInt(args[1]) < 0){
            System.err.println("Second argument can not be a negative number!");
            System.exit(1);
        }
        if(Integer.parseInt(args[2]) < 0){
            System.err.println("Third argument can not be a negative number!");
            System.exit(1);
        }
        if(Integer.parseInt(args[1]) > Integer.parseInt(args[2])){
            System.err.println("The maximum fuel level must be greater than or equal to the minimum fuel level!");
            System.exit(1);
        }
        if(!args[3].toUpperCase(Locale.ROOT).equals("BFS")){
            if(!args[3].toUpperCase(Locale.ROOT).equals("DFS")){
                if(!args[3].toUpperCase(Locale.ROOT).equals("DIJKSTRA")){
                    System.err.println("The fourth argument should be \"BFS\", \"DFS\" or \"Dijkstra\"!");
                    System.exit(1);
                }
            }
        }
    }

    public static void main(String[] args) {
        checkArguments(args);
        int numberOfAirplanes = Integer.parseInt(args[0]);
        int minFuel = Integer.parseInt(args[1]);
        int maxFuel = Integer.parseInt(args[2]);
        String algorithmName = args[3];
        System.out.println();
        System.out.println("Number of airplanes: " + numberOfAirplanes);
        System.out.println("Minimum fuel       : " + minFuel);
        System.out.println("Maximum fuel       : " + maxFuel);
        System.out.println("Name of algorithm  : " + algorithmName);
        System.out.println();
        AirplanesGUI gui = new AirplanesGUI();
        new World(gui, numberOfAirplanes, minFuel, maxFuel, algorithmName);
    }
}