package org.mountainous;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonathanDavid
 */
public class Boggle extends Object {

   private static final String      BOARD_RESOURCE        = "board.txt";

   private static final String      DICTIONARY_RESOURCE   = "sowpods.txt";

   private static final String      MYFOUNDWORDS_RESOURCE = "myFoundWords.txt";

   private static final PrintStream out                   = System.out;

   public Boggle() {
   }

   public static final void main(final String[] args) throws Exception {

      // Load the Boggle board.
      Board board = new Board();
      board.load(null != arg(args, 1) ? args[1] : BOARD_RESOURCE);

      // What does this board look like?
      String[] boardArray = board.toStringArray();
      for (int i = 0; i < boardArray.length; i++) {
         out.println("[BOARD] " + boardArray[i]);
      }
      out.println();

      // What words should we search for?
      List<String> dictionaryWords = Wordlist
            .load(null != arg(args, 2) ? args[2] : DICTIONARY_RESOURCE);

      // What words from the dictionary can be found on the board?
      List<String> dictionaryWordsFound = new ArrayList<String>();

      // What words did we find ourselves?
      List<String> myWordsFound = Wordlist.load(null != arg(args, 3) ? args[3]
            : MYFOUNDWORDS_RESOURCE);

      // Search the board!
      // Display a list of words you found that are also in the dictionary.
      out.println("[INFO] Words you found on the board: ");
      int myWordsFoundCount = 0;
      for (int i = 0; i < dictionaryWords.size(); i++) {
         final String word = dictionaryWords.get(i);

         if (board.find(word)) {
            dictionaryWordsFound.add(word);

            if (myWordsFound.contains(word)) {
               out.println("[FOUND] " + word);
               myWordsFoundCount++;
            }
         }
      }
      if (myWordsFoundCount < 1) {
         out.println("[FOUND] <none>");
      }
      out.println();

      // Display a list of words that you thought you found (but they're
      // actually not found on the board).
      out.println("[INFO] Words you thought you found (but actually didn't): ");
      int myWordsNotFoundCount = 0;
      for (int i = 0; i < myWordsFound.size(); i++) {
         final String word = myWordsFound.get(i);

         if (dictionaryWords.contains(word)
               && !dictionaryWordsFound.contains(word)) {
            out.println("[NOTFOUND] " + word);
            myWordsNotFoundCount++;
         }
      }
      if (myWordsNotFoundCount < 1) {
         out.println("[NOTFOUND] <none>");
      }
      out.println();

      // Display a list of words the dictionary found that you did not.
      out.println("[INFO] Words you missed: ");
      int myWordsMissedCount = 0;
      for (int i = 0; i < dictionaryWordsFound.size(); i++) {
         final String word = dictionaryWordsFound.get(i);

         if (!myWordsFound.contains(word)) {
            out.println("[MISSED] " + word);
            myWordsMissedCount++;
         }
      }
      if (myWordsMissedCount < 1) {
         out.println("[MISSED] <none>");
      }
      out.println();

      // Display a list of words that you thought you found (but they're
      // actually not words).
      out.println("[INFO] Words you thought were real words (they're not): ");
      int myNotWordsCount = 0;
      for (int i = 0; i < myWordsFound.size(); i++) {
         final String word = myWordsFound.get(i);

         if (!dictionaryWords.contains(word)) {
            out.println("[NOTAWORD] " + word);
            myNotWordsCount++;
         }
      }
      if (myNotWordsCount < 1) {
         out.println("[NOTAWORD] <none>");
      }
      out.println();

      // Format the numbers to be displayed.
      NumberFormat formatter = new DecimalFormat("#,###,###");

      Integer found = Integer.valueOf(myWordsFoundCount);
      Integer total = Integer.valueOf(myWordsFoundCount + myWordsMissedCount);
      long percent = 0;

      if (0 < total) {
         percent = Math
               .round(100 * (found.doubleValue() / total.doubleValue()));
      }
      else {
         percent = 100;
      }

      out.println("[INFO] Words found: " + formatter.format(found) + " of "
            + formatter.format(total) + " (" + percent + "%)");
      out.println("[INFO] Complete. Exiting.");

      // If arguments have been specified, store the percent found in the first
      // argument.
      // Because strings are mutable, this percentage will be visible in the
      // calling method.
      if (null != args && 0 < args.length) {
         args[0] = String.valueOf(percent);
      }
   }

   /**
    * Process text to remove all non-letters (A-Z, case-insensitive).
    * 
    * @param text
    * @return
    */
   public final static String removeNonLetters(final String text) {
      return text.replaceAll("[^a-zA-Z]", "");
   }

   public final static String arg(String[] args, int index) {

      if (null == args || args.length <= index) {
         return null;
      }
      return args[index];
   }
}
