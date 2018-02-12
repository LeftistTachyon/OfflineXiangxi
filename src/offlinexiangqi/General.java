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
        if(!XiangqiBoard.isValidSquare(currentPosition)) throw new IllegalArgumentException("Invalid square");
        if(!(xb.getPiece(currentPosition).getCharRepresentation().equals("G"))) throw new IllegalArgumentException("This isn\'t a general!");
        LinkedList<String> output = new LinkedList<>();
        String shift;
        if(XiangqiBoard.isValidShift(currentPosition, 0, -1)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, 0, -1);
            if(XiangqiBoard.insideFortress(shift, isRed)) {
                if(xb.isEmptySquare(shift))
                    output.add(shift);
                else if(xb.getPiece(shift).isRed ^ isRed)
                    output.add(shift);
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, 0, 1)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, 0, 1);
            if(XiangqiBoard.insideFortress(shift, isRed)) {
                if(xb.isEmptySquare(shift))
                    output.add(shift);
                else if(xb.getPiece(shift).isRed ^ isRed)
                    output.add(shift);
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, 1, 0)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, 1, 0);
            if(XiangqiBoard.insideFortress(shift, isRed)) {
                if(xb.isEmptySquare(shift))
                    output.add(shift);
                else if(xb.getPiece(shift).isRed ^ isRed)
                    output.add(shift);
            }
        }
        if(XiangqiBoard.isValidShift(currentPosition, -1, 0)) {
            shift = XiangqiBoard.shiftSquare(currentPosition, -1, 0);
            if(XiangqiBoard.insideFortress(shift, isRed)) {
                if(xb.isEmptySquare(shift))
                    output.add(shift);
                else if(xb.getPiece(shift).isRed ^ isRed)
                    output.add(shift);
            }
        }
        if(XiangqiBoard.getColumn(xb.getGeneralPos(!isRed)) == XiangqiBoard.getColumn(currentPosition)) {
            int col = XiangqiBoard.getColumn(currentPosition);
            boolean flag = true;
            for(int i = XiangqiBoard.getRow(xb.getGeneralPos(true))-1;i>XiangqiBoard.getRow(xb.getGeneralPos(false));i--) {
                if(!xb.isEmptySquare(XiangqiBoard.toSquare(col, i))) {
                    flag = false;
                    break;
                }
            }
            if(flag) output.add(xb.getGeneralPos(!isRed));
        }
        return output;
    }

    @Override
    public LinkedList<String> legalCaptures(XiangqiBoard xb, String currentPosition) {
        return allLegalMoves(xb, currentPosition);
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