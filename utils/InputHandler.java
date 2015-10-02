/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import model.BlackAnt;
import model.RedAnt;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

/**
 *
 * @author Giorgos
 */
public class InputHandler {
    public static void createRandomInput(String filename, int population){
        try{
            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            int x = population;
            Random randomGenerator = new Random();
            for(int i = 0; i < x; ++i){
                int capacity = 1000 + randomGenerator.nextInt(1000);
                int item1v = 10 + randomGenerator.nextInt(30);
                int item2v = 10 + randomGenerator.nextInt(30);
                int item3v = 10 + randomGenerator.nextInt(30);
                int item4v = 10 + randomGenerator.nextInt(30);
                int item5v = 10 + randomGenerator.nextInt(30);
                out.write("0 " + randomGenerator.nextDouble() + " " + randomGenerator.nextDouble() + " " + capacity + "\n");
                out.write("1 " + randomGenerator.nextDouble() + " " + randomGenerator.nextDouble() + " " + item1v + " " + item2v + " " + item3v + " " + item4v + " " + item5v + "\n");
            }
            out.close();
        }
        catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public static void readInput(String filename, List<RedAnt> redAnts, List<BlackAnt> blackAnts){
        BufferedReader InputFile = null;
        try{
            String InputLine;
            InputFile = new BufferedReader(new FileReader(filename));
            int id_red = 0;
            int id_black = 0;
            int[] objects = new int[5];
            while((InputLine = InputFile.readLine()) != null){
                double x;
                double y;
                String[] InputValues = InputLine.split(" ");
                int value = Integer.parseInt(InputValues[0]);
                if(value == 0){
                    x = Double.parseDouble(InputValues[1]);
                    y = Double.parseDouble(InputValues[2]);
                    int capacity = Integer.parseInt(InputValues[3]);
                    RedAnt redAnt = new RedAnt(id_red, x, y, capacity);
                    redAnts.add(redAnt);
                    ++id_red;
                }
                else if(value == 1){
                    x = Double.parseDouble(InputValues[1]);
                    y = Double.parseDouble(InputValues[2]);
                    objects = new int[]{Integer.parseInt(InputValues[3]), 
                                        Integer.parseInt(InputValues[4]), 
                                        Integer.parseInt(InputValues[5]), 
                                        Integer.parseInt(InputValues[6]), 
                                        Integer.parseInt(InputValues[7])};
                    BlackAnt blackAnt = new BlackAnt(id_black, x, y, objects);
                    blackAnts.add(blackAnt);
                    ++id_black;
                }
                else{
                    System.err.println("Wrong ant id value, must be 0 or 1.");
                }
            }
            InputFile.close();
        }
        catch(Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}
