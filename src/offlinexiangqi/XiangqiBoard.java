package offlinexiangqi;

/**
 * A class that represents a Xiangxi board
 * @author Jed Wang
 */
public class XiangqiBoard {
    /**
     * The board that stores the pieces.
     */
    private AbstractPiece[][] board;
    
    /**
     * Default constructor.
     */
    public XiangqiBoard() {
        board = new AbstractPiece[9][10];
    }
    
    /**
     * Determines which row a square is referring to<br>
     * <br>
     * The rows are ordered as such:<br>
     * ____<br>
     * 0 |_<br>
     * 1 |_ <br>
     * 2 |_<br>
     * 3 |_<br>
     * 4 |_<br>
     * 5 |_<br>
     * 6 |_<br>
     * 7 |_<br>
     * 8 |_<br>
     * 9 |_<br>
     * ___R
     * @param s the square
     * @return the column / file
     */
    public static int getRow(String s) {
        if(isValidSquare(s)) {
            return 10 - Integer.parseInt(s.substring(1) + "");
        } else throw new IllegalArgumentException("Invalid square");
    }
    
    /**
     * Determines which column a square is referring to<br>
     * <br>
     * The columns are ordered as such:<br>
     * |_|_|_|_|_|_|_|_|_|<br>
     * |0 1 2 3 4 5 6 7 8<br>
     * |a b c d e f g h i
     * @param s a square
     * @return which column the String is referring to
     */
    public static int getColumn(String s) {
        if(isValidSquare(s)) {
            return s.charAt(0)-'a';
        } else throw new IllegalArgumentException("Invalid square");
    }
    
    /**
     * Determines the validity of the square
     * @param s a square
     * @return whether the square is valid
     */
    public static boolean isValidSquare(String s) {
        if(s == null) return false;
        switch(s.length()) {
            case 2:
                int col = s.charAt(0)-'a', 
                        row = 10 - Integer.parseInt(s.substring(1));
                return Character.isLowerCase(s.charAt(0)) &&
                        Character.isDigit(s.charAt(1)) && isValidSquare(col, row);
            case 3:
                if(s.substring(1).equals("10")) {
                    col = s.charAt(0)-'a'; 
                    row = 0;
                    return Character.isLowerCase(s.charAt(0)) && isValidSquare(col, row);
                }
            default:
                return false;
        }
    }
    
    /**
     * Determines the validity of the square
     * @param col the column
     * @param row the row
     * @return whether the square is valid
     */
    public static boolean isValidSquare(int col, int row) {
        return col >= 0 && col <= 8 && row >= 0 && row <= 9;
    }

    /**
     * Returns the current state of the game
     * @return the current state of the game
     */
    public AbstractPiece[][] getBoard() {
        return board;
    }
    
    /**
     * DO NOT USE OFTEN <br>
     * Sets this board to a new state
     * @param board the board to set to
     */
    public void setBoard(AbstractPiece[][] board) {
        this.board = new AbstractPiece[board.length][board[0].length];
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }
}