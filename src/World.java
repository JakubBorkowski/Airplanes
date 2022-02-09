import java.util.ArrayList;
import java.util.Random;

public class World {
    public ArrayList<Airport> airportsArrayList = new ArrayList<>();
    public ArrayList<Airplane> airplanesArrayList = new ArrayList<>();
    public ArrayList<Airplane> availableAirplanesArrayList = new ArrayList<>();

    private Random rand = new Random();

    public World(){

    }

    double checkDistance(Airport initialAirport, Airport finalAirport){
        double distance = Math.sqrt(Math.pow((initialAirport.x - finalAirport.x), 2) + Math.pow((initialAirport.y - finalAirport.y), 2));
        return distance;
    }
}