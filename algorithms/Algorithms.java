
package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import utils.InputHandler;
import model.BlackAnt;
import model.RedAnt;
import model.Edge;
import utils.Visualize;
/**
 *
 * @author Giorgos Christakis
 * @email giorgos_chri@hotmail.com
 */
public class Algorithms {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        checkParameters(args);
        
        //create Lists of Red and Black Ants
        ArrayList<RedAnt> redAnts = new ArrayList<>();
        ArrayList<BlackAnt> blackAnts = new ArrayList<>();
        
        int flag = Integer.parseInt(args[1]);
        //If there is not an input file, create one
        if (flag == 0) {
            InputHandler.createRandomInput(args[0], Integer.parseInt(args[2]));
        }
        //Read the data from the input file
        InputHandler.readInput(args[0], redAnts, blackAnts);
        
        //Set true to the algorithms we want to be executed
        boolean visualizeMST = true;
        boolean visualizeSM = true;
        boolean printCC = true;
        
        
        if(visualizeMST){
            int[][] mst = findMST(redAnts, blackAnts);
            if (mst != null) {
                Visualize sd = new Visualize(redAnts, blackAnts, mst, null, "Minimum Spanning Tree");
                sd.drawInitialPoints();
            }
        }

        if(visualizeSM){
            int[][] matchings = findStableMarriage(redAnts, blackAnts);
            if (matchings != null) {
                Visualize sd = new Visualize(redAnts, blackAnts, null, matchings, "Stable Marriage");
                sd.drawInitialPoints();
            }
        }

