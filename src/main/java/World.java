import java.util.ArrayList;
import java.util.Random;

public class World {
    public final ArrayList<Airport> airportsArrayList = new ArrayList<>();
    public final ArrayList<Airplane> airplanesArrayList = new ArrayList<>();

    /**
     * Creates the world.
     * @param airplanesGUI Graphic interface on which airplanes and airports will be displayed.
     * @param numberOfAirplanes Number of airplanes to create.
     * @param minFuel Minimal number of fuel with airplane can have.
     * @param maxFuel Maximum number of fuel with airplane can have.
     * @param algorithmName name of algorithm witch will be used by airplane to find track.
     *                      Available names: "BFS", "DFS", "DIJKSTRA".
     */
    public World(AirplanesGUI airplanesGUI, int numberOfAirplanes, int minFuel, int maxFuel, String algorithmName){
        createAirports(airplanesGUI);
        createAirplanes(numberOfAirplanes, minFuel, maxFuel, algorithmName, airplanesGUI);
    }

    /**
     * Creates specified number of airplanes. All airplanes will be distributed equally between all airports.
     * @param numberOfAirplanes Number of airplanes to create.
     * @param minFuel Minimal number of fuel with airplane can have.
     * @param maxFuel Maximum number of fuel with airplane can have.
     * @param airplanesGUI Graphic interface on which airplane will be displayed.
     * @param algorithmName name of algorithm witch will be used by airplane to find track.
     *                      Available names: "BFS", "DFS", "DIJKSTRA".
     */
    private void createAirplanes(int numberOfAirplanes, int minFuel, int maxFuel, String algorithmName,
                                 AirplanesGUI airplanesGUI){
        Random rand = new Random();
        for(int i = 0, j=0; i<numberOfAirplanes; i++, j++){
            if(j==airportsArrayList.size()){
                j=0;
            }
            if(minFuel==maxFuel){
                airplanesArrayList.add(new Airplane(airportsArrayList.get(j), "Airplane_" + (i+1),
                        maxFuel, algorithmName, airplanesGUI, this));
            }
            else{
                airplanesArrayList.add(new Airplane(airportsArrayList.get(j), "Airplane_" + (i+1),
                        rand.nextInt(maxFuel - minFuel) + minFuel, algorithmName, airplanesGUI, this));
            }
            Thread airplaneThread = new Thread(airplanesArrayList.get(i));
            airplaneThread.start();
        }
    }

    /**
     * Crate airports.
     * @param airplanesGUI Graphic interface on which airport will be displayed.
     */
    private void createAirports(AirplanesGUI airplanesGUI){
        airportsArrayList.add(new Airport(101, 319, "Zielona Góra", airplanesGUI, this));
        airportsArrayList.add(new Airport(81, 238, "Gorzów Wielokopolski", airplanesGUI, this));
        airportsArrayList.add(new Airport(44, 163, "Szczecin", airplanesGUI, this));
        airportsArrayList.add(new Airport(301, 71, "Gdańsk", airplanesGUI, this));
        airportsArrayList.add(new Airport(413, 138, "Olsztyn", airplanesGUI, this));
        airportsArrayList.add(new Airport(578, 203, "Białystok", airplanesGUI, this));
        airportsArrayList.add(new Airport(443, 286, "Warszawa", airplanesGUI, this));
        airportsArrayList.add(new Airport(253, 196, "Bydgoszcz", airplanesGUI, this));
        airportsArrayList.add(new Airport(299, 209, "Toruń", airplanesGUI, this));
        airportsArrayList.add(new Airport(189, 272, "Poznań", airplanesGUI, this));
        airportsArrayList.add(new Airport(348, 346, "Łódź", airplanesGUI, this));
        airportsArrayList.add(new Airport(200, 399, "Wrocław", airplanesGUI, this));
        airportsArrayList.add(new Airport(252, 455, "Opole", airplanesGUI, this));
        airportsArrayList.add(new Airport(319, 498, "Katowice", airplanesGUI, this));
        airportsArrayList.add(new Airport(376, 501, "Kraków", airplanesGUI, this));
        airportsArrayList.add(new Airport(516, 517, "Rzeszów", airplanesGUI, this));
        airportsArrayList.add(new Airport(422, 385, "Kielce", airplanesGUI, this));
        airportsArrayList.add(new Airport(545, 393, "Lublin", airplanesGUI, this));
    }

    /**
     * Check distance between two airports.
     * @param initialAirport Airport, from which distance will be calculated.
     * @param finalAirport Airport to which distance will be calculated.
     * @return distance between initialAirport and finalAirport.
     */
    double checkDistance(Airport initialAirport, Airport finalAirport){
        return Math.sqrt(Math.pow((initialAirport.getX() - finalAirport.getX()), 2) +
                Math.pow((initialAirport.getY() - finalAirport.getY()), 2));
    }
}