package offlinexiangqi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A class that represents a Xiangqi board
 * @author Jed Wang
 */
public class XiangqiBoard {
    /**
     * The board that stores the pieces.
     */
    private AbstractPiece[][] board;
    
    /**
     * Whether the player this board is facing is red
     */
    private boolean playerIsRed = true;
    
    /**
     * A HashMap that has the positions of the generals 
     */
    private HashMap<Boolean, String> generalPos;
    
    /**
     * Coordinates of the top left corner
     */
    private int x, y;
    
    /**
     * The last move by a piece.<br>
     * Controls drawing the last move
     */
    private String lastMoveFrom = null, lastMoveTo = null;
    
    /**
     * The piece's square to be dragging from.<br>
     * Controls dragging pieces
     */
    private String draggingFrom = null;
    
    /**
     * The piece's square to be dragging from.<br>
     * Controls playing around with pieces
     */
    private String fakeDraggingFrom = null;
    
    /**
     * The last known non-null point the mouse was at.<br>
     * Controls dragging pieces
     */
    private Point lastPoint;
    
    /**
     * From which perspective the board is to be drawn.
     */
    private boolean fromPerspective = true;
    
    /**
     * Which side this player can move pieces for.<br>
     * 0 == RED<br>
     * 1 == BLACK<br>
     * 2 == BOTH
     */
    private int manipulable = 2;
    
    /**
     * The selected square
     */
    private String selected = null;
    
    /**
     * A Map of all of the legal moves possible
     */
    private HashMap<String, LinkedList<String>> allLegalMoves;
    
    /**
     * The size of the individual xiangqi squares.
     */
    public static final int SQUARE_SIZE = 57;
    
    /**
     * The offset to the center needed for a 13-diameter circle
     */
    public static final int CENTER_OFFSET = (SQUARE_SIZE-13)/2;
    
    /**
     * The sizes of the triangles that surround a piece that can be captured
     */
    public static final int TRIANGLE_SIZE = (int) ((11.0/51)*SQUARE_SIZE);
    
    /**
     * A number which represents Red is manipulable.
     */
    public static final int WHITE_MANIPULABLE = 0;
    
    /**
     * A number which represents Black is manipulable.
     */
    public static final int BLACK_MANIPULABLE = 1;
    
    /**
     * A number which represents both sides is manipulable.
     */
    public static final int BOTH_MANIPULABLE = 2;
    
    /**
     * Default constructor.
     */
    public XiangqiBoard() {
        board = new AbstractPiece[9][10];
        generalPos = new HashMap<>();
        initPieces();
        allLegalMoves = new HashMap<>();
        x = y = 0;
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
        
        generalPos.put(true, "e1");
        generalPos.put(false, "e10");
    }
    
    /**
     * Draws the current state of the chess board
     * @param g Graphics to draw on
     */
    public void draw(Graphics g) {
        Point temp = XiangqiPanel.getMouseCoordinates();
        if(temp != null) 
            lastPoint = temp;
        
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawLines(g2D);
        drawCheck(g2D);
        drawSelection(g2D);
        drawPieces(g2D);
        drawDraggedPiece(g2D);
    }
    
