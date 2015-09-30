package algorithms;

import model.BlackAnt;
import model.RedAnt;
import java.util.Arrays;

//To generate a more integrated JavaDoc HTML file we must Include Private and Private Package Members!!!

/**
 * @author Χριστάκης Γεώργιος
 * @aem 1559
 * @email nchrista@csd.auth.gr
 */
public class FindCoinChange {
    /**
     * Is the total weight of seeds the red ant can carry.
     */
    private int capacity;
    
    /**
    * Contains the weight of each seed that the black ant holds in ascending order.
    */
    private final int[] objects;
    
    /**
    * Contains the min number of individual objects needed for a specific value.
    * E.g. If fewestSeeds[10] = 3 means that for weight 10 the min number of seeds needed are 3.
    */
    private final int[] fewestSeeds;
    
    /**
     * Contains the seed that must be taken next so that we will have the min number of seeds in the end.
     * E.g. If bestFirstSeeds[20] = 8 means that for weight 20 the next seed that must be
     * chosen must be the one with weight 8.
     */
    private final int[] bestFirstSeeds;
    
    /**
     * The final table with the quantity of each object chosen.
     * <p>
     * E.g. If the coin change array have values: 0  2  3  1<br>
     * and the objects the black ant have are:   4 12 15 20<br>
     * Means that the red ant will carry 0 seeds that weight 4, 2 seeds that weight 12
     * and so on..</p>
     */
    private final int[] coinChange;
    
    /**
     * The number of different seeds.
     */
    private final int differentSeeds;
    
    /**
     * Constructor
     * @param redAnt the red ant chosen to carry seeds.
     * @param blackAnt the black ant that will give seeds.
     */
    
    public FindCoinChange(RedAnt redAnt, BlackAnt blackAnt){
        capacity = redAnt.getCapacity();
        //objects = new int[] {4,6,8,9,15};
        objects = blackAnt.getObjects();
        //The size of fewestSeeds[] and bestFirstSeeds[] is set to capacity + 1
        //because we assume that there is a seed with weight 0.
        fewestSeeds = new int[capacity+1];
        bestFirstSeeds = new int[capacity+1];
        coinChange = new int[objects.length];
        differentSeeds = objects.length;
    }
    
    
    /**
     * Contains all the methods needed to calculate the min num of seeds the red ant
     * can carry using the Coin Change problem with Dynamic Programming.
     * @return the final an array containing how many seeds are needed of each separate seed, 
     * to get the min number of seeds the red ant can carry.
     * @see ants.FindCoinChange#findCoinChange() 
     * @see ants.FindCoinChange#finalCoinChangeArray() 
     */
    public int[] calculateCoinChange(){
        //Order the objects[] in ascending order
        Arrays.sort(objects);
        
        findCoinChange();       
        
        finalCoinChangeArray();
        
        return coinChange;
    }
    
    
    /**
     * Calculates the min seeds the red ant can carry using the seeds of the black ant,
     * using the coin change algorithm with dynamic programming.
     * <p>
     * To calculate the min number of seeds with dynamic programming for a specific weight
     * the red ant can carry, we must have calculated before the min number of seeds for all 
     * the previous weights. Meaning that if the red ant can carry 60 weight we must have 
     * calculated the min seeds form 1...59.<br>
     * So we will first iterate from 1 until the weight the red ant can carry.<br>
     * Then for each seed the black ant has, we will check if it gives us less number of seeds for
     * the current weight. If yes we set the weight of the seed in the bestFirstSeeds[] array 
     * in the specific position and set the total number of seeds needed for that weight to the
     * fewestSeeds[] array in the specific position. We continue until the seeds black has are 
     * over or until we find a seed weighting more than we can carry.<br>
     * The seeds are sorted so when we find the first seed weighting more all the other seeds after
     * that will also weight more.
     * </p>
     */
    private void findCoinChange(){
        
        //Initialize the first cell of the 2 arrays in 0 meaning that for 0 weight we 
        //will get 0 seeds.
        fewestSeeds[0] = 0;
        bestFirstSeeds[0] = 0; 
        
        
        for(int currentWeight=1; currentWeight<=capacity; ++currentWeight){
            
            //minNumOfSeeds contains the min number of seeds needed for a weight using each seed.It is set to currentWeight so 
            //that when the first time we check the fewestSeeds needed for a specific weight it will be less than currentWeight.
            int minNumOfSeeds = currentWeight;
            //maxSeed is set to 0 so if for example we have left with 3 weight but the lower seed weights 4 the positions from 0 to 3 
            //in bestFirstSeed array will be set to 0.
            int maxSeed = 0;
            //Set the fewestSeeds equal to 0 so that if the smaller seed weights 5 then the values from 0 to 4 will be set to 0 (no seeds). 
            fewestSeeds[currentWeight] = 0;
            for(int seed=0; seed<differentSeeds; ++seed){
                //If seed weight bigger than weight can carried break.
                if(objects[seed] > currentWeight){
                    break;
                }
                //Check if the array containing the min number of seeds (fewestSeeds[]) gives a smaller 
                //number of seeds using the new seed with weight objects[seed], use the new seed.
                //The number of seeds taken is incriment by one because we will use
                //one more seed (the new seed object[seed]).
                else if(fewestSeeds[currentWeight-objects[seed]] +1 <= minNumOfSeeds){
                    //Set the new number of seeds.
                    minNumOfSeeds = fewestSeeds[currentWeight-objects[seed]] + 1;
                    //Set as maxSeed the new seed.
                    maxSeed = objects[seed];
                }
            }
            //After the seeds have been iterated or we have found a seed weighting more than
            //we can update the arrays.
            fewestSeeds[currentWeight] = minNumOfSeeds;
            bestFirstSeeds[currentWeight] = maxSeed;
        }
    }
    
    
    /**
     * Creates the final coin change array using the bestFirstSeeds array.
     * <p>
     * For each seed found in the bestFirstSeeds we check if its value is equal to 0. If it is not means we have more seeds to add
     * to the coinChange array, if it is the coinChange array has finished. In the coinChange array the values are 
     * considered sorted in ascending order.
     * </p>
     */
    private void finalCoinChangeArray(){
        //Initialize the coinChange array to 0 because we don't have any seed inserted.
        //Later for every seeds found we will incriment the value of that seed.
        for(int i=0; i<differentSeeds; ++i){
            coinChange[i] = 0;
        }
        
        //Ends when the bestFirstSeeds array get a value equal to 0;
        while(bestFirstSeeds[capacity] != 0){
            //Get the next seed needed for the min number of seeds. 
            int seed = bestFirstSeeds[capacity];
            int start =0;
            int finish = differentSeeds - 1;
            //Binary search to the objects array so that the position of the searched seed is found. 
            //We need the position beacuse in the same position of the coinChange array we will incriment
            //the value meaning thath we found one more seed of this kind.
            int i=(start+finish)/2;
            while(start <= finish){
                if(seed == objects[i]){
                    coinChange[i] += 1;
                    capacity -= seed;
                    break;
                }
                else if(seed > objects[i]){
                    start = i+1;
                }
                else{
                    finish = i-1;
                }
                i = (start+finish)/2;
            }
            
        }
    }   
}
