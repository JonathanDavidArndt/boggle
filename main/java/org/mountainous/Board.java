package org.mountainous;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonathanDavid
 */
public class Board extends Boggle {

   private int        width  = 0;

   private int        height = 0;

   private Letter[][] grid   = null;

   public Board() {
   }

   /**
    * Create a two-dimensional grid from a file resource.
    * 
    * @param resourceName
    * @throws Exception
    */
   public void load(final String resourceName) throws Exception {

      InputStream boardStream = Board.class.getResourceAsStream(resourceName);
      BufferedReader board = new BufferedReader(new InputStreamReader(
            boardStream));

      int minWidth = 0;
      String nextLine;
      List<String> boardLines = new ArrayList<String>();

      while (null != (nextLine = board.readLine())) {

         // Only recognize letters A-Z in the data file.
         nextLine = Boggle.removeNonLetters(nextLine);

         int width = nextLine.length();
         if (0 < width) {
            boardLines.add(nextLine);

            // Remember the longest and shortest lines.
            if (minWidth < 1 || width < minWidth) {
               minWidth = width;
            }
         }
      }

      // Width of the board is the size of the shortest row.
      this.setWidth(minWidth);

      // Height of the board is the number of lines.
      this.setHeight(boardLines.size());

      // Initializes a new grid with new dimensions--based on the number and
      // length of the board lines loaded above.
      // Clears the contents of the current grid (if there is one).
      this.clearGrid();

      // Create the part of the grid with letters (A-Z).
      this.setGridValues(boardLines);

      // Set the outside boarder to be blank (this helps during searches through
      // the board grid).
      this.setGridBorder();

      board.close();
   }

   /**
    * Fill in the inside areas of the Boggle board. This is the part with actual
    * letters and words.
    * 
    * @param boardLines
    */
   private void setGridValues(final List<String> boardLines) {

      // Fill the grid with information from the Boggle board file.
      for (int y = 1; y <= this.height; y++) {
         for (int x = 1; x <= this.width; x++) {

            // Add the next character in the proper position on the grid.
            this.setGridValue(x, y, boardLines.get(y - 1).charAt(x - 1));
         }
      }
   }

   /**
    * Fill the outside boarders of the grid with blanks. This speeds searching,
    * as you do not need to continually check if you are about to run
    * out-of-bounds.
    */
   private void setGridBorder() {

      // Fill in the top and bottom of the board with question marks.
      for (int x = 0; x < this.width + 2; x++) {
         this.setGridValue(x, 0, null);
         this.setGridValue(x, this.getHeight() + 1, null);
      }

      // Fill in the sides of the board with question marks.
      for (int y = 0; y < this.height + 2; y++) {
         this.setGridValue(0, y, null);
         this.setGridValue(this.getWidth() + 1, y, null);
      }
   }

   /**
    * Reset the "visited" flag on each letter in the grid. It would be
    * appropriate to call this method immediately after finishing a search, or
    * immediately before starting a new search.
    */
   public void resetGrid() {
      for (int y = 0; y < this.height + 2; y++) {
         for (int x = 0; x < this.width + 2; x++) {
            this.setGridVisited(x, y, false);
         }
      }
   }

   /**
    * Initializes a new grid with new dimensions--based on the number and length
    * of the board lines loaded above. Clears the contents of the current grid
    * (if there is one).
    */
   private void clearGrid() {
      this.grid = new Letter[this.width + 2][this.height + 2];
   }

   /**
    * Can this word be found somewhere in the grid?
    * 
    * @param word
    * @return
    */
   public boolean find(String word) {

      this.resetGrid();

      // If this word has a 'Qu', knock off the 'u' part.
      // Pretend like we already found that bit.
      word = word.replaceAll("qu", "q");

      for (int y = 0; y < this.height + 2; y++) {
         for (int x = 0; x < this.width + 2; x++) {

            // Is the first letter of this word at the current grid location?
            if (word.charAt(0) == this.getGridValue(x, y)) {
               if (this.findAt(x, y, word)) {
                  return true;
               }
            }
         }
      }
      return false;
   }

