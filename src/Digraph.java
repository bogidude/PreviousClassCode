import java.util.ArrayList;
/**
 * 
 * This is the graph structure
 * TODO: You should implement it
 *
 */
public class Digraph
{
	
	private Node[] vertex; //every node in the graph
	private int edge; //number of edges
	
	public Digraph(int n, int e){
		vertex = new Node[n];
		edge = e;
		for(int i = 0; i < n; i++){ //initialize every node
			vertex[i] = new Node(i);
		}
	}
	
	/**
	 * returns the size of the graph
	 * @return number of vertices
	 */
	public int size(){
		return vertex.length;
	}
	
	/**
	 * returns the number of edges
	 */
	public int edges(){
		return edge;
	}
	
	/**
	 * returns node with id n
	 * @param n - id
	 * @return node n
	 */
	public Node getNode(int n){
		if(n >= vertex.length || n < 0){
			return null;
		}
		return vertex[n];
	}
}