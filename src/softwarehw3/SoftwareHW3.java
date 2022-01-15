/* 
Language to Use: JAVA
Author: Tyler Ziggas
Date: September 21, 2021
Class: Intro to Software Profession 4500
Explanation: This program simulates pill taking over multiple days, the amount of days will be the amount of pills multiplied by 2. 
    The simulations will run for all of those days so it can finish all of the pills remaining in the bottle. This program will 
    first ask for two numbers, the first being the amount of pills to start with, and the second being how many simulations of pill 
    taking will happen.   
Central Data Structures: This program utilizes 2D Arrays to store each of the simulations in a different dimension of the array.
    As we want to access every simulation at the end of the program, we want out simulations to be the first dimension while our actual
    data is for each statistic to be the other dimension, so we have an extensive history of the simulations that we have done.
External Files: None
 */

package softwarehw3;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class SoftwareHW3 {

    private static final NumberFormat tableInt = new DecimalFormat("0000"); // Create our formats for displaying information in the table
    private static final DecimalFormat tableRatio = new DecimalFormat("00.00");
    private static final DecimalFormat tableAverage = new DecimalFormat("0000.00");

    public static void main(String[] args) throws IOException {
        pillDescription(); // Print our description of this project
        
        String currentMessage = "Please input the number of pills in the bottle ranging from 1 to 1000: ";
        int totalPills = inputValidation(currentMessage); // Prompt input validation for total of pills, and then simulations after

        currentMessage = "Please input the number of pill bottle emptying simulations you would like to run ranging from 1 to 1000: ";
        int totalSimulations = inputValidation(currentMessage);

        int totalSimsAverage = totalSimulations + 1;
        
        int amountOfDays = totalPills * 2; // The total days will be the total pills * 2
        double[][] currentDay = new double[totalSimsAverage][amountOfDays]; // Create 2D arrays to store our information for each simulation in 
        double[][] currentWholePillsArray = new double[totalSimsAverage][amountOfDays];
        double[][] currentHalfPillsArray = new double[totalSimsAverage][amountOfDays];
        double[][] currentRatioPills = new double[totalSimsAverage][amountOfDays];

        System.out.println("\nThere will be a total of " + totalPills + " in the bottle and we will run " + totalSimulations + " simulations.");
        
        for (int currentSimulation = 0; currentSimulation < totalSimulations; currentSimulation++) { // Go through each simulation
            simulateTakingPills(amountOfDays, totalPills, currentSimulation, currentDay, currentWholePillsArray, currentHalfPillsArray, currentRatioPills);
            printDayResults(amountOfDays, currentSimulation, currentDay, currentWholePillsArray, currentHalfPillsArray, currentRatioPills); // Print our results
            
            for (int day = 0; day < amountOfDays; day++) {
                currentDay[totalSimulations][day] += (double) currentDay[currentSimulation][day];
                currentWholePillsArray[totalSimulations][day] += (double) currentWholePillsArray[currentSimulation][day];
                currentHalfPillsArray[totalSimulations][day] += (double) currentHalfPillsArray[currentSimulation][day];
                currentRatioPills[totalSimulations][day] += (double) currentRatioPills[currentSimulation][day];
            }
        }
        
        for (int day = 0; day < amountOfDays; day++) {
                currentDay[totalSimulations][day] /= (double) totalSimulations;
                currentWholePillsArray[totalSimulations][day] /= (double) totalSimulations;
                currentHalfPillsArray[totalSimulations][day] /= (double) totalSimulations;
                currentRatioPills[totalSimulations][day] /= (double) totalSimulations;
            }
        
        printAverages(amountOfDays, totalSimulations, currentDay, currentWholePillsArray, currentHalfPillsArray, currentRatioPills);
    }

    static void pillDescription() { // Function for printing what our description of this project
        System.out.println("This program simulates pill taking over multiple days, the amount of days will be the amount of pills multiplied by 2. ");
        System.out.println("The simulations will run for all of those days so it can finish all of the pills remaining in the bottle. ");
        System.out.println("This program will first ask you for two numbers, the first being the amount of pills you would like to start with, ");
        System.out.println("and the second being how many simulations of pill taking you would like to do.  Please input between ");
        System.out.println("1 and 1000 inclusively for both, and then the simulations will begin.\n");
    }

    static int inputValidation(String message) { // Function for validating our input for either thing asked of 
        boolean retry;
        int input = 0;

        do {
            try {
                System.out.println(message); // Print the message for whichever validation this is given to us by main
                retry = false;
                Scanner scan = new Scanner(System.in);
                input = scan.nextInt();

                if (0 < input && input <= 1000) { // Need to make sure our number is larger than 0, as it needs to be a positive integer equal to or less than 1000

                } else {
                    System.out.println("Please pick a number larger than 0 and smaller than 1001!");
                    retry = true;
                }
            } catch (InputMismatchException ex) { // Catch if there is not a number in the users typed request
                System.out.println("Invalid input, please use a number!");
                retry = true;
            }
        } while (retry);

        return input;
    }

    static void simulateTakingPills(int amountOfDays, int totalPills, int currentSimulation, double[][] currentDay, double[][] currentWholePillsArray, double[][] currentHalfPillsArray, double[][] currentRatioPills) {
        int currentWholePills = totalPills; // Keep track of our statistics, set them
        int totalPillsLeft = totalPills;
        int currentHalfPills = 0;

        for (int counter = 0; counter < amountOfDays; counter++) { // Start the amount of days from the first day
            int minimum = 1;
            currentDay[currentSimulation][counter] = counter + 1; // Fill out our stats to later be shown before a pill is chosen
            currentWholePillsArray[currentSimulation][counter] = currentWholePills;
            currentHalfPillsArray[currentSimulation][counter] = currentHalfPills;
            currentRatioPills[currentSimulation][counter] = (double) currentHalfPills / totalPillsLeft;

            if (currentHalfPills == 0) { // Consume a whole pill if there are no half pills
                currentRatioPills[currentSimulation][counter] = 0.0;
                currentWholePills--;
                currentHalfPills++;

            } else if (currentWholePills == 0) { // Consume a half pill if there are no whole pills
                currentRatioPills[currentSimulation][counter] = 1.0;
                currentHalfPills--;
                totalPillsLeft--;

            } else {
                int currentPill = ThreadLocalRandom.current().nextInt(minimum, totalPillsLeft + 1); // Grab a random number for whether or not the pill is whole or half 
                if (currentPill <= currentHalfPills) { // In the case of a half pill
                    currentHalfPills--;
                    totalPillsLeft--;

                } else { // In the case of a whole pill
                    currentWholePills--;
                    currentHalfPills++;

                }
            }
        }
    }

    static void printDayResults(int amountOfDays, int currentSimulation, double[][] currentDay, double[][] currentWholePillsArray, double[][] currentHalfPillsArray, double[][] currentRatioPills) throws IOException {

        System.out.print("---------------------------Simulation #" + tableInt.format((currentSimulation + 1)) + "---------------------------\n"); // Our headers
        System.out.print("|----Day----||----Whole Pills----||----Half Pills----||----Ratio----||\n");
            
        for (int counter = 0; counter < amountOfDays; counter++) { // Print out each row with the information for each day
            System.out.print("||   " + tableInt.format(currentDay[currentSimulation][counter]) + "   |");
            System.out.print("|        " + tableInt.format(currentWholePillsArray[currentSimulation][counter]) + "       |");
            System.out.print("|       " + tableInt.format(currentHalfPillsArray[currentSimulation][counter]) + "       |");
            System.out.print("|    " + tableRatio.format(currentRatioPills[currentSimulation][counter]) + "    ||");
            System.out.println("");
        }

        System.out.print("----------------------End of This Simulation--------------------------\n\n");
        System.out.println("\n\n Please press ENTER to move on."); // Prompt the user to end the program by pressing enter
        System.in.read();
    }
    
    static void printAverages(int amountOfDays, int averageSimulation, double[][] currentDay, double[][] currentWholePillsArray, double[][] currentHalfPillsArray, double[][] currentRatioPills) throws IOException {
        System.out.print("-------------------------Averages For All Simulations----------------------------\n"); // Our headers
        System.out.print("|------Day-----||------Whole Pills-----||------Half Pills-----||-----Ratio-----||\n");
            
        for (int counter = 0; counter < amountOfDays; counter++) { // Print out each row with the information for each day
            System.out.print("||   " + tableAverage.format(currentDay[averageSimulation][counter]) + "   |");
            System.out.print("|        " + tableAverage.format(currentWholePillsArray[averageSimulation][counter]) + "       |");
            System.out.print("|       " + tableAverage.format(currentHalfPillsArray[averageSimulation][counter]) + "       |");
            System.out.print("|    " + tableAverage.format(currentRatioPills[averageSimulation][counter]) + "    ||");
            System.out.println("");
        }

        System.out.print("-----------------------------End of This Program---------------------------------\n\n");
        System.out.println("\n\n Please press ENTER to move on."); // Prompt the user to end the program by pressing enter
        System.in.read();
    }
}