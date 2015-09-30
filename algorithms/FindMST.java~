package algorithms;

import model.Edge;
import java.util.ArrayList;
import java.util.HashMap;

//To generate a more integrated JavaDoc HTML file we must Include Private and Private Package Members!!!

/**
 * @author Χριστάκης Γεώργιος
 * @aem 1559
 * @email nchrista@csd.auth.gr
 */

public class FindMST {
    /**ArrayList that contains the edges that will constitute the MST.  */
    private final ArrayList<Edge> mstEdges;
    
    /**
     * ArrayList that contains all the edges between all the ants in ascending order. 
     */
    private final ArrayList<Edge> allTheEdges;
    
    /**The total number of all the ants (Both red and black).  */
    private final int totalAnts;
    
    /**Array that is used by the UNION FIND algorithm and contains for each ant
    * the "leader" of the subtree that it is part of.
    * <p>
    * The position disjointSet[0] refers to the ant with id 0 and so on.
    * First each ant has as a leader itself, meaning that each ant is a subtree with only
    * one node.
    * </p>
    */
    private final int[] disjointSet;
    
    /**
     * The final 2D array that contains all the edges that constitute the MST.
     * <p>
     * The size of the array is (totalAnts-1) * 4 (because if we have 10 nodes the MST 
     * will have 9 edges). So if we have 10 ants total we will have 9 edges stored in the array. 
     * The 4 columns will consists of:<br>
     * Ant ID, Ant color, Ant ID, Ant color. I.e. if it has [3,1,8,0] means that the black ant with id 3 connects with the red ant with id 8.
     * </p>
     */
    private final int[][] mstArray;
    
    /**
     * HashMap that is used by the UNION FIND algorithm and contains for each "leader" the ants
     * that belongs to his subtree.
     * <p>It contains as a KEY the leader of a subtree
     * and as a VALUE all the nodes that consist the subtree of that leader.<br>
     * It is used so that we can determine which subtree is smaller so when we update the subtrees to 
     * the new leader we will add to the big subtree the nodes of the small.</p>
     */
    private final HashMap<Integer, ArrayList<Integer>> listOfLeaders;
    
    /**The total number of edges inserted in the MST.
     * When the MST is completed it must equal to the number of the ants -1.
     */
    private int numOfEdgesInMST;
    /**
     * Constructor 
     * @param allTheEdges ArrayList that contains all the connections between the ants
     * @param totalAnts The number of all the ants
     */
    public FindMST(ArrayList<Edge> allTheEdges, int totalAnts){
	this.allTheEdges = allTheEdges;
	this.totalAnts = totalAnts;
	disjointSet = new int[totalAnts];
	mstArray = new int[totalAnts-1][4];
	mstEdges = new ArrayList<>();
	listOfLeaders = new HashMap<>();
	numOfEdgesInMST = 0;
    }
    
    /**
     * Calculates the MST between all the ants.
     * <p>
     * For the calculation of the MST has been implemented the <b>Kruskal's Algorithm</b>
     * and for better performance we use the <b>Union-Find Algorithm</b> to check if a 
     * circle is created when adding a new edge to the MST.
     * The color of the ants connected in the MST doesn't matter, meaning that ants of 
     * the same color can be connected.</p>
     * <p>
     * To calculate the MST we iterate the ArrayList allTheEdges that contain all the posible edges
     * between the ants sorted in ascending order. So the first edge that appears has the lower cost
     * (costs are measured by the euclidean distance between the ants), we continue until there are no
     * other edges or until we have added N-1 edges to the MST (where N are the number of ants - nodes).<br>
     * For every new edge about to be inserted in the MST we must check if there is created a circle, 
     * here is where the union find algorithm is used. For every subtree of ants (nodes) created 
     * we set one of its ants as a leader and all the other ants of that subtree point to that leader. This way we 
     * can determine if 2 ants are in the same subtree by checking their leaders (same leaders mean same subtree). 
     * If they are in the same subtree we skip the edge
     * else we add the edge and update the ants of the smaller subtree to the new leader.
     * </p>
     * @return A 2d array int[red_population+black_population-1][4]. In each row there is the ant 
     * ID and an identifier whether it is from red (0) or black population (1) 
     * I.e., ant id - identifier - ant id -identifier E.g. [21,0,4,1] -> means that the red ant with ID 21 
     * is connected with the black ant with ID 4.
     */
    public int[][] calculateMST(){  
        
        //Initialize the structs used by Union Find Algorithm.
        initializeUnionFindStructs();
        
        
        for(Edge edge : allTheEdges){
            //If the number of edges the MST must contain is reached stop the iteration. 
            if(numOfEdgesInMST == totalAnts-1){
		break;
            }
            int firstAnt = edge.getFrom();
            int secondAnt = edge.getTo();
            //Gets the leader of each ant.
            int firstLeader = findLeader(firstAnt);
            int secondLeader = findLeader(secondAnt);
            /*Checks if the two ants have different leader means that there will not be created a circle 
              if we add their edge in the MST.
            */
            if(firstLeader != secondLeader){
                ++numOfEdgesInMST;
                mstEdges.add(edge);
                //After we add the new edge in the MST we must update the nodes of 
                //the 2 subtrees to the new leader.
                union(firstLeader, secondLeader); 
            }
        }       
        
        //Create the final table with the connections of the MST.
        createFinalMstTable();
        
        return mstArray;
    }
    
