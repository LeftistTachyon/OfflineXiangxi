package offlinexiangqi;

import java.util.HashMap;

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
     * Whether the player this board is facing is white
     */
    private boolean playerIsRed = true;
    
    /**
     * A HashMap that has the positions of the generals 
     */
    private HashMap<Boolean, String> generalPos;
    
    /**
     * Default constructor.
     */
    public XiangqiBoard() {
        board = new AbstractPiece[9][10];
        initPieces();
        generalPos = new HashMap<>();
    }
    
    /**
     * Adds the starting pieces to a chessboard.
     */
    private void initPieces() {
        board[0][0] = new Chariot(false);
        board[1][0] = new Horse(false);
        board[2][0] = new Elephant(false);
        board[3][0] = new Advisor(false);
        board[4][0] = new General(false);
        board[5][0] = new Advisor(false);
        board[6][0] = new Elephant(false);
        board[7][0] = new Horse(false);
        board[8][0] = new Chariot(false);
        
        board[1][2] = new Cannon(false);
        board[7][2] = new Cannon(false);
        
        board[0][3] = new Pawn(false);
        board[2][3] = new Pawn(false);
        board[4][3] = new Pawn(false);
        board[6][3] = new Pawn(false);
        board[8][3] = new Pawn(false);
        
        board[0][9] = new Chariot(true);
        board[1][9] = new Horse(true);
        board[2][9] = new Elephant(true);
        board[3][9] = new Advisor(true);
        board[4][9] = new General(true);
        board[5][9] = new Advisor(true);
        board[6][9] = new Elephant(true);
        board[7][9] = new Horse(true);
        board[8][9] = new Chariot(true);
        
        board[1][7] = new Cannon(true);
        board[7][7] = new Cannon(true);
        
        board[0][6] = new Pawn(true);
        board[2][6] = new Pawn(true);
        board[4][6] = new Pawn(true);
        board[6][6] = new Pawn(true);
        board[8][6] = new Pawn(true);
    }
    
    /**
     * Constructor from a previous ChessBoard
     * @param xb the XiangqiBoard to duplicate
     */
    public XiangqiBoard(XiangqiBoard xb) {
        this();
        for(int i = 0; i < xb.board.length; i++) {
            System.arraycopy(xb.board[i], 0, board[i], 0, xb.board[i].length);
        }
    }
    
    /**
     * Determines the square represented by the row and column
     * @param column the column
     * @param row the row
     * @return the square that is represented by the row and column
     */
    public static String toSquare(int column, int row) {
        return "" + (char)('a' + column) + (10 - row);
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
     * Determines where a square is after a shift (a.k.a. moving it left and right, up and down)
     * @param col current column
     * @param row current row
     * @param colShift how much to shift the columns
     * @param rowShift how much to shift the rows
     * @return the shifted square
     */
    public static String shiftSquare(int col, int row, int colShift, int rowShift) {
        if(isValidSquare(col, row)) {
            int shiftedCol = col + colShift, shiftedRow = row + rowShift;
            if(isValidSquare(shiftedCol, shiftedRow)) {
                return toSquare(shiftedCol, shiftedRow);
            } else throw new IllegalArgumentException("Invalid shift");
        } else throw new IllegalArgumentException("Invalid square");
    }
    
    /**
     * Determines where a square is after a shift (a.k.a. moving it left and right, up and down)
     * @param s the current square
     * @param colShift how much to shift the columns
     * @param rowShift how much to shift the rows
     * @return the shifted square
     */
    public static String shiftSquare(String s, int colShift, int rowShift) {
        if(isValidSquare(s)) {
            int col = getColumn(s), row = getRow(s);
            int shiftedCol = col + colShift, shiftedRow = row + rowShift;
            if(isValidSquare(shiftedCol, shiftedRow)) {
                return toSquare(shiftedCol, shiftedRow);
            } else throw new IllegalArgumentException("Invalid shift");
        } else throw new IllegalArgumentException("Invalid square");
    }
    
    /**
     * Checks if a shift is valid
     * @param col current column
     * @param row current row
     * @param colShift how much to shift the columns
     * @param rowShift how much to shift the rows
     * @return whether the shift is valid
     */
    public static boolean isValidShift(int col, int row, int colShift, int rowShift) {
        if(isValidSquare(col, row)) {
            int shiftedCol = col + colShift, shiftedRow = row + rowShift;
            return isValidSquare(shiftedCol, shiftedRow);
        } else return false;
    }
    
    /**
     * Checks if this shift is valid
     * @param s current square
     * @param colShift how much to shift the columns
     * @param rowShift how much to shift the rows
     * @return whether the shift is valid
     */
    public static boolean isValidShift(String s, int colShift, int rowShift) {
        return isValidShift(
                XiangqiBoard.getColumn(s), XiangqiBoard.getRow(s), 
                colShift, rowShift
        );
    }
    
    
    /**
     * Determines which piece occupies a square
     * @param square a square
     * @return the piece on that square, and if none, null
     */
    public AbstractPiece getPiece(String square) {
        if(isValidSquare(square)) {
            return board[getColumn(square)][getRow(square)];
        } else throw new IllegalArgumentException("Invalid square");
    }
    
    /**
     * Determines which piece occupies a space represented by coordinates<br>
     * i.e. (0, 0) represents the top left corner
     * @param col the column
     * @param row the row
     * @return the piece on that square, and if none, null
     */
    public AbstractPiece getPiece(int col, int row) {
        if(isValidSquare(col, row)) {
            return board[col][row];
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
     * Determines whether one side's general is in check
     * @param isWhite whether the side to check is white (PUN INTENDED)
     * @return whether the side is in check
     */
    public boolean inCheck(boolean isWhite) {
        for(int i = 0;i<8;i++) {
            for(int j = 0;j<8;j++) {
                AbstractPiece ap = getPiece(i, j);//lit dude lit
                if(ap != null) {
                    if(ap.isRed ^ isWhite) {
                        //if(ap.legalCaptures(this, XiangqiBoard.toSquare(i, j)).contains(generalPos))
                        // if the current opposite-colored piece can eat the king on the next move
                        if(ap.isAllLegalMove(this, XiangqiBoard.toSquare(i, j), generalPos.get(isWhite))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Moves a piece from fromWhere to toWhere
     * @param fromWhere from where a piece is moved
     * @param toWhere where to move a piece
     */
    public void movePiece(String fromWhere, String toWhere) {
        movePiece(
                XiangqiBoard.getColumn(fromWhere), 
                XiangqiBoard.getRow(fromWhere), 
                XiangqiBoard.getColumn(toWhere), 
                XiangqiBoard.getRow(toWhere)
        );
    }
    
    /**
     * Moves a piece from fromWhere(X, Y) to toWhere(X, Y)
     * @param fromWhereX from where a piece is moved
     * @param fromWhereY from where a piece is moved
     * @param toWhereX where to move a piece
     * @param toWhereY where to move a piece
     */
    public void movePiece(int fromWhereX, int fromWhereY, int toWhereX, int toWhereY) {
        XiangqiBoard thisCopy = new XiangqiBoard(this);
        maybeMove(fromWhereX, fromWhereY, toWhereX, toWhereY);
        System.out.println("Moved: " + playerIsRed);
        playerIsRed = !playerIsRed;
        //recalculateMoves();
        //updatePos(miniFEN());
        /*if(checkMated(playerIsRed)) System.out.println("Checkmate!\n");
        else if(inCheck(playerIsRed)) {
            System.out.println("Check!\n");
        } else if(isDraw(playerIsRed)) System.out.println("Draw.\n");*/
    }
    
    /**
     * Used to check whether this move is legal
     * @param fromWhere from where to move a piece
     * @param toWhere to where to move a piece
     */
    public void maybeMove(String fromWhere, String toWhere) {
        maybeMove(
                getColumn(fromWhere), getRow(fromWhere), 
                getColumn(toWhere), getRow(toWhere)
        );
    }
    
    /**
     * Used to check whether this move is legal
     * @param fromWhereX from which column to move a piece
     * @param fromWhereY from which row to move a piece
     * @param toWhereX to which column to move a piece
     * @param toWhereY to which row to move a piece
     */
    public void maybeMove(int fromWhereX, int fromWhereY, int toWhereX, int toWhereY) {
        board[toWhereX][toWhereY] = board[fromWhereX][fromWhereY];
        board[fromWhereX][fromWhereY] = null;
    }
    
    /**
     * Refinds both kings.
     */
    public void resetGeneralPos() {
        String bGeneral = null, rGeneral = null;
        OUTER: for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] == null) continue;
                if(board[i][j].getCharRepresentation().equals("G")) {
                    if(board[i][j].isRed) {
                        if(rGeneral == null) {
                            rGeneral = toSquare(i, j);
                        } else {
                            assert false : "There are two red generals?!";
                        }
                    } else {
                        if(bGeneral == null) {
                            bGeneral = toSquare(i, j);
                        } else {
                            assert false : "There are two black generals?!";
                        }
                    }
                    if(rGeneral != null && bGeneral != null) break OUTER;
                }
            }
        }
        if(rGeneral == null) assert false : "Cannot find red general";
        if(bGeneral == null) assert false : "Cannot find black general";
        generalPos.put(true, rGeneral);
        generalPos.put(false, bGeneral);
    }
    
    /**
     * Refinds only one king.
     * @param isRed whether the king to find again is red
     */
    public void resetGeneralPos(boolean isRed) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] == null) continue;
                if(board[i][j].getCharRepresentation().equals("G") && (board[i][j].isRed == isRed)) {
                    generalPos.put(isRed, toSquare(i, j));
                    return;
                }
            }
        }
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
    
    /**
     * Prints the current state of the chess board.
     */
    public void printBoard() {
        for(int i = 0;i<board[0].length;i++) {
            for(int j = 0;j<board.length;j++) {
                AbstractPiece ap = board[j][i];
                if(ap == null) {
                    System.out.print(" ");
                } else if(ap.isRed) {
                    System.out.print(ap.getCharRepresentation());
                } else {
                    System.out.print(ap.getCharRepresentation().toLowerCase());
                }
            }
            System.out.println();
        }
    }
}