package offlinexiangqi;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 * A class to represent any Xiangqi piece
 * @author Jed Wang
 */
public abstract class AbstractPiece {
    /**
     * Whether or not the piece is red
     */
    protected final boolean isRed;
    
    /**
     * Creates a new AbstractPiece
     * @param isRed whether or not the piece is red
     */
    public AbstractPiece(boolean isRed) {
        this.isRed = isRed;
    }
    
    /**
     * Determines whether a move is legal
     * @param xb the current state of the xiangqi game
     * @param fromWhere the current place of the piece
     * @param toWhere to where the piece would be moved
     * @return whether the move would be legal
     */
    public boolean isLegalMove(XiangqiBoard xb, String fromWhere, String toWhere) {
        return legalMoves(xb, fromWhere).contains(toWhere);
    }
    
    /**
     * Returns all of the legal moves this piece could make
     * @param xb the current state of the xiangqi game
     * @param currentPosition the current place of the piece
     * @return all legal moves
     */
    public abstract LinkedList<String> allLegalMoves(XiangqiBoard xb, String currentPosition);
    
    /**
      * Determines whether a move is legal <br>
      * However, this method does not check for checks
      * @param xb the current state of the xiangqi game
      * @param fromWhere the current place of the piece
      * @param toWhere to where the piece would be moved
      * @return whether the move would be legal without checking for checks
      */
    public boolean isAllLegalMove(XiangqiBoard xb, String fromWhere, String toWhere) {
        return allLegalMoves(xb, fromWhere).contains(toWhere);
    }
    
    /**
     * Returns all of the legal moves this piece could make, taking into account check
     * @param xb the current state of the xiangqi game
     * @param currentPosition the current place of the piece
     * @return the legal moves this piece can make
     */
    public LinkedList<String> legalMoves(XiangqiBoard xb, String currentPosition) {
        LinkedList<String> allLegal = allLegalMoves(xb, currentPosition);
        LinkedList<String> output = new LinkedList<>();
        AbstractPiece[][] initLayout = new AbstractPiece[xb.getBoard().length][xb.getBoard()[0].length];
        for(int i = 0; i < xb.getBoard().length; i++) {
            for(int j = 0; j < xb.getBoard()[i].length; j++) {
                initLayout[i][j] = xb.getBoard()[i][j];
            }
        }
        for(String square:allLegal) {
            xb.maybeMove(currentPosition, square);
            if(!xb.inCheck(isRed)) output.add(square);
            xb.setBoard(initLayout);
            if(getCharRepresentation().equals("G")) xb.resetGeneralPos(isRed);
        }
        return output;
    }
    
    /**
     * Returns all of the legal captures this piece could make
     * @param xb the current state of the xiangqi game
     * @param currentPosition the current place of the piece
     * @return all legal captures
     */
    public abstract LinkedList<String> legalCaptures(XiangqiBoard xb, String currentPosition);
    
    /**
     * The ghostifier
     */
    private static RescaleOp rop;
    
    /**
     * static init
     */
    static {
        float[] scales = { 1f, 1f, 1f, 0.3f };
        float[] offsets = new float[4];
        rop = new RescaleOp(scales, offsets, null);
    }
    
    /**
     * init
     */
    {
        try {
            black = ImageIO.read(getClass().getResource("/images/bk.gif"));
            red = ImageIO.read(getClass().getResource("/images/rk.gif"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * The images for the black and red pieces
     */
    private static BufferedImage black, red;
    
    /**
     * Draws this piece
     * @param g2D the Graphics2D to draw on
     * @param x the X coordinate of the image
     * @param y the Y coordinate of the image
     * @param width the width of the picture
     * @param height the height of the picture
     */
    public void draw(Graphics2D g2D, int x, int y, int width, int height) {
        if(isRed) {
            g2D.drawImage(red, x, y, width, height, null);
        } else {
            g2D.drawImage(black, x, y, width, height, null);
        }
    }
    
    /**
     * The images for the black and red ghosts
     */
    private static BufferedImage blackGhost, redGhost;
    
    /**
     * Turns the alpha of the image to 30%
     * @param bi the BufferedImage to change
     * @return the changed image
     */
    public static BufferedImage ghostify(BufferedImage bi) {
        return rop.filter(bi, null);
    }
    
    /**
     * Draws a ghost of this image
     * @param g2D the Graphics2D to draw on
     * @param x the X coordinate of the image
     * @param y the Y coordinate of the image
     * @param width the width of the picture
     * @param height the height of the picture
     */
    public void drawGhost(Graphics2D g2D, int x, int y, int width, int height) {
        if(isRed) {
            g2D.drawImage(redGhost, x, y, width, height, null);
        } else {
            g2D.drawImage(blackGhost, x, y, width, height, null);
        }
    }
    
    /**
     * Gets this piece's image that is red or black
     * @param isRed whether the image should be red or black
     * @return the image that represents this piece
     */
    public static BufferedImage getImage(boolean isRed) {
        return (isRed)?red:black;
    }
    
    /**
     * Returns the character that represents this piece
     * @return the character that represents this piece
     */
    public abstract String getCharRepresentation();
}