    /**
     * Initializes the 2 structures used by the union find algorithm.
     * <p>
     * The array disjointSet is initialized for each ant pointing to itself 
     * (E.g. disjointSet[5] = 5, disjointSet[6]= 6 ...).<br>
     * The HashMap listOfLeaders is initialized for each ant by setting as key value
     * the id of each ant and in the value which is an ArrayList of ants id's we add 
     * the id of this ant. That mean that in the beginning each ant is a subtree with
     * one node - itself.
     * </p> 
     * <h6>See Also:</h6>
     * <ul>
     * <li>{@link #disjointSet} </li>
     * <li>{@link #listOfLeaders} </li>
     * </ul>
     */
    private void initializeUnionFindStructs(){
        for(int i=0; i <totalAnts; ++i){
            disjointSet[i] = i;
            ArrayList<Integer> antsPerLeader = new ArrayList<>();
            antsPerLeader.add(i);
            listOfLeaders.put(i, antsPerLeader);
        }
    }

    
    /**
     * Checks the size of the subtrees that will be joined and call the updateListOfLeader
     * function according of their size.
     * <p>
     * If the first subtree is bigger we will add the nodes of the second subtree to it,
     * else the first subtree will be add to the second.
     * </p>
     * @param firstLeader The key of the first subtree.
     * @param secondLeader The key of the second subtree
     */
    private void union(int firstLeader, int secondLeader){
	if(listOfLeaders.get(firstLeader).size() < listOfLeaders.get(secondLeader).size()){
            updateListOfLeader(secondLeader,firstLeader);
	}
	else{
            updateListOfLeader(firstLeader,secondLeader);
	}
    }
	
    /**
     * Updates the lists of the subtrees and the disjoint set array
     * used by the Union Find algorithm.
     * <p>
     * Adds to the big subtree the nodes of the smaller subtree and
     * changes the leader id to the disjointSet array by setting the new one.
     * </p>
     * @param leaderUpdated The leader id of the big subtree.
     * @param leaderDeleted The leader id of the small subtree.
     */

    private void updateListOfLeader(Integer leaderUpdated, Integer leaderDeleted){
	for(Integer slave : listOfLeaders.get(leaderDeleted)){
            listOfLeaders.get(leaderUpdated).add(slave);
            disjointSet[slave] = leaderUpdated;
	}
        
	listOfLeaders.get(leaderDeleted).clear();
    }
	
    /**
     * Creates the final Table that contains all the connections between the 
     * edges of the MST.
     * <p>
     * We iterate the ArrayList of edges called mstEdges and for every edge
     * we add to the table the proper value according to the id of each ant.
     * </p>
     */
    
    private void createFinalMstTable(){
        int k=0;
        /*
         If it is not clear the reason we subtract totalAnts/2 from the black ants id when we find one
         see: the findAllTheEdges documentation in ants.IP_1559
        */
        for(Edge edge : mstEdges){
            int first = edge.getFrom();
            int second = edge.getTo();
            //Checks if the first ant is red.
            if(first < totalAnts/2){
        	mstArray[k][0] = first;
        	mstArray[k][1] = 0;
            }
            else{
                //To get the initial black ant id we must remove from its current id 
                //the total size of the black ants (totalAnts/2), we do so because it was added to the black ants ids
                //so that we can seperate them for the red ants with the same id. (Black and red ants share the same ids).
        	mstArray[k][0] = first - totalAnts/2;
        	mstArray[k][1] = 1;
            }
            //Checks if the second ant is red.
            if(second < totalAnts/2){
        	mstArray[k][2] = second;
        	mstArray[k][3] = 0;
            }
            else{
                //To get the initial black ant id we must remove from its current id 
                //the total size of the black ants (totalAnts/2), we do so because it was added to the black ants ids
                //so that we can seperate them for the red ants with the same id. (Black and red ants share the same ids).
        	mstArray[k][2] = second - totalAnts/2;
        	mstArray[k][3] = 1;
            }
        ++k;
        }        	
    }
    
    
    /**
     * Finds and returns the leader of the sub tree the ant belongs.
     * @param id The id of the ant which we want to find its leader.
     * @return The leader of the ant
     */
	
    private int findLeader(int id){
	//if(disjointSet[id] != id){
        return disjointSet[disjointSet[id]];
	//}
	//return id;
    }
}