        if(printCC){
            int[] coinChange = coinChange(redAnts.get(0), blackAnts.get(0)); 
            System.out.println("Capacity: " + redAnts.get(0).getCapacity());
            for(int i = 0; i < blackAnts.get(0).getObjects().length; i++){
                System.out.println(blackAnts.get(0).getObjects()[i] + ": " + coinChange[i]);
            }
        }
        
        
        
    }
    
    /**
     * Kruskal algorithm implementation, using my own union-find implementation.
     * @param redAnts The red ants population
     * @param blackAnts The black ants population
     * @return A 2d array int[red_population+black_population-1][4]. In each row there 
     * is the ant ID and an identifier whether it is from red (0) or black population (1) 
     * I.e., ant id - identifier - ant id -identifier E.g. [21,0,4,1] -> means that the red ant 
     * with ID 21 is connected with the black ant with ID 4.
     */
    public static int[][] findMST(ArrayList<RedAnt> redAnts, ArrayList<BlackAnt> blackAnts) {
        ArrayList<Edge> allTheEdges;
        allTheEdges = findAllTheEdges(redAnts, blackAnts);
        //Sorts all the edges in the allTheEdges ArrayList in ascending order.
    	Collections.sort(allTheEdges,new Edge()); 
    	FindMST mst = new FindMST(allTheEdges, 2*redAnts.size());
        return mst.calculateMST();
    }
    
    /**
     * Stable Matching algorithm implementation, considering that the red ants (0) do the proposals.
     * @param redAnts The red ants population
     * @param blackAnts The black ants population
     * @return A 2d array int[red_population][2]. The 2nd dimension is of size of 2 as 
     * it expects values in the form [ant_id_1,ant_id_2] where ant_id is the id of the ant. 
     * This means that ant_id_1 is matched with ant_id_2. ant_id_1 should be a red ant!
     */
    public static int[][] findStableMarriage(ArrayList<RedAnt> redAnts, ArrayList<BlackAnt> blackAnts) {
        ArrayList<Edge> redBlackEdges;
        redBlackEdges = findRedBlackEdges(redAnts,blackAnts);
        //Sorts the redBlackEdges ArrayList in ascending order.
    	Collections.sort(redBlackEdges,new Edge());  
        FindSM fsm = new FindSM(redBlackEdges, redAnts.size());
        return fsm.calculateStableMarriage();    	
    }
    
    /**
     * Coin Change algorithm implementation. It is developed like the change return dynamic programming problem.
     * @param redAnt A red ant
     * @param blackAnt A black ant
     * @return An array of size n int[n] (as n the kinds of defferent seeds every black ant holds) with the counts of every item.
     */
    public static int[] coinChange(RedAnt redAnt, BlackAnt blackAnt) {
        FindCoinChange fcc = new FindCoinChange(redAnt, blackAnt);
        return fcc.calculateCoinChange();
    }
    
    /**
     * Calculates the distances between all the ants (red with blacks, red with reds and black with blacks).
     * <p>
     * To store the distances and the ants id we create a class Edge() object for each two ants connecnted
     * and we keep all of the edges in an ArraList called allTheEdges.</p>
     * <p>
     * Because red and black ants share the same id (i.e. if we have 5 red ants and 5 black the id's of the red
     * will be 0,1,2,3,4 and so the blacks). But we want to separate them, we store the red ants
     * with their regular id's and the blacks with a new id equal to their id's + the number of blackAnts (the number of black ants
     * is the same with the number of red ants).</p>
     * <p>
     * I.e. If we have 5 red ants and 5 black the id's of the red ants
     * will be 0,1,2,3,4 and the id's of black ants will be 5,6,7,8,9.
     * </p>
     * @param redAnts an ArrayList that contains all the red ants.
     * @param blackAnts an ArrayList that contains all the black ants.
     * @return an ArrayList that contains all the edges between all the ants.
     * @see Edge
     */    
    public static ArrayList<Edge> findAllTheEdges(ArrayList<RedAnt> redAnts, ArrayList<BlackAnt> blackAnts){
        ArrayList<Edge> allTheEdges = new ArrayList<>();
        //Iterate all the red ants first time
	for(RedAnt r1 : redAnts){
            //Iterate all the red ants second time so that the distances between the red ants can be computed
            for(RedAnt r2 : redAnts){
                if(r1.getID() < r2.getID()){
                    Edge newEdge = new Edge(r1.getID(),r2.getID(),r1.getDistanceFrom(r2));
                    allTheEdges.add(newEdge);
                }
            }
            //Iterate all the black ants so that the distances between the red ants and the black ants can be computed
            for(BlackAnt b : blackAnts){
        	Edge newEdge = new Edge(r1.getID(),b.getID()+blackAnts.size(),r1.getDistanceFrom(b));
        	allTheEdges.add(newEdge);
            }
        }     
        //Iterate all the black ants first time        
        for(BlackAnt b1 : blackAnts){
                //Iterate all the black ants second time so that the distances between the black ants can be computed
        	for(BlackAnt b2 : blackAnts){
        		if(b1.getID() < b2.getID()){
        			Edge newEdge = new Edge(b1.getID()+blackAnts.size(),b2.getID()+blackAnts.size(),b1.getDistanceFrom(b2));
        			allTheEdges.add(newEdge);
        		}
        	}
        }
        return allTheEdges;
    }
    
    /**
     * Calculates all the distances between the red and the black ants.
     * <p>
     * To store the distances and the ants id we create a class Edge() object for each two ants connecnted 
     * and we keep all of the edges in an ArraList called redBlackEdges.</p>
     * <p>
     * Because red and black ants share the same id and we want to separate them, 
     * we store the red ants always first
     * and the black ants always second in the Edge object inserted in the ArrayList.
     * </p>
     * @param redAnts an ArrayList that contains all the red ants.
     * @param blackAnts an ArrayList that contains all the black ants.
     * @return an ArrayList that contains all the edges between the red and the black ants.
     * @see Edge
     */
    
    public static ArrayList<Edge> findRedBlackEdges(ArrayList<RedAnt> redAnts, ArrayList<BlackAnt> blackAnts){
        ArrayList<Edge> redBlackEdges = new ArrayList<>();
        for(RedAnt r1 : redAnts){
            for(BlackAnt b : blackAnts){
                Edge redBlackEdge = new Edge(r1.getID(),b.getID(),r1.getDistanceFrom(b));
        	redBlackEdges.add(redBlackEdge);
            }            
        }   
        return redBlackEdges;
    }  
    
    
    /**
     * Checks if the parameters given by the user for the creation of the ants are correct.
     * @param args Must give 3 parameters.<br> First Argument:  Name of the file the ants will be saved (i.e. input.txt).<br>
     * Second Argument:  0 to create a new random file or 1 if the input file exists allready.<br>
     * Third Argument:  The number of ants we want to create. I.e. if we give as argument 5 we will create 5 red and 5 black ants.
     * (This argument is optional if 1 is given to the 2nd argument)
     */
    private static void checkParameters(String[] args) {
        if (args.length == 0 || args.length < 2 || (args[1].equals("0") && args.length < 3)) {
            if (args.length > 0 && args[1].equals("0") && args.length < 3) {
                System.out.println("3rd argument is mandatory. Represents the population of the Ants");
            }
            System.out.println("Usage:");
            System.out.println("1st argument: name of filename");
            System.out.println("2nd argument: 0 create random file, 1 input file is given as input");
            System.out.println("3rd argument: number of ants to create (optional if 1 is given in the 2nd argument)");
            System.exit(-1);
        }
    }
    
}
