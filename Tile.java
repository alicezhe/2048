/* 
 * Class representing each singular tile on the board;
 * includes functions for merging and setting
 * tile values.
 */

public class Tile {
  private int number;
  private boolean merge;
  
  // constructor
  public Tile(int x) {
    number = x;
  }
  
  /* Input: another tile to be merged with current tile.
   * Output: boolean value for whether the two tiles can
   * be merged or not.
   * 
   * Tests if two tiles are compatible for merging (i.e. same
   * value, not null).
   */
  public boolean canMerge(Tile mergeTile) {
    if (mergeTile == null || mergeTile.getNumber() != number) {
      return false;
    }
    return !mergeTile.merge && !merge;
  }
  
  /* Input: another tile to be merged with current tile.
   * Output: integer value of new merged tile.
   * 
   * Finds the value of the new tile after merging current
   * tile with input tile.
   */
  public int merge(Tile mergeTile) {
    merge = true;
    number *= 2;
    
    return number;
  }
  
  /* Input: none.
   * Output: returns tile value.
   * 
   * Getter function for current tile value.
   */
  public int getNumber() {
    return number;
  }
  
  /* Input: boolean.
   * Output: none.
   * 
   * Setter function for current tile's merge state: once 
   * this value has changed to true, the tile cannot be merged 
   * with another tile again during the same move.
   */
  public void isMerged(boolean x) {
    merge = x;
  }
}