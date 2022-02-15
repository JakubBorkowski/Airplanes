public class Main {

    private static void checkArguments(String[] args){
        if(!(args.length == 3)){
            System.err.println("Wrong number of arguments!\nPlease provide exactly 3 arguments.");
            System.exit(1);
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.err.println("Firs argument is not a number!");
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
            System.err.println("Firs argument can not be a negative number!");
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
    }

    public static void main(String[] args) {
        checkArguments(args);
        int numberOfAirplanes = Integer.parseInt(args[0]);
        int minFuel = Integer.parseInt(args[1]);
        int maxFuel = Integer.parseInt(args[2]);
        AirplanesGUI gui = new AirplanesGUI();
        World world = new World(gui, numberOfAirplanes, minFuel, maxFuel);
    }
}