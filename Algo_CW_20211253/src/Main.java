import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    static ArrayList<Vertex> vertices = new ArrayList<>(); // store nodes
    static Vertex sink; // sink node
    static Stack<Vertex> visitingNodes = new Stack<>(); // for add all visiting nodes
    static Vertex presentNode; // for DFS
    public static void main(String[] args) {

        // Menu
        showToDoTask();

        int num;

        do{
            num = displayMenu();
            switch (num) {
                case 1:
                    if(vertices.size() == 0){
                        System.out.println("please input Graph or enter 2");
                    }else{
                        sinkTask();
                    }
                    vertices.clear();
                    showToDoTask();
                    break;
                case 2:
                    vertices.clear();
                    readTheFile();
                    showToDoTask();
                    break;
                case 3:
                    if(vertices.size() == 0){
                        System.out.println("please input Graph or enter 2");
                    }else{
                        acyclicTask();
                    }
                    vertices.clear();
                    visitingNodes.clear();
                    showToDoTask();

                    break;
                case 0:
                    System.exit(0);
                    break;
            }
        }while(num != 0);
    }

    public static void showToDoTask(){
        System.out.println("\n=--------------------> Graph <-----------------------=");

        System.out.println("1. Finding a sink and remove vertex ");
        System.out.println("2. Load input file");
        System.out.println("3. Check Graph is Acyclic or Cyclic.\n(if graph cycle then show cycle path)");
        System.out.println("0. Exit");
    }

    // display menu
    public static int displayMenu(){
        while(true){
            try{
                Scanner sc = new Scanner(System.in);
                System.out.println("\nplease enter a Number :");
                int number = sc.nextInt();
                return number;
            }catch (Exception e){
                System.out.println("Enter Correct Number...");
            }
        }
    }

    // -------------- Task 2 ------------------

    // find sink of vertex
    public static boolean hasSink(){
        for(Vertex node: vertices){
            if(node.getAdjList().size() == 0){
                sink = node;
                return true; // find sink
            }
        }
        return false; // not find sink
    }

    // remove sink node from vertices arrayList
    public static void removeTheSink(){
        System.out.println("Remove the sink Node : " + sink.getValue());

        // remove vertex from vertices list
        vertices.remove(sink);

        // remove adjacent
        for(Vertex v: vertices){
            v.remove(sink);
        }


    }

    // all task for find sink and remove sink
    public static void sinkTask(){
        System.out.println("""
                **********************************
                Finding sink and remove sink node
                **********************************
                """);

        while(hasSink()){
            System.out.println("Node " + sink.getValue() + " is a sink node");
            removeTheSink();
            System.out.println();
        }
        System.out.println("Not found sink node");
    }

    // ------------------ Task 3 --------------------

    public static void readTheFile(){
        Scanner sc = null;
        try{
            sc = new Scanner(new File("Num.txt"));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        addNewDataToVerticesArrayList(sc);
    }

    public static void addNewDataToVerticesArrayList(Scanner sc){
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String[] newVertices = line.split(" ");
            // convert string to int
            int[] valueOfVertices = {Integer.parseInt(newVertices[0]), Integer.parseInt(newVertices[1])};

            if(addNewNodeToVertices1(valueOfVertices)){
                vertices.add(new Vertex(valueOfVertices[0]));
            }
            if(addNewNodeToVertices2(valueOfVertices)){
                vertices.add(new Vertex(valueOfVertices[1]));
            }
            addTheEdge(valueOfVertices);
        }
    }

    public static boolean addNewNodeToVertices1(int[] valueOfVertices){
        for(Vertex v: vertices){
            if(v.getValue() == valueOfVertices[0]){
                return false;
            }
        }
        return true;
    }

    public static boolean addNewNodeToVertices2(int[] valueOfVertices){
        for(Vertex v: vertices){
            if(v.getValue() == valueOfVertices[1]){
                return false;
            }
        }
        return true;
    }

    public static void addTheEdge(int[] valueOfVertices){
        Vertex v1 = null;
        Vertex v2 = null;
        for(Vertex v: vertices){
            if(v.getValue() == valueOfVertices[0]){
                v1 = v;
                break;
            }
        }

        for (Vertex v: vertices){
            if(v.getValue() == valueOfVertices[1]){
                v2 = v;
            }
        }
        v1.setAdjList(v2);
    }



    // ---------------- Task 4 --------------------

    public static boolean isAcyclic(){
        // first of all nodes is initialized as UNVISITED
        for(Vertex node: vertices){
            node.setState(Vertex.State.UNVISITED);
        }

        // check whether is there Cyclic
        for(Vertex node: vertices){
            if(node.getState() == Vertex.State.UNVISITED){
                if(DFS(node)){
                    // if found cyclic graph then return false
                    return false;
                }
            }
        }

        // the graph is acyclic
        return true;
    }

    public static boolean DFS(Vertex node){

        System.out.println("==============================================");
        // node is marked as visiting
        node.setState(Vertex.State.VISITING);
        System.out.println("Visiting Node : " + node.getValue());
        // print all visited nodes
        System.out.printf("Visited Nodes : ");
        for(Vertex v: visitingNodes){
            System.out.printf(String.valueOf(v.getValue()) + ", ");
        }
        System.out.println();
        // node added to the stack
        visitingNodes.push(node);

        // check adjacent nodes
        for(Vertex adjNode: node.getAdjList()){
            if(adjNode.getState() == Vertex.State.UNVISITED){
                if(DFS(adjNode)){
                    // there cycle in graph
                    return true;
                }
            } else if (adjNode.getState() == Vertex.State.VISITING) {
                presentNode = adjNode;
                // back edge
                return true;
            }
        }
        // node is marked as fully explored
        node.setState(Vertex.State.VISITED);
        //print explored node
        System.out.println("Node " + node.getValue() + " as Explored");
        // pop explored node
        visitingNodes.pop();

        // if there are no cycles found in graph
        return false;
    }

    public static void acyclicTask(){
        if(isAcyclic()){
            System.out.println("Graph is Acyclic");
        }else{
            System.out.println("Graph is Cyclic");
            findCyclePath();
        }
    }

    // ----------------- Task 5 -----------------------
    public static void findCyclePath(){
        int indexNumber = visitingNodes.indexOf(presentNode);
        int sizeOfStack = visitingNodes.size();

        System.out.println();
        // print path
        for(int i = indexNumber; i < sizeOfStack; i++){
            System.out.printf(visitingNodes.get(i).getValue() + " => ");
        }
        System.out.printf(presentNode.getValue() + "\n");
    }

}