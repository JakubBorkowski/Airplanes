# Airplanes
App creates airplanes which will fly through predefined airports. Every airplane have different randomly chosen number 
of fuel between minimum and maximum provided by user. If airplane can not reach airport immediately it will search for 
shortest path to this airport. Current there are three path finding algorithm available in the app.
User can select which one should be used.

<p align="center">
<img align="center" src="https://dl.dropbox.com/s/wiydt6p6yrexm0k/screenshot621.gif" height="450" width="450">
</p>

## Available algorithms

### Depth-first search algorithm
In this implementation of Depth-first search algorithm, it is used to find all possible paths. Next, paths are sorted 
and there is chosen one with the shortest overall distance of travel. This approach guarantee to find the best path, 
but it's highly ineffective.

The algorithm first checks if the airport has unvisited neighbors. If so, these neighbors are checked if one of them is 
a final airport. When the final airport is found, path to it is added to the list of found paths. The process repeats 
itself until all possible paths are found.

### Breadth-first search algorithm
The algorithm searches neighboring airports to see if any of them is the final airport. If none of the found airports 
is the final airport, the neighbors of those airports are added to the queue and the search is repeated until the final 
airport is found or all airports are searched.

Breadth-first search algorithm do not guarantee finding the shortest path in terms of overall traveled distance, 
but number of airports to which airplane will stop by will be the smallest.

### Dijkstra's shortest path algorithm
The algorithm first creates a table with: name of each airport, the shortest distance that is currently known from 
initial airport to this airport, name of airport, from which we got to this airport in currently known the shortest 
distance. Initial values in table for every airport except initial airport will be as follows: name of airport, 
positive infinity, null. For initial airport, values in table will be: name of initial airport, 0, null. Next, 
the algorithm calculate the shortest distance from initial airport to currently processed airport and if it is shorter 
that distance in the table, the table it's updated. Once the shortest distance from initial airport to every other 
airport is calculated, base on this information it is created the shortest path to the final airport.

Algorithm guarantee to find the shortest path relatively quick.

## Usage
Program must be started with the following arguments in the same order:
- `[Number of airplanes]` - Number of airplanes to create, that will be distributed equally across airports.
- `[Minimal fuel]` - Minimal fuel that created airplane can have.
- `[Maximum fuel]` - Maximal fuel that created airplane can have.
- `[Algorithm name]` - Name of shortest path finding algorithm which should be use. Possible names:
  - **DFS** for Depth-first search algorithm,
  - **BFS** for Breadth-first search algorithm,
  - **DIJKSTRA** for Dijkstra's shortest path algorithm.

Example use:
```
java -jar Airplanes.jar [Number of airplanes] [Minimal fuel] [Maximum fuel] [Algorithm name]
```
```
java -jar Airplanes.jar 1 200 200 DIJKSTRA
```
