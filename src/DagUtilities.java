import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * I use the princeton code for a queue in my topological sort, longest path, and minimal schedule
 * @author Bogdan
 * @pso 05
 */
public class DagUtilities {

	
	private static int[] T; //topological numbers in vertex order
	private static Node[] Top; // Nodes in topological order
    /** Task 1
     * This function performs a toplogical sort on a graph
     * 
     * @param dag G
     * @return an array containing the topological numbers of each vertex in graph G
     */
    @SuppressWarnings("unused")
	public static int[] topologicalSort(Digraph G) {
        Queue<Node> TSQ = new Queue<Node>();
        int[] in = new int[G.size()]; //Array to keep track of how many nodes have had their incoming nodes' topological number calculated
        T = new int [G.size()]; //Array of topological numbers
        Top = new Node[G.size()]; //Array of nodes in Topological order
        int tn = 0; //Topological number
        for(int i = 0; i < G.size(); i++){ //Initializes the in array and puts sources into the queue TSQ
        	if(G.getNode(i).In() == 0){
        		TSQ.enqueue(G.getNode(i));
        	}
        	in[i] = G.getNode(i).In();
        }
        
        while(!TSQ.isEmpty()){//Produce topological numbers
        	Node u = TSQ.dequeue(); //dequeues the next node u
        	T[u.id()] = tn; //sets the topological number of u to the next available topological number
        	Top[tn++] = u; //sets the corresponding node in this array to the next available topological number to u
        	for(int i = 0; i < u.Out(); i++){
        		in[u.getOut(i)]--; //All of u's children nodes has had one of their incoming node's topological number calculated 
        		if(in[u.getOut(i)] == 0){ //if all of a node's incoming nodes have had their topological number calculated, put it in the queu
        			TSQ.enqueue(G.getNode(u.getOut(i)));
        		} 
        	}
        }
        return T;
    }

    /**
     * Task 2
     * This function determines the length of the longest path
     * 
     * @param dag G
     * @return length of the longest path
     */
    public static int longestPath(Digraph G) {
    	Queue<Node> TSQ = new Queue<Node>(); //Queue used to navigate the graph to update the longest path
    	int[] disTo = new int[G.size()]; //distance to vertex i from the beginning a source
    	int Long = 0; //longest path to return
    	for(int i = 0; i < G.size(); i++){ //Initializes the disTo array and puts sources into the queue TSQ
        	if(G.getNode(i).In() == 0){
        		TSQ.enqueue(G.getNode(i));
        		disTo[i] = 0;
        	}else{
        		disTo[i] = -1;
        	}
        }
    	
    	while(!TSQ.isEmpty()){ //calculate the longest path for each node
    		Node u = TSQ.dequeue();// gets node u
    		for(int i = 0; i < u.Out(); i++){ //for each of node u's children, update their longest distance from a source
    			if(disTo[u.getOut(i)] < disTo[u.id()] + 1){ //If the path to child i through u is longer than the current longest path to i
    				disTo[u.getOut(i)] = disTo[u.id()] + 1; //replace the path of i with the path to u + 1
    				if(disTo[u.getOut(i)] > Long){ //if the new path is longer than the current longest
    					Long = disTo[u.getOut(i)]; //update the current longest path
    				}
    			}
    			TSQ.enqueue(G.getNode(u.getOut(i))); //enqueue child i to calculate all of its children's paths
    		}
    	}
    	
        return Long;
    }

    /**
     * Task 3
     * This function  generates a schedule that completes all manufacturing steps in the shortest
     * production span.
     * 
     * @param dag G
     * @return a schedule
     */
    public static Schedule minProdSpanSchedule(Digraph G) {
    	Schedule Minimum = new Schedule(longestPath(G)+1); //Initiates a new Schdule with time span: longest path + 1
    	Queue<Node> Possible = new Queue<Node>(); //Queue to structure navigation through graph
    	int[] scheduled = new int[G.size()]; //array saying whether or not a node needs to be scheduled. They can be scheduled only when all of its previous nodes are scheduled
    	for(int i = 0; i < G.size(); i++){ //Finds all the sources and puts them in the queue as an initialization
        	if(G.getNode(i).In() == 0){
        		Possible.enqueue(G.getNode(i));// adds the sources to the queue
        	}
        	scheduled[i] = G.getNode(i).In(); //The number of parents node i has until it can be scheduled
        }
    	/*
    	 * At each time step i, remove all the nodes in the queue and put them in the schedule
    	 * Then for each of the node's children, decrement their scheduled array value. This shows that one more parent has been scheduled.
    	 * When a child's schedule value is 0 (i.e. all of its parents have been scheduled), add it a second queue
    	 * That second queue represents the all of the nodes in the previous time step
    	 * The second queue is copied into the original queue and then the time step is incremented
    	 */
    	for(int i = 0; i < Minimum.getProductionSpan(); i++){ //Each iteration is a time step
    		Queue<Node> temp = new Queue<Node>(); //Temporary queue that will hold all of the previous production step's nodes
    		while(!Possible.isEmpty()){ //Empties the current time step's nodes
    			Node u = Possible.dequeue(); //gets node u from the queue
    			Minimum.addToSchedule(i, u.id()); //add u to the schedule at time i
    			for(int j = 0; j < u.Out();j++){ //each of u's children who are not already scheduled are added to the temp queue
    				scheduled[u.getOut(j)]--; //u's child has one more parent scheduled (u)
    				if(scheduled[u.getOut(j)] == 0){ //if the all the parents of u's child are already scheduled, add it to the temp queue
        				temp.enqueue(G.getNode(u.getOut(j)));
    				}
    			}
    		}
    		while(!temp.isEmpty()){ //sets the possible queue to the temp queue
    			Possible.enqueue(temp.dequeue());
    		}
    	}
        return Minimum;
    }

