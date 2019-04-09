/**
 * This program is an implementation of a Directed Graph of Computers in a network. 
    It involves a method for traversing through the network and deciphering whether or not
    a message can be sent from one computure to another within a window of time.
    
    It is designed specifically for Exercise 11, project 1, CPSC 320 - Trinity College
    <a href="http://www.cs.trincoll.edu/~miyazaki/cpsc320/project1.html">
    
    Compilation: javac Digraphv.java
    Execution: Java Digraphv 
    Dependencies: Java LinkedList, Java ArrayList, Java HashMap, Java List.
    
    Package with a test file document "test.txt" which can be modified under the specified encoding 
    under 
    
    @author Andrew Lewis
 */

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * The class Digraphv stores an ArrayList of Nodes. Each of these {@type Node} objects are defined under
 * the Node class. 
 * 
 * <p>All operations take constant time except interating through the adjacency lists of each node.
 * of which takes time proportional to the number of neighboring nodes for a given node.
 */
public class Digraphv{

    private Queue<Node> queue;
    private ArrayList<Node> graph; 
    private int numberOfComputers; 
    private int numberOfConnections;
    private static HashMap<Integer, Node> map;
    private int tracker;                        //Tracker keeps numeric count of graph index
    
    /**
     * Basic Helper class to represent a directed time-stamped edge to another node in G.
     */
    static class Edge{
            int node;   //node Id
            int time;   //Time of communication

            /**
             * Default constructor
             * @param n Id of adjacent computer(node)
             * @param t time
             */
            public Edge(int n, int t){
                this.node = n;
                this.time = t;
            }
            
            public int getTime(){
                return this.time;
            }

            public int getNode(){
                return this.node;
            }

            public String toString(){                
                return "("+node+":"+time+")";
            }        
    }
    
    /**
     * Basic Helper Node class to represent a computer on the network. Each node has an ArrayList
     * of its adjacent neighbors, as well as respective timestamps. ArrayList<Edge>
     */
    static class Node{
            
        private int data;
        private boolean visited;
        private int lastVisited;
        private ArrayList<Edge> neighbors;
        /**
         * Default constructor for Node class. Given that the input data to the problem is in triples,
         * this constructor takes in a set of computer ID's and a time T. Creates node for computer C1,
         * and adds c2 to its adjacency lists.
         * @param c1 computer 1
         * @param c2 computer 2
         * @param t  time 
         */
        public Node(int c1, int c2, int t){
            this.data = c1;
            this.visited = false;
            this.lastVisited = 0;
            this.neighbors = new ArrayList<>();            
            addNeighbor(c2, t);            
        }
    
        /**
         * This function creates an edge object a adds to the neighbors (adj) list
         * @param n ID of adj computer
         * @param t time of connection
         */
        public void addNeighbor(int n, int t){
            neighbors.add(edge(n,t));                  
        }
        /**
         * This function sets the minimum time limit for the current node.
         * This is used by the graph traversal function to determine which node to traverse next.
         * @param n time from last visiting node.
         */
        public void setLastVisited(int n){
            this.lastVisited = n;
        }
        
        public boolean isVisited(){
            return visited;
        }

        public int getLastVisited(){
            return lastVisited;
        }
        /**
         * This function creaes a directed edge
         * @param n ID
         * @param t Time
         * @return edge object (n,t)
         */
        public Edge edge(int n, int t){
            Edge edge = new Edge(n, t);
            return edge;
        }
        /**
         * This function returns the neigbors list
         * @return adjacency list
         */
        public ArrayList<Edge> getNeighbors(){
            return neighbors;
        }
        /**
         * This function returns the Id of Node. Used by hashmap to identify nodes in graph
         * @return
         */
        public int getData(){
            return this.data;
        }

        public void print(){
            for (int i = 0; i < neighbors.size(); i++){
                System.out.println("neighbor " +i+ " is:"+neighbors.get(i));
            }
        }

    }

    /**
     * Default constructor for Digraphv. Initialize all variables. 
     * @param x number of computers
     * @param y number of connections
     */
    public Digraphv(int x, int y){
        queue = new LinkedList<Node>();
        graph = new ArrayList<Node>();
        map = new HashMap<>();
        numberOfComputers = x;
        numberOfConnections = y;
        tracker = 0;    
    }
    
