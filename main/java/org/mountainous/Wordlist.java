/**
 * Stores a word list. Each word in the list should contain only letters (A-Z,
 * case in-sensitive). Some words will (hopefully) be found on the Boggle board.
 */
package org.mountainous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonathanDavid
 */
public class Wordlist extends Boggle {

   public final static int MIN_WORD_LENGTH = 3;

   public static final List<String> load(final String resourceName)
         throws IOException {

      InputStream dataStream = Boggle.class.getResourceAsStream(resourceName);
      BufferedReader data = new BufferedReader(
            new InputStreamReader(dataStream));

      List<String> words = new ArrayList<String>();

      String nextLine;
      while (null != (nextLine = data.readLine())) {

         // Process the next line according to Boggle standards.
         nextLine = Boggle.removeNonLetters(nextLine.toLowerCase());

         // Filter words that are too short.
         if (MIN_WORD_LENGTH <= nextLine.length()) {
            words.add(nextLine);
         }
      }

      data.close();

      return words;
   }
}