   /**
    * Can the rest of this word-fragment be found starting at the current
    * location?
    * 
    * @return
    */
   private boolean findAt(final int x, final int y, final String fragment) {

      // If the next character in the word-fragment doesn't match the current
      // character, there's no point in going on.
      // It also doesn't count if this letter has been visited already.
      char hopeful = fragment.charAt(0);
      char actual = this.getGridValue(x, y);

      if (hopeful != actual || this.isGridVisited(x, y)) {
         return false;
      }

      // If this is the last character in the word, we have a winner!
      if (fragment.length() < 2) {
         return true;
      }

      // Check the various letters surrounding the current letter.
      // Mark the current letter as being used before continuing.
      this.setGridVisited(x, y, true);
      if (this.findAt(x - 1, y - 1, fragment.substring(1))) {
         return true;
      }
      if (this.findAt(x, y - 1, fragment.substring(1))) {
         return true;
      }
      if (this.findAt(x + 1, y - 1, fragment.substring(1))) {
         return true;
      }
      if (this.findAt(x - 1, y, fragment.substring(1))) {
         return true;
      }
      if (this.findAt(x + 1, y, fragment.substring(1))) {
         return true;
      }
      if (this.findAt(x - 1, y + 1, fragment.substring(1))) {
         return true;
      }
      if (this.findAt(x, y + 1, fragment.substring(1))) {
         return true;
      }
      if (this.findAt(x + 1, y + 1, fragment.substring(1))) {
         return true;
      }

      // Looks like we didn't end up using this letter after all!
      this.setGridVisited(x, y, false);
      return false;
   }

   /**
    * The width of the current grid.
    * 
    * @return
    */
   public int getWidth() {
      return this.width;
   }

   private void setWidth(final int newWidth) {
      this.width = newWidth;
   }

   /**
    * The height of the current grid.
    * 
    * @return
    */
   public int getHeight() {
      return this.height;
   }

   private void setHeight(final int newHeight) {
      this.height = newHeight;
   }

   /**
    * Get the character value at a particular set of coordinates in the grid.
    * 
    * @param x
    *           Horizontal offset from top left-hand corner.
    * @param y
    *           Vertical offset from top left-hand corner.
    */
   private char getGridValue(final int x, final int y) {
      return this.grid[x][y].getValue();
   }

   /**
    * Set the character value at a particular set of coordinates in the grid.
    * 
    * @param x
    *           Horizontal offset from top left-hand corner.
    * @param y
    *           Vertical offset from top left-hand corner.
    * @param value
    *           The character value to assign this letter (A-Z, case
    *           in-sensitive).
    */
   private void setGridValue(final int x, final int y, final Character value) {
      this.grid[x][y] = new Letter(value);
   }

   /**
    * Has this particular letter has already been visited in this search?
    * 
    * @param x
    *           Horizontal offset from top left-hand corner.
    * @param y
    *           Vertical offset from top left-hand corner.
    */
   private boolean isGridVisited(final int x, final int y) {
      return this.grid[x][y].isVisited();
   }

   /**
    * This particular letter has already been visited in this search.
    * 
    * @param x
    *           Horizontal offset from top left-hand corner.
    * @param y
    *           Vertical offset from top left-hand corner.
    * @param isVisited
    *           Visited flag.
    */
   private void setGridVisited(final int x, final int y, final boolean isVisited) {
      this.grid[x][y].setVisited(isVisited);
   }

   /**
    * Create a visual representation of the Boggle board.
    */
   public String toString() {

      StringBuilder board = new StringBuilder();

      for (int y = 0; y < this.height + 2; y++) {
         for (int x = 0; x < this.width + 2; x++) {
            board.append(this.grid[x][y].getValue() + " ");
         }
         board.append("\n");
      }

      return board.toString();
   }

   /**
    * Create a printable representation of the Boggle board.
    * 
    * @return
    */
   public String[] toStringArray() {

      String[] boardArray = new String[this.height + 2];

      for (int y = 0; y < this.height + 2; y++) {

         StringBuilder board = new StringBuilder();

         for (int x = 0; x < this.width + 2; x++) {
            board.append(this.grid[x][y].getValue() + " ");
         }
         boardArray[y] = board.toString();
      }

      return boardArray;
   }
}
