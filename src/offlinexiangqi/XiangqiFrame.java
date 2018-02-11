package offlinexiangqi;

import java.awt.Dimension;
import javax.swing.JFrame;

public class XiangqiFrame extends JFrame {
    /**
     * The content panel/chess panel
     */
    private XiangqiPanel xp;
    
    /**
     * Default constructor
     */
    public XiangqiFrame() {
        super("Offline Xiangqi");
        xp = new XiangqiPanel();
        setSize(new Dimension(550, 660));
        super.getContentPane().add(xp);
        super.setResizable(false);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        xp.start();
    }
    
    /**
     * Stops redrawing the chess board
     */
    public void stop() {
        System.out.println("STOP!");
        xp.stop();
    }
}