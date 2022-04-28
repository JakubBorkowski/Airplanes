package util;

import gui.AirplanesGUI;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Random;

@NoArgsConstructor
public class World {
    @Getter private final ArrayList<Airport> airportsArrayList = new ArrayList<>();
    @Getter private final ArrayList<Airplane> airplanesArrayList = new ArrayList<>();
    @Getter private final AirplanesGUI airplanesGUI = new AirplanesGUI();

    /**
     * Creates specified number of airplanes. All airplanes will be distributed equally between all airports.
     * @param numberOfAirplanes Number of airplanes to create.
     * @param minFuel Minimal number of fuel which airplane can have.
     * @param maxFuel Maximum number of fuel which airplane can have.
     * @param algorithmName name of algorithm which will be used by airplane to find path.
     *                      Available names: "BFS", "DFS", "DIJKSTRA".
     */
    public void addAirplanes(int numberOfAirplanes, int minFuel, int maxFuel, String algorithmName){
        if(!airportsArrayList.isEmpty()){
            Random rand = new Random();
            for(int i = 0, j=0; i<numberOfAirplanes; i++, j++){
                if(j==airportsArrayList.size()){
                    j=0;
                }
                if(minFuel==maxFuel){
                    airplanesArrayList.add(new Airplane(airportsArrayList.get(j), "Airplane_" + (i+1),
                            maxFuel, algorithmName, this));
                }
                else{
                    airplanesArrayList.add(new Airplane(airportsArrayList.get(j), "Airplane_" + (i+1),
                            rand.nextInt(maxFuel - minFuel) + minFuel, algorithmName, this));
                }
                Thread airplaneThread = new Thread(airplanesArrayList.get(i));
                airplaneThread.start();
            }
        }
    }

    /**
     * Creates an airport.
     * @param x X-axis position of airport.
     * @param y Y-axis position of airport.
     * @param name Name of airport.
     */
    public void addAirport(int x, int y, String name){
        airportsArrayList.add(new Airport(x, y, name, this));
    }

    /**
     * Check distance between two airports.
     * @param initialAirport Airport, from which distance will be calculated.
     * @param finalAirport Airport to which distance will be calculated.
     * @return distance between initialAirport and finalAirport.
     */
    public double checkDistance(Airport initialAirport, Airport finalAirport){
        return Math.sqrt(Math.pow((initialAirport.getX() - finalAirport.getX()), 2) +
                Math.pow((initialAirport.getY() - finalAirport.getY()), 2));
    }
}