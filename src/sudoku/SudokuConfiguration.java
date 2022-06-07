package sudoku;

import backtracking.Configuration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * SudokuConfiguration class
 *
 * @date June 7 2022
 * @author Andrew Photinakis
 */
public class SudokuConfiguration implements Configuration {

    /* holds the sudoku game board */
    public char[][] gameBoard;
    /* holds the number of rows even though they are the same in every game  */
    public static int numRows;
    /* holds the number of columns even though they are the same in every game  */
    public static int numCols;
    /* variable that represents a blank spot in the puzzle */
    public static char EMPTY = '*';
    /* holds the row of the cursor */
    private int cursorRow;
    /* holds the column of the cursor */
    private int cursorCol;

    /**
     * Configuration constructor that takes in a puzzle file:
     * - attempts to read the file
     * - parses the first line for the row and column count
     * - fills in the rest of the board
     *
     * @param gameFile puzzle to be solved
     * @throws IOException if the BufferedReader fails
     */
    public SudokuConfiguration(String gameFile) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(gameFile))) {
            System.out.println("File: " + gameFile);
            String[] fields = in.readLine().split("\\s+");
            numRows = Integer.parseInt(fields[0]);
            numCols = Integer.parseInt(fields[1]);
            System.out.println("Rows: " + numRows + " Columns: " + numCols);
            this.gameBoard = new char[numRows][numCols];
            String line;
            int col = 0;
            while ((line = in.readLine()) != null) {
                for (int i = 0; i < numRows; i++) {
                    this.gameBoard[col][i] = line.charAt(i);
                }
                col++;
            }
            for (int i = 0; i < numRows; i++) {
                System.out.println(this.gameBoard[i]);
            }

            this.cursorRow = 0;
            this.cursorCol = -1;
        }
    }

    /**
     * Copy constructor that takes in another config and the val to change the board at:
     * - creates a new game board
     * - copies over the row and column cursor values
     * - copies the other game board to the new
     * - changes the value at the row and column cursor
     *
     * @param other SudokuConfig that is passed in
     * @param val char that is put in place of an EMPTY spot on the game board
     */
    private SudokuConfiguration(SudokuConfiguration other, char val) {
        this.gameBoard = new char[numRows][numRows];
        this.cursorRow = other.cursorRow;
        this.cursorCol = other.cursorCol;
        this.updateCursor();
        for (int r = 0; r < numRows; r++) {
            if (numCols >= 0) System.arraycopy(other.gameBoard[r], 0, this.gameBoard[r], 0, numCols);
        }
        this.gameBoard[this.cursorRow][this.cursorCol] = val;
    }

    /**
     * Once a new config is created, if the cursor col value is greater than 9, 9 columns in the puzzle,
     * then it is reset to 0  and the cursor row is advanced which means we have come to the end of a row.
     */
    private void updateCursor() {
        this.cursorCol++;
        if (this.cursorCol == numCols) {
            this.cursorRow++;
            this.cursorCol = 0;
        }
    }

    /**
     * Creates a List of SudokuConfigurations and returns it
     *
     * @return list of sudoku configurations
     */
    @Override
    public List<Configuration> getSuccessors() {
        List<Configuration> successors = new ArrayList<>();
        if (this.cursorRow < numRows && this.cursorCol < numCols) {
            successors.add(new SudokuConfiguration(this, '1'));
            successors.add(new SudokuConfiguration(this, '2'));
            successors.add(new SudokuConfiguration(this, '3'));
            successors.add(new SudokuConfiguration(this, '4'));
            successors.add(new SudokuConfiguration(this, '5'));
            successors.add(new SudokuConfiguration(this, '6'));
            successors.add(new SudokuConfiguration(this, '7'));
            successors.add(new SudokuConfiguration(this, '8'));
            successors.add(new SudokuConfiguration(this, '9'));
        }
        return successors;
    }

    /**
     * Checks if the configuration isValid. What this means is, if the move
     * that was made was as according to game rules:
     * - the same number doesn't exist more than once in a row
     * - the same number doesn't exist more than once in a column
     * - the same number doesn't exist more than once in a 3x3 cube
     *
     * This method is used for minimal pruning to quickly discard invalid configs
     *
     * @return true is the config is valid and false otherwise
     */
    @Override
    public boolean isValid() {

        //check the values in the same cube

        boolean valid;

        //first row cubes
        if(this.cursorRow  < 3 && this.cursorCol < 3){
            boolean cubeDistinction = checkCubes(0,3, 0,3);
            if(!cubeDistinction){
                return false;
            }
        }
        if(this.cursorRow < 3 && this.cursorCol > 2 && this.cursorCol < 6 ){
            boolean cubeDistinction = checkCubes(0,3, 3,6);
            if(!cubeDistinction){
                return false;
            }
        }
        if(this.cursorRow < 3 && this.cursorCol > 5 && this.cursorCol < numCols){
            boolean cubeDistinction = checkCubes(0,3, 6, numCols);
            if(!cubeDistinction){
                return false;
            }
        }

        //second row cubes
        if(this.cursorRow > 2 && this.cursorRow < 6 && this.cursorCol < 3){
            boolean cubeDistinction = checkCubes(3,6, 0,3);
            if(!cubeDistinction){
                return false;
            }
        }
        if(this.cursorRow > 2 && this.cursorRow < 6 && this.cursorCol > 2 && this.cursorCol < 6){
            boolean cubeDistinction = checkCubes(3,6, 3,6);
            if(!cubeDistinction){
                return false;
            }
        }
        if(this.cursorRow > 2 && this.cursorRow < 6 && this.cursorCol > 5 && this.cursorCol < numCols){
            boolean cubeDistinction = checkCubes(3,6, 6, numCols);
            if(!cubeDistinction){
                return false;
            }
        }

        //third row of cubes
        if(this.cursorRow > 5 && this.cursorRow < numRows && this.cursorCol < 3){
            boolean cubeDistinction = checkCubes(6,numRows, 0,3);
            if(!cubeDistinction){
                return false;
            }
        }
        if(this.cursorRow > 5 && this.cursorRow < numRows && this.cursorCol > 2 && this.cursorCol < 6){
            boolean cubeDistinction = checkCubes(6,numRows, 3,6);
            if(!cubeDistinction){
                return false;
            }
        }
        if(this.cursorRow > 5 && this.cursorRow < numRows && this.cursorCol > 5 && this.cursorCol < numCols){
            boolean cubeDistinction = checkCubes(6, numRows, 6,numCols);
            if(!cubeDistinction){
                return false;
            }
        }

        //check the values in the same row
        ArrayList<Character> gfg = new ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            if (this.gameBoard[this.cursorRow][i] != EMPTY) {
                gfg.add(this.gameBoard[this.cursorRow][i]);
            }
        }
        HashSet<Character> hashSet = new HashSet<>(gfg);
        valid = gfg.size() == hashSet.size();
        if (!valid) {
            return false;
        }

        //check the values in the same column
        ArrayList<Character> secondGfg = new ArrayList<>();
        for (int j = 0; j < numRows; j++) {
            if (this.gameBoard[j][this.cursorCol] != EMPTY) {
                secondGfg.add(this.gameBoard[j][this.cursorCol]);
            }
        }
        HashSet<Character> secondHashSet = new HashSet<>(secondGfg);
        valid = secondGfg.size() == secondHashSet.size();
        if (!valid) {
            return false;
        }

        return true;
    }

    /**
     * Helper function used for checking the values in a cube to make sure
     * duplicates do not exist
     *
     * @param rowStart starting row
     * @param rowEnd ending row
     * @param colStart starting column
     * @param colEnd ending column
     * @return boolean to represent whether or not duplicates are present in the cube
     */
    public boolean checkCubes(int rowStart, int rowEnd, int colStart, int colEnd){
        boolean valid;
        ArrayList<Character> characterArrayList = new ArrayList<>();
        for(int i = rowStart; i < rowEnd; i++){
            for(int j = colStart; j < colEnd; j++){
                if(this.gameBoard[i][j] != EMPTY){
                    characterArrayList.add(this.gameBoard[i][j]);
                }
            }
        }
        HashSet<Character> compare = new HashSet<>(characterArrayList);
        valid = compare.size() == characterArrayList.size();
        return valid;
    }

    /**
     * @return true when the board is filled up, this means we have reached a solution
     */
    @Override
    public boolean isGoal() {
        return this.cursorCol == numCols - 1 && this.cursorRow == numRows - 1;
    }

    /**
     * @return string format of the puzzle board
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("   ");
        for (int col = 0; col < numCols; ++col) {
            res.append(col).append(" ");
        }
        res.append(System.lineSeparator());
        res.append("  ");
        res.append("--".repeat(Math.max(0, numCols)));
        res.append(System.lineSeparator());
        for (int i = 0; i < numRows; i++) {
            res.append(i).append("| ");
            for (int j = 0; j < numCols; j++) {
                res.append(this.gameBoard[i][j]).append(" ");
            }
            res.append(System.lineSeparator());
        }
        return res.toString();
    }


}
