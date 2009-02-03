package org.mountainous;

/**
 * Contains information for an individual letter on the Boggle board.
 * 
 * @author JonathanDavid
 */
public class Letter extends Board {

   public static final Character BORDER_CHAR = '*';

   char                          value;

   // Letters are not visited until searches begin.
   boolean                       visited     = false;

   /**
    * Create a new letter.
    * 
    * @param newValue
    *           The character (from A-Z). Case in-sensitive.
    * @param newX
    *           Horizontal offset from top left-hand corner.
    * @param newY
    *           Vertical offset from top left-hand corner.
    */
   public Letter(final Character newValue) {
      this.setValue(newValue);
   }

   /**
    * Gets the character value for this grid position on the board. When the
    * "visited" flag has been set, use an upper-case letter. This can be useful
    * for showing the location for the result of a particular search.
    * 
    * @return
    */
   public char getValue() {
      if (this.isVisited()) {
         return Character.toUpperCase(this.value);
      }
      return this.value;
   }

   /**
    * Sets the character value for this grid position on the board. To make
    * things easy, when the board assigns a character the null value, that
    * indicates that this letter appears on the boarder of the board.
    * 
    * @param newValue
    */
   private void setValue(Character newValue) {
      if (null == newValue) {
         newValue = BORDER_CHAR;
      }
      this.value = Character.toLowerCase(newValue);
   }

   /**
    * Has this letter position already been visited during the current search?
    * 
    * @return
    */
   public boolean isVisited() {
      return this.visited;
   }

   public void setVisited(final boolean isVisited) {
      this.visited = isVisited;
   }
}
