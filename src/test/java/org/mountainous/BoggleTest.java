/**
 * Test the behaviour of the Boggle board.
 */
package org.mountainous;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;

/**
 * @author JonathanDavid
 */
public class BoggleTest extends TestCase {

   private static String BOARD_RESOURCE         = "testBoard.txt";

   // These words should be found on the board.
   private static String GOOD_WORDLIST_RESOURCE = "testGoodWordlist.txt";

   // These words should not be found on the board.
   private static String BAD_WORDLIST_RESOURCE  = "testBadWordlist.txt";

   private Board         board;

   /**
    * @throws java.lang.Exception
    */
   @Before
   public void setUp() throws Exception {
      this.board = new Board();
      this.board.load(BOARD_RESOURCE);
   }

   public void testGoodWordlist() throws IOException {

      List<String> words = Wordlist.load(GOOD_WORDLIST_RESOURCE);

      // This test includes the following:
      // A single letter.
      // A word straight across the top.
      // A word straight down the side.
      // A word backwards (left-to-right).
      // A word upside down (top-to-bottom).
      // A diagonal word.
      // A word twisted around itself.
      // Some "Qu" words.
      for (int i = 0; i < words.size(); i++) {
         assertTrue(board.find(words.get(i)));
      }
   }

   public void testBadWordlist() throws IOException {

      List<String> words = Wordlist.load(BAD_WORDLIST_RESOURCE);

      // This test includes the following:
      // A letter clearly in the data file, but it should not be found.
      // Lines in the data file become truncated when they are longer than
      // others, and the "X" is definitely hanging out a limb.
      // Words that use the same letter twice should not be allowed.
      for (int i = 0; i < words.size(); i++) {
         assertFalse(board.find(words.get(i)));
      }
   }

   /**
    * Run through some rigged games and check what the percentage of words found
    * should be.
    */
   public void testGames() throws Exception {

      // Run through the test resources.
      String[] testArgs = new String[] { "0", "testBoard.txt",
            "testDictionary.txt", "testMyFoundWords.txt" };
      Boggle.main(testArgs);

      // This scenario should yield the following:
      // Words found: 1 of 2 (50%)
      assertEquals("50", testArgs[0]);

      // Run through the blank test resources.
      String[] testZeroArgs = new String[] { "0", "testBoard.txt",
            "testZeroDictionary.txt", "testZeroMyFoundWords.txt" };
      Boggle.main(testZeroArgs);

      // This scenario should yield the following:
      // Words found: 0 of 0 (100%)
      assertEquals("100", testZeroArgs[0]);
   }

   /**
    * Hit constructors and code that isn't covered anywhere else.
    * 
    * @throws Exception
    */
   public void testIncreaseTestCoverage() throws Exception {

      new Wordlist();
      new Boggle();
      assertNotSame(0, this.board.toString().length());

      // Run through the files you would use if you were to run the game as a
      // stand-alone application.
      Boggle.main(null);
   }

}
