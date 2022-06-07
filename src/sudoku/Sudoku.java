package sudoku;

import java.io.IOException;
import java.util.Optional;
import backtracking.Configuration;
import backtracking.Backtracker;

/**
 * Sudoku class
 *
 * @date June 7 2022
 * @author Andrew Photinakis
 */
public class Sudoku {

    /**
     * Main method that takes in a puzzle file argument
     * and creates the config for it. The class then implements
     * backtracking in order to solve the puzzle.
     * If a solution is present, it is displayed, otherwise the
     * opposite is shown.
     *
     * @param args game file that is passed in
     * @throws IOException reader
     */
    public static void main(String[] args) throws IOException {
        if(args.length != 2){
            System.out.println("Usage: Java Sudoku filname");
        }else{
            try {
                SudokuConfiguration config = new SudokuConfiguration(args[0]);
                Backtracker bt = new Backtracker();
                Optional<Configuration> sol = bt.solve(config);
                if (sol.isPresent()) {
                    System.out.println("Solution:\n" + sol.get());
                } else {
                    System.out.println("No solution exists!");
                }
                System.out.println(bt.getConfigCount() + " configurations generated.");
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
    

}
