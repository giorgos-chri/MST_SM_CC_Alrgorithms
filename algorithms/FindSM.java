package algorithms;

import model.Edge;
import java.util.ArrayList;

//To generate a more integrated JavaDoc HTML file we must Include Private and Private Package Members!!!

/**
 * @author Χριστάκης Γεώργιος
 * @aem 1559
 * @email nchrista@csd.auth.gr
 */
public class FindSM {
    /**
     * ArrayList with all the edges between the red and the black ants in ascending order.
     */
    private final ArrayList<Edge> redBlackEdges;
    
    /**
     * The number of the ants of each color.
     */
    private final int numOfAntsPerColour;
    
    /** 
     * For each red ant keeps the black ants id's in preference serie.
     * <p>
     * It is a 2D array of size numOfAntsPerColour * (numOfAntsPerColour + 1).
     * For example if we have 5 red ants and 5 black, the array will have 5 rows and 6 columns.
     * Each row refers to the red ant with the specific id, i.e redTable[0][] refers to the red ant with id = 0. The first 5 
     * values will have the black ants id's preferred by that red ant (first is the most preferred).
     * Finally in the last position of the row we will have the id of the black ant that is matched
     * with the red. </p>
     * <p>
     * <b>NOTE:</b> The last column is the only one that changes after the initialization of the array
     * and the values that will have at the end of the stable marriage will be the match of the red ants.
     * So if the values of the second row is: [4, 3, 1, 0, 2, 1] means that the red ant
     * with id 1 prefers first the black with id = 4 the with id =3 and so on, and is matched with the black ant with id 1.
     * </p>
     */    
    private final int redTable[][]; 
    
   /**
     * For each black ant keeps the preference serie of the red ants.
     * <p>
     * It is a 2D array of size numOfAntsPerColour * (numOfAntsPerColour + 1).
     * For example if we have 5 red ants and 5 black, the array will have 5 rows and 6 columns.
     * Each row refers to the black ant with the specific id, i.e blackTable[3][] refers to the black ant with id = 3.
     * The last position of the row will have the id of the red ant that is matched with the specific
     * black (-1 in the begining - meaning no match). The first 5 values will have the red ants preferred series according to each red ant. <br> 
     * <b>More detailed: </b> I.e. If the third row (blackTable[2][]) contains the balues {3,2,1,5,4,-1} means that: <br>
     * Last value is the black ants match (-1 in the start - no match)<br>
     * The second value (3) means that the red ant with id = 0 is the 3rd prefered for the black ant.<br>
     * The thrid value (2) means that the red ant with id = 1 is the 2nd prefered for the black ant.<br>
     * The forth value (1) means that the red ant with id = 2 is the 1st prefered for the black ant.<br>
     * and so on...<br>
     * So this means that each column referes to the red ants ids.
     * This gives us the opportunity to find if each black ant, prefers the new red ant propose to it, or the old red ant
     * that it is already matched with. We have the result in O(1) time.
     * </p>
     * <p>
     * <b>NOTE:</b> The last column is the only one that changes after the initialization of the array
     * and the value that will have at the end of the stable marriage will be the match of the black ants.
     * So if the values of the second row is: [4, 3, 1, 0, 2, 1] means that the red ant
     * with id 1 prefers first the black with id = 4 the with id =3 and so on, and is matched with the black ant with id 1.
     * </p>
     */       
    private final int blackTable[][];
    
    /**
     * Keeps the position of the next black ant preferred by the red ants, 
     * durring the initialization of redTable[][] and while calculating the stable marriage.
     * <p>
     * <b>Durring initialization of redTable[][]:</b><br>
     * The position redCounter[1] refers to the red ant with
     * id equal to 1. And if redCounter[1] == 3 means that the next black ant that will 
     * have an edge with red ant 1 in the redBlackEdges ArrayList will be placed in 
     * redTable[1][3] position.
     * </p>
     * <p>
     * <b>Durring calculation of the sable marriage:</b><br>
     * When we iterate each red ants choices to find a match, we keep in redCounter the position
     * of the black ant prefered this time. We do so that when a red ant gets rejected from his choice 
     * (the black ant it was matched prefered a new red), we don't have to iterate all its choices from the 
     * beginning but we find in O(1) time the next choice it has. <br> 
     * </p>
     */
    private final int redCounter[];
    
