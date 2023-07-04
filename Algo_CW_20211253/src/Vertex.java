import java.util.ArrayList;
import java.util.Iterator;

public class Vertex {
    enum State{
        UNVISITED,VISITING,VISITED
    }

    // Declared value for node
    private int value;
    // Edges
    private ArrayList<Vertex> adjList = new ArrayList<>();
    private State state;

    public Vertex(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public void setAdjList(Vertex adjNode){
        adjList.add(adjNode);
    }
    public ArrayList<Vertex> getAdjList(){
        return adjList;
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
    public void remove(Vertex v){
        if(adjList.contains(v)){
            adjList.remove(v);
        }
    }
}

