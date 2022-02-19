import java.util.ArrayList;
import java.util.Comparator;

public class Airport {
    final int x;
    final int y;
    final String name;
    private final World world;

    Airport(int x, int y, String name, AirplanesGUI airplanesGUI, World world){
        this.x = x;
        this.y = y;
        this.name = name;
        this.world = world;
        airplanesGUI.drawAirport(x, y, name);
    }

    public ArrayList<Airport> getNearAirports(double fuel){
        ArrayList<Airport> nearAirportsArrayList = new ArrayList<>();
        for(Airport airport : world.airportsArrayList){
            if(fuel >= world.checkDistance(this, airport)){
                nearAirportsArrayList.add(airport);
            }
        }
        nearAirportsArrayList.remove(this);
        return nearAirportsArrayList;
    }

    public ArrayList<Airport[]> getNodes(double fuel){
        ArrayList<Airport[]> nearAirportsArrayList = new ArrayList<>();
        for(Airport airport : world.airportsArrayList){
            if(!airport.equals(this) && fuel >= world.checkDistance(this, airport)){
                Airport[] node = {this, airport};
                nearAirportsArrayList.add(node);
            }
        }
        return nearAirportsArrayList;
    }

    public ArrayList<Airport[]> getSortedNodes(double fuel){
        ArrayList<Airport[]> nodesToSort = getNodes(fuel);
        Comparator<Airport[]> compareByDistance = new Comparator<Airport[]>() {
            @Override
            public int compare(Airport[] node1, Airport[] node2) {
                double distance1 = world.checkDistance(node1[0], node1[1]);
                double distance2 = world.checkDistance(node2[0], node2[1]);
                return Double.compare(distance1, distance2);// * (-1);
            }
        };
        nodesToSort.sort(compareByDistance);
        return nodesToSort;
    }
}