    /**
     * Keeps the position of the next red ant preferred by the black ants 
     * when the blackTable[][] array is initialized.
     * <p>
     * More specifically the position blackCounter[1] refers to the black ant with
     * id equal to 1. And if blackCounter[1] == 3 means that the next red ant that will 
     * have an edge with black ant 1 in the redBlackEdges ArrayList will be placed in 
     * blackTable[1][3] position.
     * </p>
     */
    private final int blackCounter[];
    
    /**
     * The final table with the matches between the ants consisting the stable marriage.
     * <p>It is a 2D array with rows equal to the number of the red ants (or the black, they are the same)
     * and 2 columns.
     * In the first column we set the id of the red ants and in the second 
     * column we set the id of the black ant each red is matched with.
     * </p>
     */
    private final int finalSMTable[][];
    
    /**
    * Constructor.
    * @param redBlackEdges ArrayList with all the edges between the red and black ants in ascending order.
    * @param numOfAntsPerColour The number of ants of each color
    */
    public FindSM(ArrayList<Edge> redBlackEdges, int numOfAntsPerColour){
	this.redBlackEdges = redBlackEdges;
	this.numOfAntsPerColour = numOfAntsPerColour;
	redTable = new int[numOfAntsPerColour][numOfAntsPerColour + 1];
	blackTable = new int[numOfAntsPerColour][numOfAntsPerColour + 1];
	finalSMTable = new int[numOfAntsPerColour][2];
        redCounter = new int[numOfAntsPerColour];
        blackCounter = new int[numOfAntsPerColour];
    }
	
    /**
    * Contains all the methods needed to compute a stable marriage between the red
    * and the black ants.
    * We set the red ants making the proposals (color id 0) although if the black ants was 
    * making the proposals the result would also be stable BUT maybe different.
    * @return the final table with the matches between the ants.
    */
        
    public int[][] calculateStableMarriage(){
        //Initialize the tables used for Stable Marriage
        initializeTables();
        
        findSM();
        
        //Gets the couples from the redTable and puts the in finalSMTable
	for(int i=0; i<numOfAntsPerColour; ++i){
            //Sets in the first column the red ants id's [0 1 ... numOfAntsPerColour-1].
            finalSMTable[i][0] = i;
            //Gets the last column value from redTable array where is the black ants id matched with the red ant
            //and stores it in finalSMTable array in the second column.
            finalSMTable[i][1] = redTable[i][numOfAntsPerColour];
	}
	return finalSMTable;
    }
        
    /**
     * Initializes all the Tables used for the calculation of the Stable Marriage.
     * <p>
     * The tables initialized are redCounter[], blackCounter[] 
     * and redTable[][], blackTable[][].<br>
     * redCounter[] is initialized to 0 because the first value added in the redTable array will be in position 0 (first place).<br>
     * blackCounter[] is initialized to 1 beacuse for each black ant the first red ant found is its first choice.<br>
     * </p>
     * <h6>See Also:</h6>
     * <ul>
     * <li>{@link #redCounter} </li>
     * <li>{@link #blackCounter} </li>
     * <li>{@link #redTable} </li>
     * <li>{@link #blackTable} </li>
     * </ul>
     */
    private void initializeTables(){
        
        for(int i=0; i<numOfAntsPerColour; ++i){
            //redCounter[] and blackCounter[] are set equal to 0 as the first value added in 
            //redTable and blackTable will be in their first position.
            redCounter[i] = 0;
            blackCounter[i] = 1;
            //Initializes the match of all red ants and black ants (last column) to -1(not existind ant).
            redTable[i][numOfAntsPerColour] =-1;
            blackTable[i][numOfAntsPerColour] =-1;
	}   
                
        for(Edge edge : redBlackEdges){
	    int from = edge.getFrom();
            int to = edge.getTo();
            //Gets the column where the new black ant id will be stored in redTable
            int redCount = redCounter[from];
            redTable[from][redCount] = to;
            //Updates redCounter
            ++redCounter[from];
            
            blackTable[to][from] = blackCounter[to];
            //Updates blackCounter
            ++blackCounter[to];
	}
    }
	
