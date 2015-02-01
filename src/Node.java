import java.util.ArrayList;

public class Node{
		private int id; //Node's id
		private ArrayList<Integer> in, out;//in edges and out edges
		
		public Node(int n){
			id = n; //sets the id
			in = new ArrayList<Integer>(0); //initializes its in edges to 0
			out = new ArrayList<Integer>(0); //initializes its out edges to 0
		}
		
		/**
		 * returns the number of in edges
		 * @return in degree
		 */
		public int In(){
			return in.size();
		}
		
		/**
		 * returns node id
		 * @return id
		 */
		public int id(){
			return id;
		}
		
		/**
		 * return node's i in node
		 * @param i - index of node i in this node's in array
		 * @return - node i
		 */
		public int getIn(int i){
			return in.get(i);
		}
		
		/**
		 * return node's i out node
		 * @param i - index of node i in this node's out array
		 * @return - node i
		 */
		public int getOut(int i){
			return out.get(i);
		}
		
		/**
		 * return the out degree
		 * @return - out degree
		 */
		public int Out(){
			return out.size();
		}
		
		/**
		 * add node n to this node's in array
		 * @param n - id
		 */
		public void addIn(int n){
			in.add(n);
		}
		
		/**
		 * add node n to this node's in array
		 * @param n - id
		 */
		public void addOut(int n){
			out.add(n);
		}
	}