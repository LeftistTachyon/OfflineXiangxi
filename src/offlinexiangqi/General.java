package offlinexiangqi;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 * A class that represents a General
 * @author Jed Wang
 */
public class General extends AbstractPiece {
    /**
     * A constructor that creates a new General.
     * @param isRed whether the General is red
     */
    public General(boolean isRed) {
        super(isRed);
    }
    
    @Override
    public LinkedList<String> allLegalMoves(XiangqiBoard xb, String currentPosition) {
        LinkedList<String> output = new LinkedList<>();
        String shift;
        if(XiangqiBoard.isValidShift(currentPosition, 0, -1)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, 0, -1);
            if(XiangqiBoard.getColumn(shift) >= 3 && XiangqiBoard.getColumn(shift) <= 5) {
                if((isRed && XiangqiBoard.getRow(shift) >= 7 && XiangqiBoard.getRow(shift) <= 9) || 
                        (!isRed && XiangqiBoard.getRow(shift) >= 0 && XiangqiBoard.getRow(shift) <= 2)) {
                    output.add(shift);
                }
            }
        }
        return output;
    }
    
    /**
     * Determines whether a square is inside a colored fortress
     * @param square the square to check
     * @param isRed whether the fortress is red or not
     * @return whether the square is inside a fortress
     */
    public static boolean insideFortress(String square, boolean isRed) {
        if(XiangqiBoard.getColumn(square) < 3 || XiangqiBoard.getColumn(square) > 5) 
            return false;
        if(isRed) {
            return XiangqiBoard.getRow(square) >= 7 && XiangqiBoard.getRow(square) <= 9;
        } else {
            return XiangqiBoard.getRow(square) >= 0 && XiangqiBoard.getRow(square) <= 2;
        }
    }

    @Override
    public LinkedList<String> legalCaptures(XiangqiBoard xb, String currentPosition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCharRepresentation() {
        return "G";
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
    @Override
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
     * Draws a ghost of this image
     * @param g2D the Graphics2D to draw on
     * @param x the X coordinate of the image
     * @param y the Y coordinate of the image
     * @param width the width of the picture
     * @param height the height of the picture
     */
    @Override
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
}