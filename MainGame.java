public class MainGame {
  public static void main(String[] args) {
    MainGame newGame = new MainGame();
  }
  
  private Tile[][] tiles = new Tile[4][4];
  
  private int largestValue = 0;
  private int numberOfMoves = 0;
  private static final int TILE_2048 = 2048;
  
  private boolean checkingMoves;
  private boolean gameLost;
  private boolean gameWon;
  
  // constructor
  public MainGame() {
    
    // randomly generate starting tiles
    for (int i = 0; i < 2; i++) {
      addTile();
    }
    
    updateGrid();
    
    while (true) {
      getKeyTyped();
      checkMoves();
      
      // print win/lose statements
      if (gameWon) {
        updateGrid();
        
        PennDraw.clear();
        
        PennDraw.setPenColor(PennDraw.BLACK);
        String moves = "" + numberOfMoves;
        PennDraw.text(0.5, 0.5, "You won with " + moves + " moves!");
        break;
      }
      if (gameLost) {
        PennDraw.clear();
        
        PennDraw.setPenColor(PennDraw.BLACK);
        String moves = "" + numberOfMoves;
        PennDraw.text(0.5, 0.5, "You lost with " + moves + " moves.");
        break;
      }
    }
  }
  
  /* Input: none.
   * Output: none.
   * 
   * Redraws the grid after every move.
   */
  public void updateGrid() {
    PennDraw.clear(203, 191, 183);
    
    // draw grid
    PennDraw.setPenColor(184, 172, 161);
    PennDraw.setPenRadius(0.03);
    PennDraw.line(0, 0.25, 1, 0.25);
    PennDraw.line(0, 0.50, 1, 0.50);
    PennDraw.line(0, 0.75, 1, 0.75);
    PennDraw.line(0.25, 0, 0.25, 1);
    PennDraw.line(0.50, 0, 0.50, 1);
    PennDraw.line(0.75, 0, 0.75, 1);
    
    // draw tiles
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (tiles[i][j] != null) {
          drawTiles(i, j);
        }
      }
    }
  }
  
  /* Input: integer values for row and column position
   * Output: none
   * 
   * Calculates the midpoints of each tile on the grid
   * and prints the tile value using PennDraw.
   */
  public void drawTiles(int row, int col) {
    int number = tiles[row][col].getNumber();
    String numberString = "" + number;
    
    double midRow = 0;
    double midCol = 0;
    
    // error checking
    if (row < 0 || row > 3 || col < 0 || col > 3) {
      throw new RuntimeException("Error: row or column number invalid");
    }
    
    // assign row midpoint
    switch (row) {
      case 0: 
        midRow = 0.125;
        break;
      case 1: 
        midRow = 0.375;
        break;
      case 2:
        midRow = 0.625;
        break;
      case 3:
        midRow = 0.875;
        break;
      default:
        break;
    }
    
    // assign column midpoint
    switch (col) {
      case 0: 
        midCol = 0.125;
        break;
      case 1: 
        midCol = 0.375;
        break;
      case 2:
        midCol = 0.625;
        break;
      case 3:
        midCol = 0.875;
        break;
      default:
        break;
    }
    
    PennDraw.setPenColor(PennDraw.BLACK);
    PennDraw.text(midCol, 1 - midRow, numberString);
  }
  
  /* Input: none.
   * Output: none.
   * 
   * Adds a new tile of value 2 or 4 at a random blank tile.
   */
  public void addTile() {
    int tileNumber;
    
    // assign random row and column
    int row = (int) (Math.random() * 4);
    int col = (int) (Math.random() * 4);
    
    // assign tile value (2 or 4)
    if (Math.random() < 0.5) {
      tileNumber = 4;
    } 
    else {
      tileNumber = 2;
    }
    
    while (tiles[row][col] != null) {
            row = (int) (Math.random() * 4);
            col = (int) (Math.random() * 4);
        }
    
    tiles[row][col] = new Tile(tileNumber);
  }
  
  /* Input: none.
   * Output: none.
   * 
   * Processes key inputs; i.e. relates up and down keys
   * to mergeUp() and mergeDown(), respectively.
   */
  public void getKeyTyped() {
    if (PennDraw.hasNextKeyTyped()) {
      char key = PennDraw.nextKeyTyped();
      
      switch (key) {
        case 's':
          mergeDown();
          break;
        case 'w':
          mergeUp();
          break;
        case 'd':
          mergeRight();
          break;
        case 'a':
          mergeLeft();
          break;
        default:
          break;
      }
    }
  }
  
  /* Input: none.
   * Output: none.
   * 
   * Checks if any moves can be played. If there aren't any moves
   * that can be played, the game ends.
   */
  public void checkMoves() {
    checkingMoves = true;
    boolean canMove = mergeUp() || mergeDown() || 
      mergeRight() || mergeLeft();
    checkingMoves = false;
    
    if (!canMove) {
      gameLost = true;
    }
  }
  
  /* Input: none.
   * Output: none.
   * 
   * Disables any tile from merging more than once during any move.
   */
  public void updateMerge() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (tiles[i][j] != null) {
          tiles[i][j].isMerged(false);
        }
      }
    }
  }
  
  /* Input: none.
   * Output: boolean that is true if it is possible for at least
   * one merge to happen in the upwards direction.
   * 
   * Moves every single tile up (if such a move is possible).
   */
  public boolean mergeUp() {
    boolean canMove = false;
    
    for (int i = 1; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (tiles[i][j] == null) {
          continue;
        }
        
        int newRow = i - 1;
        
        while (newRow < 4 && newRow >= 0) {
          Tile newTile = tiles[newRow][j];
          Tile currentTile = tiles[i][j];
          
          if (newTile == null) {
            if (checkingMoves) {
              return true;
            }
            canMove = true;
            tiles[newRow][j] = currentTile;
            tiles[i][j] = null;
            i = newRow;
            newRow -= 1;
          } 
          else if (newTile.canMerge(currentTile)) {
            if (checkingMoves) {
              return true;
            }
            
            int number = newTile.merge(currentTile);
            
            if (number > largestValue) {
              largestValue = number;
            }
            
            canMove = true;
            tiles[i][j] = null;
            break;
          }
          else {
            break;
          }
        }
      }
    }
    testWinCondition(canMove);
    return canMove;
  }
  
  /* Input: none.
   * Output: boolean that is true if it is possible for at least
   * one merge to happen in the downwards direction.
   * 
   * Moves every single tile down (if such a move is possible).
   */
  public boolean mergeDown() {
    boolean canMove = false;
    
    for (int i = 2; i > -1; i--) {
      for (int j = 0; j < 4; j++) {
        if (tiles[i][j] == null) {
          continue;
        }
        
        int newRow = i + 1;
        
        while (newRow < 4 && newRow >= 0) {
          Tile newTile = tiles[newRow][j];
          Tile currentTile = tiles[i][j];
          
          if (newTile == null) {
            if (checkingMoves) {
              return true;
            }
            canMove = true;
            tiles[newRow][j] = currentTile;
            tiles[i][j] = null;
            i = newRow;
            newRow += 1;
          } 
          else if (newTile.canMerge(currentTile)) {
            if (checkingMoves) {
              return true;
            }
            
            int number = newTile.merge(currentTile);
            
            if (number > largestValue) {
              largestValue = number;
            }
            
            canMove = true;
            tiles[i][j] = null;
            break;
          }
          else {
            break;
          }
        }
      }
    }
    testWinCondition(canMove);
    return canMove;
  }
  
  /* Input: none.
   * Output: boolean that is true if it is possible for at least
   * one merge to happen in the rightward direction.
   * 
   * Moves every single tile right (if such a move is possible).
   */
  public boolean mergeRight() {
    boolean canMove = false;
    
    for (int i = 0; i < 4; i++) {
      for (int j = 2; j >= 0; j--) {
        if (tiles[i][j] == null) {
          continue;
        }
        
        int newCol = j + 1;
        
        while (newCol < 4 && newCol >= 0) {
          Tile newTile = tiles[i][newCol];
          Tile currentTile = tiles[i][j];
          
          if (newTile == null) {
            if (checkingMoves) {
              return true;
            }
            canMove = true;
            tiles[i][newCol] = currentTile;
            tiles[i][j] = null;
            j = newCol;
            newCol += 1;
          } 
          else if (newTile.canMerge(currentTile)) {
            if (checkingMoves) {
              return true;
            }
            
            int number = newTile.merge(currentTile);
            
            if (number > largestValue) {
              largestValue = number;
            }
            
            canMove = true;
            tiles[i][j] = null;
            break;
          }
          else {
            break;
          }
        }
      }
    }
    testWinCondition(canMove);
    return canMove;
  }
  
  /* Input: none.
   * Output: boolean that is true if it is possible for at least
   * one merge to happen in the leftward direction.
   * 
   * Moves every single tile left (if such a move is possible).
   */
  public boolean mergeLeft() {
    boolean canMove = false;
    
    for (int i = 0; i < 4; i++) {
      for (int j = 1; j < 4; j++) {
        if (tiles[i][j] == null) {
          continue;
        }
        
        int newCol = j - 1;
        
        while (newCol < 4 && newCol >= 0) {
          Tile newTile = tiles[i][newCol];
          Tile currentTile = tiles[i][j];
          
          if (newTile == null) {
            if (checkingMoves) {
              return true;
            }
            canMove = true;
            tiles[i][newCol] = currentTile;
            tiles[i][j] = null;
            j = newCol;
            newCol -= 1;
          } 
          else if (newTile.canMerge(currentTile)) {
            if (checkingMoves) {
              return true;
            }
            
            int number = newTile.merge(currentTile);
            
            if (number > largestValue) {
              largestValue = number;
            }
            
            canMove = true;
            tiles[i][j] = null;
            break;
          }
          else {
            break;
          }
        }
      }
    }
    testWinCondition(canMove);
    return canMove;
  }
  
  /* Input: boolean value representing ability to move/merge
   * Output: none.
   * 
   * Tests to see if the 2048 tile has been reached; if so, 
   * the game ends and the player has won.
   */
  public void testWinCondition(boolean canMove) {
    if (canMove) {
      numberOfMoves++;
      
      if (largestValue == TILE_2048) {
        gameWon = true;
      }
      else if (largestValue < TILE_2048) {
        updateMerge();
        addTile();
        updateGrid();
      }
    }
  }
  
}