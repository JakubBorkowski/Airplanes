package pathfinding.model;

import lombok.NoArgsConstructor;
import util.Airport;

import java.util.LinkedList;

@NoArgsConstructor
public class Path extends LinkedList<Node> {

    public Path(Path path) {
        this.addAll(path);
    }

    public double getDistance() {
        double distance = 0.0;
        for (Node node : this) {
            distance += node.getDistance();
        }
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Path path = (Path) o;
        if (path.size() == this.size()) {
            for (Node nodeFromPath : path) {
                for (Node nodeFromThis : path) {
                    if (nodeFromPath.getInitialAirport() != nodeFromThis.getInitialAirport()
                            && nodeFromPath.getFinalAirport() != nodeFromThis.getFinalAirport()) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean contains(Airport airport) {
        for (Node node : this) {
            if (node.getInitialAirport() == airport || node.getFinalAirport() == airport) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder trackString = new StringBuilder();
        for (Node airport : this) {
            trackString.append(" -> ").append(airport.getFinalAirport().getName());
        }
        return trackString.toString();
    }
}