    /**
     * This function performs a BFS -esque trasversal of the nodes in the graph
     * It is tailored to check for valid timed-paths between vertices(nodes) of the graph. 
     * 
     * <p>This function outputs a trace of these valid communications and a summary of the
     * path if there is one. Otherwise outputs the word "safe" meaning that there was no valid
     * timedpath between nodes 1 and 2
     * @param node1 root node
     * @param time1 minimum time
     * @param node2 destination node
     * @param time2 maximum time
     */
    public void check_spread(Node node1, int time1, Node node2, int time2){
        queue.add(node1);
        node1.visited = true;
        node1.setLastVisited(time1);
		while (!queue.isEmpty())
		{ 
			Node element = queue.remove();			
            List<Edge> neighbors = element.getNeighbors();            
            int minTime = element.getLastVisited();         //This checks that we don't traverse backwards in time
            System.out.println("At Computer " + element.data + " checking for communications after time " + minTime+", but within "+ time2);
			for (int i = 0; i < neighbors.size(); i++) {
                Edge n = neighbors.get(i);
                Node x = (Node)map.get(n.node);
                //check neighbors for valid path
				if(x != null && !x.visited && n.time >= minTime && n.time <= time2){
                    if (n.node == node2.getData()){
                        System.out.println("Virus will affect Computer " + n.node +" at time " + n.time + " through computer " + element.getData());
                        return;
                    }  
                    x.setLastVisited(n.time);
                    queue.add(x);
				    x.visited=true;                  
                }
                 
			} 
        }
        System.out.println("Safe!");
    }
    /**
     * This function checks the list of nodes in the graph based on the triples. Input new nodes and edges.
     * Ensures that once a node is in the graph, it is mapped to an ID which is an Int. This allows for o(1)
     * lookup of a node to draw or add to its adj list
     * @param c1 computer 1 in triple
     * @param c2 computer 2 in triple
     * @param t time in triple
     */
    public void addToGraph(int c1, int c2, int t){
        if (map.containsKey(c1) && map.containsKey(c2)){
            map.get(c1).addNeighbor(c2, t);map.get(c2).addNeighbor(c1, t); 
        }
        else if(map.containsKey(c1) && !map.containsKey(c2)){
            graph.add(new Node(c2, c1, t));
            map.get(c1).addNeighbor(c2, t);
            map.put(c2, graph.get(tracker));
            tracker++;
        }
        else if(map.containsKey(c2) && !map.containsKey(c1)){
            graph.add(new Node(c1, c2, t));
            map.get(c2).addNeighbor(c1, t);
            map.put(c1, graph.get(tracker));
            tracker++;
        }
        else{
            graph.add(new Node(c1, c2, t));
            graph.add(new Node(c2, c1, t));
            map.put(c1, graph.get(tracker));
            map.put(c2, graph.get(tracker+1));
            tracker=tracker+2;
        }           
    }
   
    /**
     * Program driver
     * @param arg
     */
    public static void main(String arg[]){             
        
        File file = new File("test.txt");   //"test.txt" is the test file found in the relative root of main class
        int computers = 0;
        int triples = 0;
        //Reading input from file
        try {
            Scanner sc = new Scanner(file);
            computers = sc.nextInt(); triples = sc.nextInt();
            Digraphv digraph = new Digraphv(computers, triples);
            //extract data from each triple and add to graph.
            for (int i = 0; i < triples; i++){
                int c1 = sc.nextInt(); int c2 = sc.nextInt(); int t = sc.nextInt();
                digraph.addToGraph(c1, c2, t);                                                
            }
            System.out.println("The traversal of the graph is: "); 
            //"coming" represents root computer, "going" represents target          
            int coming  = sc.nextInt(); int time = sc.nextInt(); int going = sc.nextInt(); 
            System.out.println("Starting from: " + map.get(coming).getData());
            digraph.check_spread((Node)map.get(coming), time, (Node)map.get(going), sc.nextInt());            
            sc.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e);
        }                     
	}
}