    /**
     * Calculates a Stable Marriage between the red and the black ants.
     * <p>
     * The ants making the proposal are the red ants. So we iterate the red ants and for every red ant
     * we start iterating its preferred black ants until we find one that can be matched.</p>
     * <p>
     * To check if the black ant is not been matched or if it is matched with which red ant it is, we see the last column of blackTable[][] for the specific 
     * black ant. The time needed is O(1). <br>
     * If the 2 ants are already matched with each other we stop the iteration of the black ants and we move on to the next red ant.<br>
     * If the black ant does not have a match (last column -1) we match the two ants and move on to the next red.<br>
     * Else if the black ant is already matched we must determine if the black ant
     * prefers to be matched with the new red ant or the old. If the black ant prefers the old
     * red ant (uses blackTable array and gets the result in O(1)) we do nothing and continue to the next red ant's choice. But if the black ant prefers 
     * the new red (uses blackTable array and gets the result in O(1)) we make them couple and we start searching for a new match for the old red ant that
     * was matched with black. The next choice of the old red ant is accessed through the redCounter array so that 
     * it can be find in O(1) time.
     * </p>
     */
    private void findSM(){
        //for every row of redTable.
	for(int redID=0; redID<numOfAntsPerColour; ++redID){
            //for every column of the row redID of redTable.
            for(int nextBlackAnt=0; nextBlackAnt<numOfAntsPerColour; ++nextBlackAnt){
                //Update redCounter so if we change the match of the red ant with ID = redID again,
                //we can access the next black ant is prefers int O(1) time.
                redCounter[redID] = nextBlackAnt;
                //We get the next preferred ant for red ant with id = redID.
		int blackAntRedPrefers = redTable[redID][nextBlackAnt];
                //If the black ant has no match (last column = -1)
                //make it couple with the red that prefers it.
		if(blackTable[blackAntRedPrefers][numOfAntsPerColour] == -1){
                    //Update the red ant in the redTable with the id of the black ant is has matched.
                    redTable[redID][numOfAntsPerColour] = blackAntRedPrefers;
                    //Update the black ant in the blackTable with the id of the red ant is has matched. 
                    blackTable[blackAntRedPrefers][numOfAntsPerColour] = redID;
                    //Stop iterating the columns of this red ant beacuse it has matched.
                    break;
		}
                //If the red ant has already matched with the black ant it prefers 
                else if(blackTable[blackAntRedPrefers][numOfAntsPerColour] == redID){
                    //Do nothing...
                    break;
                }
                //Else if the black ant that red prefers has already been matched.
		else {
                    //Get the red ant's id that black is matched with.
                    int blackChoice = blackTable[blackAntRedPrefers][numOfAntsPerColour];
                    //Checks if the old red ant is in lower preference of the new red ant the black is matched with.
                    //If it is true change the matching..
                    if(blackTable[blackAntRedPrefers][blackChoice] > blackTable[blackAntRedPrefers][redID]){
                        //Update the red ant in the redTable with the id of the black ant is has matched.
                        redTable[redID][numOfAntsPerColour ] = blackAntRedPrefers;
                        
                        //Get the id of the red ant black was matched with before the new match.
                        int oldRedPrefered = blackTable[blackAntRedPrefers][numOfAntsPerColour];
                        
                        //Update the black ant in the blackTable with the id of the red ant is has matched.
                        blackTable[blackAntRedPrefers][numOfAntsPerColour] = redID;
                        
                        //Set the match of the old red ant to -1 (meaning that it is not matched)
                        redTable[oldRedPrefered][numOfAntsPerColour] = -1;
                        
                        //Update redCounter so if we change match of the red ant with ID = redID again,
                        //We can access the next black ant is prefers int O(1) time.
                        redCounter[redID] = nextBlackAnt;
                        
                        //Set redID to the id of the old red ant. So that it get matched with another black ant.
                        redID = oldRedPrefered;
                        
                        //Set j to the position of the next black ant the old red prefers.
                        nextBlackAnt = redCounter[oldRedPrefered];
                    }					
		}
            }
	}
    }    
}