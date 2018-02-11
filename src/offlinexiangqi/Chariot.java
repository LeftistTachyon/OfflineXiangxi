package offlinexiangqi;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 * A class that represents a Chariot
 * @author Jed Wang
 */
public class Chariot extends AbstractPiece {

    /**
     * A constructor that creates a new Chariot
     * @param isRed whether this Chariot is red
     */
    public Chariot(boolean isRed) {
        super(isRed);
    }

    @Override
    public LinkedList<String> allLegalMoves(XiangqiBoard xb, String currentPosition) {
        if(!XiangqiBoard.isValidSquare(currentPosition)) throw new IllegalArgumentException("Invalid square");
        if(!(xb.getPiece(currentPosition).getCharRepresentation().equals("R"))) throw new IllegalArgumentException("This isn\'t a chariot!");
        LinkedList<String> output = new LinkedList<>();
        String temp;
        if(XiangqiBoard.isValidShift(currentPosition, 1, 0)) {
            temp = XiangqiBoard.shiftSquare(currentPosition, 1, 0);
            while(xb.isEmptySquare(temp)) {
                output.add(temp);
                try {
                    temp = XiangqiBoard.shiftSquare(temp, 1, 0);
                } catch(IllegalArgumentException iae) {
                    break;
                }
            }
            if(XiangqiBoard.isValidSquare(temp) && !xb.isEmptySquare(temp)) {
                if(xb.getPiece(temp).isRed ^ isRed) {
                    output.add(temp);
                }
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, -1, 0)) {
            temp = XiangqiBoard.shiftSquare(currentPosition, -1, 0);
            while(xb.isEmptySquare(temp)) {
                output.add(temp);
                try {
                    temp = XiangqiBoard.shiftSquare(temp, -1, 0);
                } catch(IllegalArgumentException iae) {
                    break;
                }
            }
            if(XiangqiBoard.isValidSquare(temp) && !xb.isEmptySquare(temp)) {
                if(xb.getPiece(temp).isRed ^ isRed) {
                    output.add(temp);
                }
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, 0, 1)) {
            temp = XiangqiBoard.shiftSquare(currentPosition, 0, 1);
            while(xb.isEmptySquare(temp)) {
                output.add(temp);
                try {
                    temp = XiangqiBoard.shiftSquare(temp, 0, 1);
                } catch(IllegalArgumentException iae) {
                    break;
                }
            }
            if(XiangqiBoard.isValidSquare(temp) && !xb.isEmptySquare(temp)) {
                if(xb.getPiece(temp).isRed ^ isRed) {
                    output.add(temp);
                }
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, 0, -1)) {
            temp = XiangqiBoard.shiftSquare(currentPosition, 0, -1);
            while(xb.isEmptySquare(temp)) {
                output.add(temp);
                try {
                    temp = XiangqiBoard.shiftSquare(temp, 0, -1);
                } catch(IllegalArgumentException iae) {
                    break;
                }
            }
            if(XiangqiBoard.isValidSquare(temp) && !xb.isEmptySquare(temp)) {
                if(xb.getPiece(temp).isRed ^ isRed) {
                    output.add(temp);
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
        return "R";
    }
    
    /**
     * init
     */
    {
        try {
            black = ImageIO.read(getClass().getResource("/images/br.gif"));
            red = ImageIO.read(getClass().getResource("/images/rr.gif"));
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