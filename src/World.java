import java.util.ArrayList;
import java.util.Random;

public class World {
    public ArrayList<Airport> airportsArrayList = new ArrayList<>();
    public ArrayList<Airplane> airplanesArrayList = new ArrayList<>();

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
        createAirplanes(numberOfAirplanes, minFuel, maxFuel, airplanesGUI, algorithmName);
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
    private void createAirplanes(int numberOfAirplanes, int minFuel, int maxFuel, AirplanesGUI airplanesGUI, String algorithmName){
        Random rand = new Random();
        for(int i = 0, j=0; i<numberOfAirplanes; i++, j++){
            if(airportsArrayList.get(j)==null){
                j=0;
            }
            if(minFuel==maxFuel){
                airplanesArrayList.add(new Airplane(airportsArrayList.get(j), "Airplane_" + (i+1),
                        maxFuel, airplanesGUI, this, algorithmName));
            }
            else{
                airplanesArrayList.add(new Airplane(airportsArrayList.get(j), "Airplane_" + (i+1),
                        rand.nextInt(maxFuel - minFuel) + minFuel, airplanesGUI, this, algorithmName));
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
        airportsArrayList.add(new Airport(81, 238, "Gorzów Wielokopolski", airplanesGUI, this));//
        airportsArrayList.add(new Airport(37, 170, "Szczecin", airplanesGUI, this));
        airportsArrayList.add(new Airport(301, 71, "Gdańsk", airplanesGUI, this));
        airportsArrayList.add(new Airport(413, 138, "Olsztyn", airplanesGUI, this));
        airportsArrayList.add(new Airport(578, 203, "Białystok", airplanesGUI, this));
        airportsArrayList.add(new Airport(448, 293, "Warszawa", airplanesGUI, this));
        airportsArrayList.add(new Airport(259, 198, "Bydgoszcz", airplanesGUI, this));
        airportsArrayList.add(new Airport(298, 213, "Toruń", airplanesGUI, this));//
        airportsArrayList.add(new Airport(184, 274, "Poznań", airplanesGUI, this));
        airportsArrayList.add(new Airport(348, 346, "Łódź", airplanesGUI, this));
        airportsArrayList.add(new Airport(195, 410, "Wrocław", airplanesGUI, this));
        airportsArrayList.add(new Airport(252, 455, "Opole", airplanesGUI, this));
        airportsArrayList.add(new Airport(319, 498, "Katowice", airplanesGUI, this));//
        airportsArrayList.add(new Airport(378, 517, "Kraków", airplanesGUI, this));
        airportsArrayList.add(new Airport(516, 517, "Rzeszów", airplanesGUI, this));
        airportsArrayList.add(new Airport(422, 385, "Kielce", airplanesGUI, this));//
        airportsArrayList.add(new Airport(545, 393, "Lublin", airplanesGUI, this));
    }

    /**
     * Check distance between two airports.
     * @param initialAirport Airport, from which distance will be calculated.
     * @param finalAirport Airport to which distance will be calculated.
     * @return distance between initialAirport and finalAirport.
     */
    double checkDistance(Airport initialAirport, Airport finalAirport){
        return Math.sqrt(Math.pow((initialAirport.x - finalAirport.x), 2) + Math.pow((initialAirport.y - finalAirport.y), 2));
    }
}