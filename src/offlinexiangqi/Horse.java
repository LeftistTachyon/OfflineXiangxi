package offlinexiangqi;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 * A class that represents a Horse
 * @author Jed Wang
 */
public class Horse extends AbstractPiece {

    /**
     * A constructor that creates a new Horse
     * @param isRed whether this Horse is red
     */
    public Horse(boolean isRed) {
        super(isRed);
    }

    @Override
    public LinkedList<String> allLegalMoves(XiangqiBoard xb, String currentPosition) {
        if(!XiangqiBoard.isValidSquare(currentPosition)) throw new IllegalArgumentException("Invalid square");
        if(!(xb.getPiece(currentPosition).getCharRepresentation().equals("H"))) throw new IllegalArgumentException("This isn\'t a horse!");
        LinkedList<String> output = new LinkedList<>();
        String shift;
        if(XiangqiBoard.isValidShift(currentPosition, 1, 0)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, 1, 0);
            if(xb.isEmptySquare(shift)) {
                if(XiangqiBoard.isValidShift(currentPosition, 2, -1)) {
                    output.add(XiangqiBoard.shiftSquare(currentPosition, 2, -1));
                }
                if(XiangqiBoard.isValidShift(currentPosition, 2, 1)) {
                    output.add(XiangqiBoard.shiftSquare(currentPosition, 2, 1));
                }
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, -1, 0)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, -1, 0);
            if(xb.isEmptySquare(shift)) {
                if(XiangqiBoard.isValidShift(currentPosition, -2, -1)) {
                    output.add(XiangqiBoard.shiftSquare(currentPosition, -2, -1));
                }
                if(XiangqiBoard.isValidShift(currentPosition, -2, 1)) {
                    output.add(XiangqiBoard.shiftSquare(currentPosition, -2, 1));
                }
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, 0, 1)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, 0, 1);
            if(xb.isEmptySquare(shift)) {
                if(XiangqiBoard.isValidShift(currentPosition, -1, 2)) {
                    output.add(XiangqiBoard.shiftSquare(currentPosition, -1, 2));
                }
                if(XiangqiBoard.isValidShift(currentPosition, 1, 2)) {
                    output.add(XiangqiBoard.shiftSquare(currentPosition, 1, 2));
                }
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, 0, -1)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, 0, -1);
            if(xb.isEmptySquare(shift)) {
                if(XiangqiBoard.isValidShift(currentPosition, 1, -2)) {
                    output.add(XiangqiBoard.shiftSquare(currentPosition, 1, -2));
                }
                if(XiangqiBoard.isValidShift(currentPosition, -1, -2)) {
                    output.add(XiangqiBoard.shiftSquare(currentPosition, -1, -2));
                }
            }
        }
        return output;
    }

    @Override
    public LinkedList<String> legalCaptures(XiangqiBoard xb, String currentPosition) {
        return allLegalMoves(xb, currentPosition);
    }

    @Override
    public String getCharRepresentation() {
        return "H";
    }
    
    /**
     * init
     */
    {
        try {
            black = ImageIO.read(getClass().getResource("/images/bn.gif"));
            red = ImageIO.read(getClass().getResource("/images/rn.gif"));
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