    /**
     * Task 4
     * This function generates a schedule using at most k stations. 
     * @param dag G
     * @param the number of stations k
     * @return a schedule
     */
    public static Schedule spanKStations(Digraph G, int k) {
    	int[] level = new int[G.size()]; //creates enough levels for each node to be on one (i.e. k=1)
    	Schedule kSchedule = new Schedule(G.size()); //creates a schedule of size N for now
    	int[] VertLevel = new int[G.size()]; //the level of each node
    	int base_lev = 0; //lowest level possible to add to
    	/*
    	 * For each Vertex in topological order, add it to the schedule at the base level if there is space for it and all of its parent nodes are in a level below
    	 * else bump it up to the appropriate level
    	 */
    	for(int i = 0; i < Top.length; i++){
    		Node u = Top[i];
    		int lev = base_lev; //Level to add node u, set to the lowest possible level
    		for(int j = 0; j < u.In(); j++){ //Look at all the parents of node u and move u into the next highest level of them
    			if(VertLevel[u.getIn(j)] >= lev){ //if a parent's level is the same as or greater than u's, u goes up a level above it
    				lev = VertLevel[u.getIn(j)] + 1;
    			}
    		}
    		while(level[lev] == k){ //if this level is full, bump up the level until it can fit in  
    			lev++;
    		}
    		kSchedule.addToSchedule(lev, u.id()); //add the vertex at the calculated level
    		VertLevel[u.id()] = lev; //set the level of the node u
    		level[lev]++; //fill up one station on the calculated level
    		if(level[base_lev] == k){ //if k stations are being used on the bottom level, move the bottom level up one.
    			base_lev++;
    		}
    	}
    	int lp = 0; //longest path for the graph given the constraint of k
    	while(lp < kSchedule.getProductionSpan() && kSchedule.getSchedule(lp).size() != 0){ //find the highest level that has a node
    		lp++;
    	}
    	Schedule finale = new Schedule(lp); //creates the final schedule of size of the longest path given the constraint of k stations
    	for(int i = 0; i < lp; i++){ //copies over the schedule into the new one
    		ArrayList<Integer> step = kSchedule.getSchedule(i);
    		for(int j = 0; j < step.size(); j++){
    			finale.addToSchedule(i, step.get(j));
    		}
    	}
        return finale;
    }

    /**
     * This function reads a graph from an input file
     * 
     * @param file
     * @return dag G
     */
    public static Digraph readGraphFromFile(File file) {
    	try{
            Scanner scan = new Scanner(file);
            scan.useDelimiter(" |:");
            int n = scan.nextInt();
            scan.next();
            int e = scan.nextInt();
            Digraph graph = new Digraph(n,e);
            scan.nextLine();
            while(n > 0){ //for each node
            	int id = scan.nextInt(); //gets the id of the primary node
            	scan.nextLine(); //removes the : after the id
            	String inS = scan.nextLine(); //gets the in nodes
            	Scanner in = new Scanner(inS); //creates a scanner to parse the in nodes
            	in.next(); //removes the In: string
            	
            	while(in.hasNext()){ //puts the id of each node coming in to the primary node's in arraylist
            		graph.getNode(id).addIn(in.nextInt());
            	}
            	
            	String outS = scan.nextLine(); //gets the out nodes
            	Scanner out = new Scanner(outS); //creates a scanner to parse the out nodes
            	out.next(); //removes the Out: string
            	
            	while(out.hasNext()){ //puts the id of each node going out to the primary node's out arraylist
            		graph.getNode(id).addOut(out.nextInt());
            	}
            	n--; //decrement the number of nodes left
            	in.close();
            	out.close();
            }
            scan.close();
            return graph;
    	}catch(FileNotFoundException e){
    		System.out.println("File not Found");
    	}
        return null;
    }

}
