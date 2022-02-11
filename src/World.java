import java.util.ArrayList;
import java.util.Random;

public class World {
    public ArrayList<Airport> airportsArrayList = new ArrayList<>();
    public ArrayList<Airplane> airplanesArrayList = new ArrayList<>();

    private Random rand = new Random();

    public World(){

    }

    public void createAirplanes(int numberOfAirplanes, AirplanesGUI gui){
        for(int i = 0; i<numberOfAirplanes; i++){
            for(Airport airport : airportsArrayList){
                airplanesArrayList.add(new Airplane(airportsArrayList.get(i), "Airplane_" + i, 200, gui, this));
            }
        }
    }

    double checkDistance(Airport initialAirport, Airport finalAirport){
        return Math.sqrt(Math.pow((initialAirport.x - finalAirport.x), 2) + Math.pow((initialAirport.y - finalAirport.y), 2));
    }
}