    /**
     * Draws the checkered pattern
     * @param g2D Graphics2D to draw on
     */
    private void drawLines(Graphics2D g2D) {
        g2D.setColor(new Color(181, 136, 99));
        g2D.fillRect(x, y, 9*SQUARE_SIZE, 10*SQUARE_SIZE);
        g2D.setColor(new Color(240, 217, 181));
        g2D.fillRect(x + SQUARE_SIZE / 2, y + SQUARE_SIZE / 2, 
                8*SQUARE_SIZE, 9*SQUARE_SIZE);
        g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(4, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2D.drawRect(SQUARE_SIZE/2 - 5 + x, SQUARE_SIZE/2 - 5 + y, 8*SQUARE_SIZE + 10, 9*SQUARE_SIZE + 10);
        g2D.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2D.drawRect(SQUARE_SIZE/2 + x, SQUARE_SIZE/2 + y, 8*SQUARE_SIZE, 9*SQUARE_SIZE);
        g2D.drawLine(SQUARE_SIZE/2 + x, SQUARE_SIZE/2 + SQUARE_SIZE*4 + y, 
                SQUARE_SIZE/2 + SQUARE_SIZE*8 + x, SQUARE_SIZE/2 + SQUARE_SIZE*4 + y);
        g2D.drawLine(SQUARE_SIZE/2 + x, SQUARE_SIZE/2 + SQUARE_SIZE*5 + y, 
                SQUARE_SIZE/2 + SQUARE_SIZE*8 + x, SQUARE_SIZE/2 + SQUARE_SIZE*5 + y);
        g2D.setStroke(new BasicStroke(0.75f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        for(int i = 1; i < 8; i++) {
            g2D.drawLine(SQUARE_SIZE/2 + SQUARE_SIZE * i, SQUARE_SIZE/2, 
                    SQUARE_SIZE/2 + SQUARE_SIZE * i, SQUARE_SIZE/2 + SQUARE_SIZE*4);
            g2D.drawLine(SQUARE_SIZE/2 + SQUARE_SIZE * i, SQUARE_SIZE/2 + SQUARE_SIZE*5, 
                    SQUARE_SIZE/2 + SQUARE_SIZE * i, SQUARE_SIZE/2 + SQUARE_SIZE*9);
        }
        for(int i = 1; i < 4; i++) {
            g2D.drawLine(SQUARE_SIZE/2, SQUARE_SIZE/2 + SQUARE_SIZE*i, 
                    SQUARE_SIZE/2 + SQUARE_SIZE*8, SQUARE_SIZE/2 + SQUARE_SIZE*i);
        }
        for(int i = 5; i < 9; i++) {
            g2D.drawLine(SQUARE_SIZE/2, SQUARE_SIZE/2 + SQUARE_SIZE*i, 
                    SQUARE_SIZE/2 + SQUARE_SIZE*8, SQUARE_SIZE/2 + SQUARE_SIZE*i);
        }
        g2D.drawLine(SQUARE_SIZE/2 + SQUARE_SIZE*3, SQUARE_SIZE/2, SQUARE_SIZE/2 + SQUARE_SIZE*5, SQUARE_SIZE/2 + SQUARE_SIZE*2);
        g2D.drawLine(SQUARE_SIZE/2 + SQUARE_SIZE*3, SQUARE_SIZE/2 + SQUARE_SIZE*2, SQUARE_SIZE/2 + SQUARE_SIZE*5, SQUARE_SIZE/2);
        
        g2D.setColor(new Color(155, 199, 0, 105));
        if(lastMoveFrom != null) {
            if(fromPerspective) {
                g2D.fillRect(getColumn(lastMoveFrom)*SQUARE_SIZE+x, 
                        getRow(lastMoveFrom)*SQUARE_SIZE+y, 
                        SQUARE_SIZE, SQUARE_SIZE);
            } else {
                g2D.fillRect((9-getColumn(lastMoveFrom))*SQUARE_SIZE+x, 
                        (9-getRow(lastMoveFrom))*SQUARE_SIZE+y, 
                        SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        if(lastMoveTo != null) {
            if(fromPerspective) {
                g2D.fillRect(getColumn(lastMoveTo)*SQUARE_SIZE+x, 
                        getRow(lastMoveTo)*SQUARE_SIZE+y, 
                        SQUARE_SIZE, SQUARE_SIZE);
            } else {
                g2D.fillRect((9-getColumn(lastMoveTo))*SQUARE_SIZE+x, 
                        (9-getRow(lastMoveTo))*SQUARE_SIZE+y, 
                        SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    
    /**
     * Draws the pieces on the board.
     * @param g2D Graphics2D to draw on
     */
    private void drawPieces(Graphics2D g2D) {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] != null) {
                    if (toSquare(i, j).equals(draggingFrom) || toSquare(i, j).equals(fakeDraggingFrom)) {
                        if(fromPerspective) {
                            board[i][j].drawGhost(g2D, (i * SQUARE_SIZE) + x, 
                                    (j * SQUARE_SIZE) + y, SQUARE_SIZE, SQUARE_SIZE);
                        } else {
                            board[i][j].drawGhost(g2D, ((9-i) * SQUARE_SIZE) + x, 
                                    ((9-j) * SQUARE_SIZE)+ y, SQUARE_SIZE, SQUARE_SIZE);
                        }
                    } else {
                        if(fromPerspective) {
                            board[i][j].draw(g2D, (i * SQUARE_SIZE) + x, 
                                    (j * SQUARE_SIZE) + y, SQUARE_SIZE, SQUARE_SIZE);
                        } else {
                            board[i][j].draw(g2D, ((9-i) * SQUARE_SIZE) + x, 
                                    ((9-j) * SQUARE_SIZE) + y, SQUARE_SIZE, SQUARE_SIZE);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Draws the selected pieces
     * @param g2D Graphics2D to draw on
     */
    private void drawSelection(Graphics2D g2D) {
        String selection;
        if(draggingFrom == null) {
            if(selected == null) {
                return;
            } else selection = selected;
        } else selection = draggingFrom;
        LinkedList<String> moves = allLegalMoves.get(selection);
        if(moves == null) return;
        Color moveDest = new Color(20, 85, 30, 77);
        g2D.setColor(moveDest);
        final Point p = XiangqiPanel.getMouseCoordinates();
        //System.out.println((p == null)?"null":"(" + p.x + ", " + p.y + ")");
        for(String s:moves) {
            int x1 = XiangqiBoard.getColumn(s), 
                    y1 = XiangqiBoard.getRow(s);
            int x2 = x1, y2 = y1;
            if(!fromPerspective) {
                x2 = 7 - x1;
                y2 = 7 - y1;
            }
            
            if(p != null) {
                if(isEmptySquare(x1, y1) && 
                        (p.x >= x+x2*SQUARE_SIZE && p.x <= x+x2*SQUARE_SIZE+SQUARE_SIZE) && 
                        (p.y >= y+y2*SQUARE_SIZE && p.y <= y+y2*SQUARE_SIZE+SQUARE_SIZE)) {
                    g2D.fillRoundRect(x+x2*SQUARE_SIZE, 
                            y+y2*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, 16, 16);
                    continue;
                }
            }
            if(isEmptySquare(x1, y1)) {
                g2D.fillOval(x+x2*SQUARE_SIZE+CENTER_OFFSET, y+y2*SQUARE_SIZE+CENTER_OFFSET, 14, 14);
            } else {
                /*
                1___2
                 | |
                4---3
                */
                Point one = new Point(x+x2*SQUARE_SIZE, y+y2*SQUARE_SIZE), 
                        two = new Point(x+x2*SQUARE_SIZE + SQUARE_SIZE, y+y2*SQUARE_SIZE), 
                        three = new Point(x+x2*SQUARE_SIZE + SQUARE_SIZE, y+y2*SQUARE_SIZE + SQUARE_SIZE), 
                        four = new Point(x+x2*SQUARE_SIZE, y+y2*SQUARE_SIZE + SQUARE_SIZE);
                
                g2D.fillPolygon(new int[]{one.x, one.x, one.x+TRIANGLE_SIZE}, 
                        new int[]{one.y, one.y+TRIANGLE_SIZE, one.y}, 3); // 1
                g2D.fillPolygon(new int[]{two.x, two.x, two.x-TRIANGLE_SIZE}, 
                        new int[]{two.y, two.y+TRIANGLE_SIZE, two.y}, 3); // 2
                g2D.fillPolygon(new int[]{three.x, three.x, three.x-TRIANGLE_SIZE}, 
                        new int[]{three.y, three.y-TRIANGLE_SIZE, three.y}, 3); // 3
                g2D.fillPolygon(new int[]{four.x, four.x, four.x+TRIANGLE_SIZE}, 
                        new int[]{four.y, four.y-TRIANGLE_SIZE, four.y}, 3); // 4
            }
        }
        Color selectionColor = new Color(20, 85, 30, 128);
        g2D.setColor(selectionColor);
        if(fromPerspective) {
            g2D.fillRoundRect(x+XiangqiBoard.getColumn(selection)*SQUARE_SIZE, 
                    y+XiangqiBoard.getRow(selection)*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, 16, 16);
        } else {
            g2D.fillRoundRect(x+(9-XiangqiBoard.getColumn(selection))*SQUARE_SIZE, 
                    y+(9-XiangqiBoard.getRow(selection))*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, 16, 16);
        }
    }
    
    /**
     * Draws whether either king is in check
     * @param g2D the Graphics2D to draw on
     */
    private void drawCheck(Graphics2D g2D) {
        float[] fractions = new float[]{ 0.0f, 0.25f, 0.89f, 1.0f };
        Color[] colors = new Color[]{
            new Color(255, 0, 0, 255), new Color(231, 0, 0, 255), 
            new Color(169, 0, 0, 0), new Color(158, 0, 0, 0)
        };
        if(inCheck(playerIsRed)) {
            String generalAt = generalPos.get(playerIsRed);
            int col = getColumn(generalAt), row = getRow(generalAt);
            if(!fromPerspective) {
                col = 7 - col;
                row = 7 - row;
            }
            g2D.setPaint(
                    new RadialGradientPaint(
                            (SQUARE_SIZE/2) + (col*SQUARE_SIZE) + x, 
                            (SQUARE_SIZE/2) + (row*SQUARE_SIZE) + y, 
                            SQUARE_SIZE*3/4, fractions, colors
                    )
            );
            g2D.fillRoundRect(col*SQUARE_SIZE + x, row*SQUARE_SIZE + y, 
                    SQUARE_SIZE, SQUARE_SIZE, 16, 16);
        }
    }
    
    /**
     * Draws the dragged piece
     * @param g2D the Graphics2D to draw on
     */
    private void drawDraggedPiece(Graphics2D g2D) {
        if(fakeDraggingFrom != null) {
            int midX = lastPoint.x - (SQUARE_SIZE/2), 
                midY = lastPoint.y - (SQUARE_SIZE/2);
            getPiece(fakeDraggingFrom).draw(g2D, midX, midY, SQUARE_SIZE, SQUARE_SIZE);
        }
        if(draggingFrom != null) {
            int midX = lastPoint.x - (SQUARE_SIZE/2), 
                midY = lastPoint.y - (SQUARE_SIZE/2);
            getPiece(draggingFrom).draw(g2D, midX, midY, SQUARE_SIZE, SQUARE_SIZE);
        }
    }
    
    /**
     * Constructor from a previous XiangqiBoard
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
     * Recalculates all of the moves on a square
     */
    public void recalculateMoves() {
        allLegalMoves = new HashMap<>();
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] == null) continue;
                if(board[i][j].isRed == playerIsRed) {
                    String current = XiangqiBoard.toSquare(i, j);
                    LinkedList<String> moves = board[i][j].legalMoves(this, current);
                    allLegalMoves.put(current, moves);
                }
            }
        }
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
     * Determines whether a square is empty
     * @param square a square
     * @return whether that square is empty
     */
    public boolean isEmptySquare(String square) {
        return getPiece(square) == null;
    }
    
    /**
     * Determines whether a space represented by ABSOLUTE coordinates is empty
     * @param col the ABSOLUTE column
     * @param row the ABSOLUTE row
     * @return whether that square is empty
     */
    public boolean isEmptySquare(int col, int row) {
        return getPiece(col, row) == null;
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
     * @param isRed whether the side to check is red (PUN INTENDED)
     * @return whether the side is in check
     */
    public boolean inCheck(boolean isRed) {
        for(int i = 0;i<8;i++) {
            for(int j = 0;j<8;j++) {
                AbstractPiece ap = getPiece(i, j);//lit dude lit
                if(ap != null) {
                    if(ap.isRed ^ isRed) {
                        //if(ap.legalCaptures(this, XiangqiBoard.toSquare(i, j)).contains(generalPos))
                        // if the current opposite-colored piece can eat the king on the next move
                        if(ap.isAllLegalMove(this, XiangqiBoard.toSquare(i, j), generalPos.get(isRed))) {
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
        maybeMove(fromWhereX, fromWhereY, toWhereX, toWhereY);
        System.out.println("Moved: " + playerIsRed);
        playerIsRed = !playerIsRed;
        recalculateMoves();
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
     * Determines whether a square is inside a colored fortress
     * @param square the square to check
     * @param isRed whether the fortress is red or not
     * @return whether the square is inside a fortress
     */
    public static boolean insideFortress(String square, boolean isRed) {
        if(isValidSquare(square)) {
            if(XiangqiBoard.getColumn(square) < 3 || XiangqiBoard.getColumn(square) > 5) 
                return false;
            if(isRed) {
                return XiangqiBoard.getRow(square) >= 7 && XiangqiBoard.getRow(square) <= 9;
            } else {
                return XiangqiBoard.getRow(square) >= 0 && XiangqiBoard.getRow(square) <= 2;
            }
        } else throw new IllegalArgumentException("Invalid square");
    }
    
    /**
     * Determines whether a piece is behind the river
     * @param square the square the piece is on
     * @param isRed whether the piece is red or black
     * @return whether the piece is behind the river
     */
    public static boolean behindRiver(String square, boolean isRed) {
        if(isValidSquare(square)) {
            if(isRed) {
                return getRow(square) >= 5;
            } else {
                return getRow(square) <= 4;
            }
        } else throw new IllegalArgumentException("Invalid square");
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
     * Determines where the General for a certain color is
     * @param isRed which General to find
     * @return a square
     */
    public String getGeneralPos(boolean isRed) {
        return generalPos.get(isRed);
    }
    
    /**
     * Notifies this that the board has been clicked on a square
     * @param square where the board has been clicked
     */
    public void clicked(String square) {
        if(selected == null) {
            if(!isEmptySquare(square) && (getPiece(square).isRed == playerIsRed) && 
                    ((playerIsRed && manipulable == 0) || (!playerIsRed && manipulable == 1) || manipulable == 2)) {
                selected = square;
            }
        } else if(selected.equals(square)) {
            selected = null;
        } else {
            if(!isEmptySquare(square)) {
                if(getPiece(selected).isLegalMove(this, selected, square)) {
                    movePiece(selected, square);
                    selected = null;
                } else {
                    if(getPiece(square).isRed == playerIsRed) {
                        selected = square;
                    } else {
                        selected = null;
                    }
                }
            } else {
                if(getPiece(selected).isLegalMove(this, selected, square)) {
                    movePiece(selected, square);
                    selected = null;
                } else selected = null;
            }
        }
        System.out.println("selected: " + selected);
    }
    
    /**
     * Enables dragging.
     * @param fromWhere from where the piece is being dragged 
     */
    public void enableDragging(String fromWhere) {
        if(!isEmptySquare(fromWhere)) 
            if(getPiece(fromWhere).isRed == playerIsRed && 
                    ((playerIsRed && manipulable == 0) || 
                    (!playerIsRed && manipulable == 1) || manipulable == 2)) 
                draggingFrom = fromWhere;
            else 
                fakeDraggingFrom = fromWhere;
        System.out.println("selected: " + selected);
        System.out.println("draggingFrom: " + draggingFrom);
    }
    
    /**
     * Disables dragging.
     * @param toWhere to where the piece is being dragged 
     */
    public void disableDragging(String toWhere) {
        if(fakeDraggingFrom != null) {
            fakeDraggingFrom = null;
            return;
        }
        if(draggingFrom == null) return;
        System.out.println("(" + lastPoint.x + ", " + lastPoint.y + ")");
        System.out.println(draggingFrom + " -> " + toWhere);
        /*if(getPiece(draggingFrom).isLegalMove(this, draggingFrom, dropSquare)) {
            movePiece(draggingFrom, dropSquare);
        }*/
        if(getPiece(draggingFrom).isLegalMove(this, draggingFrom, toWhere)) {
            movePiece(draggingFrom, toWhere);
        }
        if(!draggingFrom.equals(selected)) selected = null;
        draggingFrom = null;
    }
    
    /**
     * Determines the square being referenced from a position
     * @param xPos the x-position of the mouse
     * @param yPos the y-position of the mouse
     * @return a square
     */
    public String toSquareFromPos(int xPos, int yPos) {
        int x1 = (xPos - x)/SQUARE_SIZE, y1 = (yPos - y)/SQUARE_SIZE;
        return toPerspectiveSquare(x1, y1);
    }
    
    /**
     * Determines the square being referenced in perspective
     * @param x the x position of the square
     * @param y the y position of the square
     * @return the square being referenced in perspective
     */
    public String toPerspectiveSquare(int x, int y) {
        String output = (fromPerspective) ? toSquare(x, y) : rotateSquare180(x, y);
        return (isValidSquare(output))? output : null;
    }
    
    /**
     * Rotates a square 180 degrees.
     * @param s the square to rotate
     * @return the resulting square
     */
    public String rotateSquare180(String s) {
        return rotateSquare180(getColumn(s), getRow(s));
    }
    
    /**
     * Rotates a square 180 degrees.
     * @param x the x position of the square to rotate
     * @param y the y position of the square to rotate
     * @return the resulting square
     */
    public String rotateSquare180(int x, int y) {
        String output = toSquare(7-x, 7-y);
        return (isValidSquare(output))? output : null;
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