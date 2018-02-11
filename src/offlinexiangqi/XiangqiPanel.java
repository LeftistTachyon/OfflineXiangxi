package offlinexiangqi;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class XiangqiPanel extends JPanel {
    /**
     * The chessboard
     */
    private XiangqiBoard xb;
    
    /**
     * The mouse listener
     */
    private XiangqiMouseListener cml;
    
    /**
     * A reference to the most recent XiangqiPanel created
     */
    private static XiangqiPanel _this;
    
    /**
     * When to stop the game
     */
    private volatile boolean stop = false;
    
    /**
     * Default constructor
     */
    public XiangqiPanel() {
        cml = new XiangqiMouseListener(this);
        _this = this;
        xb = new XiangqiBoard();
        xb.recalculateMoves();
        addMouseListener(cml);
        super.setVisible(true);
    }

    /**
     * Updates the current rendering
     * @param g Graphics to draw on
     */
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Renders the image
     * @param g Graphics to draw on
     */
    @Override
    public void paint(Graphics g) {
        drawBackground((Graphics2D) g, 
                new GradientPaint(0, 0, new Color(215, 215, 215), 0, 
                        getHeight(), new Color(238, 238, 238))
        );
        xb.draw(g);
    }
    
    /**
     * Paints the background a solid color
     * @param g the Graphics to draw on
     * @param c The color to draw the background
     */
    private void drawBackground(Graphics g, Color c) {
        g.setColor(c);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    /**
     * Paints the background a solid color
     * @param g2D the Graphics2D to draw on
     * @param p The paint to paint the background
     */
    private void drawBackground(Graphics2D g2D, Paint p) {
        g2D.setPaint(p);
        g2D.fillRect(0, 0, getWidth(), getHeight());
    }
    
    /**
     * Notifies this of a MouseEvent
     * @param me the MouseEvent
     * @param i what fired this event (See: <code>XiangqiMouseListener.MOUSE_?</code>)
     */
    public void notify(MouseEvent me, int i) {
        String selected = xb.toSquareFromPos(me.getX(), me.getY());
        switch(i) {
            case XiangqiMouseListener.MOUSE_CLICKED:
                if(XiangqiBoard.isValidSquare(selected)) xb.clicked(selected);
                break;
            case XiangqiMouseListener.MOUSE_PRESSED:
                if(XiangqiBoard.isValidSquare(selected)) xb.enableDragging(selected);
                break;
            case XiangqiMouseListener.MOUSE_RELEASED:
                if(XiangqiBoard.isValidSquare(selected)) xb.disableDragging(selected);
                break;
        }
        repaint();
    }
    
    /**
     * Determines where the mouse currently is
     * @return A point representing the mouse's position
     */
    public static Point getMouseCoordinates() {
        return _this.getMousePosition();
    }
    
    /**
     * A method that starts the redrawing of the chess board constantly
     */
    public void start() {
        new Thread() {
            @Override
            public void run() {
                while(!stop) {
                    repaint();
                    /*try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }*/
                }
                System.out.println("Thread stopped!");
            }
        }.start();
    }
    
    /**
     * Stops this thread and redrawing the chess board.
     */
    public void stop() {
        stop = true;
        //xb.printMoves();
    }
}