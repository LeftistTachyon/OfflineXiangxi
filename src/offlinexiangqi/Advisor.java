package offlinexiangqi;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 * A class that represents an Advisor
 * @author Jed Wang
 */
public class Advisor extends AbstractPiece {

    /**
     * A constructor that creates a new Advisor
     * @param isRed whether the Advisor is red
     */
    public Advisor(boolean isRed) {
        super(isRed);
    }

    @Override
    public LinkedList<String> allLegalMoves(XiangqiBoard xb, String currentPosition) {
        if(!XiangqiBoard.isValidSquare(currentPosition)) throw new IllegalArgumentException("Invalid square");
        if(!(xb.getPiece(currentPosition).getCharRepresentation().equals("A"))) throw new IllegalArgumentException("This isn\'t an advisor!");
        LinkedList<String> output = new LinkedList<>();
        String shift;
        if(XiangqiBoard.isValidShift(currentPosition, -1, -1)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, -1, -1);
            if(XiangqiBoard.insideFortress(shift, isRed)) {
                if(xb.isEmptySquare(shift))
                    output.add(shift);
                else if(xb.getPiece(shift).isRed ^ isRed)
                    output.add(shift);
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, 1, 1)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, 1, 1);
            if(XiangqiBoard.insideFortress(shift, isRed)) {
                if(xb.isEmptySquare(shift))
                    output.add(shift);
                else if(xb.getPiece(shift).isRed ^ isRed)
                    output.add(shift);
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, 1, -1)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, 1, -1);
            if(XiangqiBoard.insideFortress(shift, isRed)) {
                if(xb.isEmptySquare(shift))
                    output.add(shift);
                else if(xb.getPiece(shift).isRed ^ isRed)
                    output.add(shift);
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, -1, 1)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, -1, 1);
            if(XiangqiBoard.insideFortress(shift, isRed)) {
                if(xb.isEmptySquare(shift))
                    output.add(shift);
                else if(xb.getPiece(shift).isRed ^ isRed)
                    output.add(shift);
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
        return "A";
    }
    
    /**
     * init
     */
    {
        try {
            black = ImageIO.read(getClass().getResource("/images/ba.gif"));
            red = ImageIO.read(getClass().getResource("/images/ra.gif"));
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